using SeatLock.Domain;

namespace SeatLock.Http;

/// <summary>Every error leaves as application/problem+json (RFC 9457, Chapter 12).</summary>
public static class Problems
{
    const string TypeBase = "https://seatlock.dev/problems/";

    public static Dictionary<string, object?> Body(DomainException e, string instance)
    {
        var body = new Dictionary<string, object?>
        {
            ["type"] = TypeBase + e.Code,
            ["title"] = e.Title,
            ["status"] = e.Status,
            ["detail"] = e.Message,
            ["instance"] = instance,
        };
        foreach (var (key, value) in e.Extensions) body[key] = value;
        return body;
    }

    public static Task Write(HttpContext ctx, DomainException e)
    {
        if (e.Code == "unauthorized") ctx.Response.Headers.WWWAuthenticate = "Bearer realm=\"seatlock\"";
        ctx.Response.StatusCode = e.Status;
        return ctx.Response.WriteAsJsonAsync(Body(e, ctx.Request.Path.Value ?? "/"), (System.Text.Json.JsonSerializerOptions?)null, "application/problem+json");
    }

    /// <summary>Middleware: domain exceptions become their problem; anything else is logged and is a 500.</summary>
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
            ctx.RequestServices.GetRequiredService<ILoggerFactory>().CreateLogger("SeatLock")
                .LogError(e, "unhandled error on {Path} (request {RequestId})", ctx.Request.Path, requestId);
            await Write(ctx, DomainException.Internal(requestId));
        }
    }
}
