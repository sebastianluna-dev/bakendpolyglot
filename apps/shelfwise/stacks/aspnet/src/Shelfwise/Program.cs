using Npgsql;
using Shelfwise;
using Shelfwise.Data;
using Shelfwise.Domain;
using Shelfwise.Http;

// The chiseled/distroless-style runtime image has no curl, so the container checks itself (compose healthcheck).
if (args.Contains("--healthcheck"))
{
    var port = AppConfig.Load(Environment.GetEnvironmentVariable, needDatabase: false).Port;
    using var http = new HttpClient { Timeout = TimeSpan.FromSeconds(2) };
    try { return (await http.GetAsync($"http://127.0.0.1:{port}/health")).IsSuccessStatusCode ? 0 : 1; }
    catch (HttpRequestException) { return 1; }
}

var config = AppConfig.Load(Environment.GetEnvironmentVariable);
var builder = WebApplication.CreateBuilder(args);
builder.WebHost.UseUrls($"http://0.0.0.0:{config.Port}");
builder.Logging.ClearProviders().AddJsonConsole().SetMinimumLevel(config.LogLevel);
builder.Logging.AddFilter("Microsoft.AspNetCore", LogLevel.Warning);

// Composition root: everything is a singleton — NpgsqlDataSource owns the connection pool.
builder.Services.AddSingleton(NpgsqlDataSource.Create(Sql.ConnectionString(config.DatabaseUrl)));
builder.Services.AddSingleton<BookRepository>();
builder.Services.AddSingleton<MemberRepository>();
builder.Services.AddSingleton<LoanRepository>();
builder.Services.AddSingleton(TimeProvider.System);
builder.Services.AddSingleton(new LoanRules(config.LoanDays, config.MaxLoansPerMember));
builder.Services.AddSingleton(sp => new LoanService(
    sp.GetRequiredService<LoanRepository>(), sp.GetRequiredService<LoanRules>(), sp.GetRequiredService<TimeProvider>()));
builder.Services.Configure<HostOptions>(o => o.ShutdownTimeout = TimeSpan.FromSeconds(10)); // drain on SIGTERM

var app = builder.Build();

app.Use(async (ctx, next) =>
{
    var id = ctx.Request.Headers["X-Request-Id"].ToString();
    ctx.Response.Headers["X-Request-Id"] = id == "" ? Guid.NewGuid().ToString("N") : id;
    await next(ctx);
});
app.Use(Problems.Handle);
Endpoints.Map(app);

await app.RunAsync();
return 0;
