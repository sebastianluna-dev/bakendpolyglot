// 198 · Idempotent Request Handler
package p198

data class ApiResponse(val status: Int, val body: String)

val CONFLICT = ApiResponse(422, "idempotency key reused with a different request")

class IdempotentHandler(private val operation: (String) -> ApiResponse) {
    private val seen = HashMap<String, Pair<String, ApiResponse>>() // key -> first request body and its response

    fun handle(key: String, body: String): ApiResponse {
        val stored = seen[key]
        if (stored != null) {
            val (firstBody, response) = stored
            return if (body == firstBody) response else CONFLICT
        }
        val response = operation(body)
        seen[key] = body to response
        return response
    }
}

fun main() {
    var runs = 0
    val h = IdempotentHandler { body ->
        runs++
        if (body == "hold=3") ApiResponse(409, "seat already sold") else ApiResponse(201, "order $runs")
    }
    check(h.handle("k1", "hold=7") == ApiResponse(201, "order 1"))
    check(h.handle("k1", "hold=7") == ApiResponse(201, "order 1"))
    check(h.handle("k2", "hold=9") == ApiResponse(201, "order 2"))
    check(h.handle("k1", "hold=8") == CONFLICT)
    check(h.handle("k1", "hold=7") == ApiResponse(201, "order 1"))
    check(runs == 2)
    check(h.handle("k3", "hold=3") == ApiResponse(409, "seat already sold"))
    check(h.handle("k3", "hold=3") == ApiResponse(409, "seat already sold"))
    check(runs == 3)
    check(h.handle("k4", "hold=7") == ApiResponse(201, "order 4"))
    check(runs == 4)
    println("198 ok")
}
