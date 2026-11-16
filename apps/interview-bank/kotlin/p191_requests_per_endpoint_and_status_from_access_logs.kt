// 191 · Requests per Endpoint and Status From Access Logs
package p191

fun countRequests(lines: List<String>): List<String> {
    val counts = HashMap<String, Int>() // "METHOD PATH STATUS" -> requests
    for (line in lines) {
        val fields = line.split(" ")
        if (fields.size != 5 || "" in fields) continue
        val key = "${fields[1]} ${fields[2]} ${fields[3]}"
        counts[key] = (counts[key] ?: 0) + 1
    }
    return counts.entries
        .sortedWith(compareByDescending<Map.Entry<String, Int>> { it.value }.thenBy { it.key })
        .map { "${it.key} ${it.value}" }
}

fun main() {
    check(
        countRequests(
            listOf(
                "2026-03-02T09:00:01Z GET /books 200 12",
                "2026-03-02T09:00:02Z POST /loans 201 40",
                "2026-03-02T09:00:03Z GET /books 200 9",
                "2026-03-02T09:00:04Z POST /loans 409 15",
                "2026-03-02T09:00:05Z GET /books 200 11",
                "2026-03-02T09:00:06Z POST /loans 201 38",
            ),
        ) == listOf("GET /books 200 3", "POST /loans 201 2", "POST /loans 409 1"),
    )
    check(
        countRequests(
            listOf(
                "2026-03-02T09:01:00Z GET /members/7 404 3",
                "",
                "garbage",
                "2026-03-02T09:01:02Z DELETE /holds/9 204 5",
                "2026-03-02T09:01:03Z GET /members/7 200 4",
            ),
        ) == listOf("DELETE /holds/9 204 1", "GET /members/7 200 1", "GET /members/7 404 1"),
    )
    check(countRequests(listOf()).isEmpty())
    check(
        countRequests(
            listOf(
                "2026-03-02T09:02:00Z GET  /books 200 7",
                "2026-03-02T09:02:01Z GET /books 200 7 ",
                "2026-03-02T09:02:02Z GET /books 200",
            ),
        ).isEmpty(),
    )
    check(
        countRequests(listOf("t1 GET /a 500 1", "t2 GET /b 200 1", "t3 GET /a 500 1", "t4 GET /b 200 1", "t5 GET /a 200 1")) ==
            listOf("GET /a 500 2", "GET /b 200 2", "GET /a 200 1"),
    )
    println("191 ok")
}
