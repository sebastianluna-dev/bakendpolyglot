using Shelfwise.Domain;

namespace Shelfwise.Http;

/// <summary>Every error leaves as application/problem+json (RFC 9457, Chapter 12).</summary>
public static class Problems
{
    const string TypeBase = "https://shelfwise.dev/problems/";

    public static Task Write(HttpContext ctx, DomainException e)
    {
        var body = new Dictionary<string, object?>
        {
            ["type"] = TypeBase + e.Code,
            ["title"] = e.Title,
            ["status"] = e.Status,
            ["detail"] = e.Message,
            ["instance"] = ctx.Request.Path.Value,
        };
        foreach (var (key, value) in e.Extensions) body[key] = value;
        ctx.Response.StatusCode = e.Status;
        return ctx.Response.WriteAsJsonAsync(body, (System.Text.Json.JsonSerializerOptions?)null, "application/problem+json");
    }

    /// <summary>
    /// Middleware: domain exceptions become their problem; anything else is logged and becomes a 500
    /// that quotes the request id. Minimal APIs have no filters for this, so it's a plain try/catch.
    /// </summary>
    public static async Task Handle(HttpContext ctx, RequestDelegate next)
    {
        try
        {
            await next(ctx);
        }
        catch (DomainException e) when (!ctx.Response.HasStarted)
        {
            await Write(ctx, e);
        }
        catch (Exception e) when (!ctx.Response.HasStarted && e is not OperationCanceledException)
        {
            var requestId = ctx.Response.Headers["X-Request-Id"].ToString();
            ctx.RequestServices.GetRequiredService<ILoggerFactory>().CreateLogger("Shelfwise")
                .LogError(e, "unhandled error on {Path} (request {RequestId})", ctx.Request.Path, requestId);
            await Write(ctx, DomainException.Internal(requestId));
        }
    }
}
