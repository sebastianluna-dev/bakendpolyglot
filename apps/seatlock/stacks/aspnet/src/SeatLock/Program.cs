using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.EntityFrameworkCore;
using SeatLock;
using SeatLock.Data;
using SeatLock.Http;
using SeatLock.Services;

// The runtime image has no curl, so the container checks itself (compose healthcheck).
if (args.Contains("--healthcheck"))
{
    var port = Environment.GetEnvironmentVariable("PORT") is { Length: > 0 } p ? p : "3000";
    using var http = new HttpClient { Timeout = TimeSpan.FromSeconds(2) };
    try { return (await http.GetAsync($"http://127.0.0.1:{port}/health")).IsSuccessStatusCode ? 0 : 1; }
    catch (HttpRequestException) { return 1; }
}

var config = AppConfig.Load(Environment.GetEnvironmentVariable);
var builder = WebApplication.CreateBuilder(args);
builder.WebHost.UseUrls($"http://0.0.0.0:{config.Port}");
builder.Logging.ClearProviders().AddJsonConsole();
builder.Logging.AddFilter("Microsoft.AspNetCore", LogLevel.Warning).AddFilter("Microsoft.EntityFrameworkCore", LogLevel.Warning);

builder.Services.AddSingleton(config);
builder.Services.AddSingleton(TimeProvider.System);
builder.Services.AddDbContext<SeatLockDb>(o => o.UseNpgsql(config.ConnectionString()));

// Controllers, with ASP.NET's automatic 400 for invalid models turned off: Body does validation.
builder.Services.AddControllers().ConfigureApiBehaviorOptions(o => o.SuppressModelStateInvalidFilter = true);

builder.Services.AddAuthentication(JwtBearerDefaults.AuthenticationScheme).AddJwtBearer(o => Auth.Configure(o, config));
builder.Services.AddAuthorization();

// HybridCache: in-process L1, plus Redis as L2 when REDIS_URL is set (Chapter 25).
if (config.RedisConfiguration() is { } redis) builder.Services.AddStackExchangeRedisCache(o => o.Configuration = redis);
builder.Services.AddHybridCache();

builder.Services.AddHttpClient<PaymentsClient>(c =>
{
    c.BaseAddress = new Uri(config.PaymentsUrl + "/");
    c.Timeout = TimeSpan.FromSeconds(5);
});
builder.Services.AddScoped<EventService>();
builder.Services.AddScoped<HoldService>();
builder.Services.AddScoped<CheckoutService>();
builder.Services.AddScoped<IdempotencyService>();
builder.Services.AddScoped<PaymentEvents>();
builder.Services.AddHostedService<ReaperWorker>();
builder.Services.Configure<HostOptions>(o => o.ShutdownTimeout = TimeSpan.FromSeconds(10));

var app = builder.Build();

app.Use(async (ctx, next) =>
{
    var id = ctx.Request.Headers["X-Request-Id"].ToString();
    ctx.Response.Headers["X-Request-Id"] = id == "" ? Guid.NewGuid().ToString("N") : id;
    await next(ctx);
});
app.Use(Problems.Handle);
app.UseAuthentication();
app.UseAuthorization();
app.MapGet("/health", () => Results.Json(new { status = "ok" })).AllowAnonymous();
app.MapControllers();
app.MapFallback(new RequestDelegate(ctx => throw SeatLock.Domain.DomainException.NotFound("Route", ctx.Request.Path.Value ?? "/")));

await app.RunAsync();
return 0;
