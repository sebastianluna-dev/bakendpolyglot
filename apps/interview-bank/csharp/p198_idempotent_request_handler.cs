// 198 · Idempotent Request Handler
var runs = 0;
var h = new IdempotentHandler(body =>
{
    runs++;
    if (body == "hold=3") return new ApiResponse(409, "seat already sold");
    return new ApiResponse(201, $"order {runs}");
});
Check(h.Handle("k1", "hold=7") == new ApiResponse(201, "order 1"));
Check(h.Handle("k1", "hold=7") == new ApiResponse(201, "order 1"));
Check(h.Handle("k2", "hold=9") == new ApiResponse(201, "order 2"));
Check(h.Handle("k1", "hold=8") == IdempotentHandler.Conflict);
Check(h.Handle("k1", "hold=7") == new ApiResponse(201, "order 1"));
Check(runs == 2);
Check(h.Handle("k3", "hold=3") == new ApiResponse(409, "seat already sold"));
Check(h.Handle("k3", "hold=3") == new ApiResponse(409, "seat already sold"));
Check(runs == 3);
Check(h.Handle("k4", "hold=7") == new ApiResponse(201, "order 4"));
Check(runs == 4);
Console.WriteLine("198 ok");

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}

record ApiResponse(int Status, string Body);

class IdempotentHandler(Func<string, ApiResponse> operation)
{
    public static readonly ApiResponse Conflict = new(422, "idempotency key reused with a different request");

    // key -> first request body and its response
    private readonly Dictionary<string, (string Body, ApiResponse Response)> seen = [];

    public ApiResponse Handle(string key, string body)
    {
        if (seen.TryGetValue(key, out var stored)) return body == stored.Body ? stored.Response : Conflict;
        var response = operation(body);
        seen[key] = (body, response);
        return response;
    }
}
