// 198 · Idempotent Request Handler
import assert from "node:assert/strict";

export type ApiResponse = { status: number; body: string };

export const CONFLICT: ApiResponse = { status: 422, body: "idempotency key reused with a different request" };

export class IdempotentHandler {
  operation: (body: string) => ApiResponse;
  seen = new Map<string, { body: string; response: ApiResponse }>(); // key -> first request body and its response

  constructor(operation: (body: string) => ApiResponse) {
    this.operation = operation;
  }

  handle(key: string, body: string): ApiResponse {
    const stored = this.seen.get(key);
    if (stored !== undefined) return body === stored.body ? stored.response : CONFLICT;
    const response = this.operation(body);
    this.seen.set(key, { body, response });
    return response;
  }
}

let runs = 0;
const h = new IdempotentHandler((body) => {
  runs++;
  if (body === "hold=3") return { status: 409, body: "seat already sold" };
  return { status: 201, body: `order ${runs}` };
});
assert.deepEqual(h.handle("k1", "hold=7"), { status: 201, body: "order 1" });
assert.deepEqual(h.handle("k1", "hold=7"), { status: 201, body: "order 1" });
assert.deepEqual(h.handle("k2", "hold=9"), { status: 201, body: "order 2" });
assert.deepEqual(h.handle("k1", "hold=8"), CONFLICT);
assert.deepEqual(h.handle("k1", "hold=7"), { status: 201, body: "order 1" });
assert.equal(runs, 2);
assert.deepEqual(h.handle("k3", "hold=3"), { status: 409, body: "seat already sold" });
assert.deepEqual(h.handle("k3", "hold=3"), { status: 409, body: "seat already sold" });
assert.equal(runs, 3);
assert.deepEqual(h.handle("k4", "hold=7"), { status: 201, body: "order 4" });
assert.equal(runs, 4);
console.log("198 ok");
