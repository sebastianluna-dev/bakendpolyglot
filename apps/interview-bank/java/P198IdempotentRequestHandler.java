// 198 · Idempotent Request Handler
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class P198IdempotentRequestHandler {
    record ApiResponse(int status, String body) {}

    static final ApiResponse CONFLICT = new ApiResponse(422, "idempotency key reused with a different request");

    static class IdempotentHandler {
        private record Stored(String body, ApiResponse response) {}

        private final Function<String, ApiResponse> operation;
        private final Map<String, Stored> seen = new HashMap<>(); // key -> first request body and its response

        IdempotentHandler(Function<String, ApiResponse> operation) {
            this.operation = operation;
        }

        ApiResponse handle(String key, String body) {
            Stored stored = seen.get(key);
            if (stored != null) return body.equals(stored.body()) ? stored.response() : CONFLICT;
            ApiResponse response = operation.apply(body);
            seen.put(key, new Stored(body, response));
            return response;
        }
    }

    public static void main(String[] args) {
        int[] runs = {0};
        IdempotentHandler h = new IdempotentHandler(body -> {
            runs[0]++;
            if (body.equals("hold=3")) return new ApiResponse(409, "seat already sold");
            return new ApiResponse(201, "order " + runs[0]);
        });
        check(h.handle("k1", "hold=7").equals(new ApiResponse(201, "order 1")));
        check(h.handle("k1", "hold=7").equals(new ApiResponse(201, "order 1")));
        check(h.handle("k2", "hold=9").equals(new ApiResponse(201, "order 2")));
        check(h.handle("k1", "hold=8").equals(CONFLICT));
        check(h.handle("k1", "hold=7").equals(new ApiResponse(201, "order 1")));
        check(runs[0] == 2);
        check(h.handle("k3", "hold=3").equals(new ApiResponse(409, "seat already sold")));
        check(h.handle("k3", "hold=3").equals(new ApiResponse(409, "seat already sold")));
        check(runs[0] == 3);
        check(h.handle("k4", "hold=7").equals(new ApiResponse(201, "order 4")));
        check(runs[0] == 4);
        System.out.println("198 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
