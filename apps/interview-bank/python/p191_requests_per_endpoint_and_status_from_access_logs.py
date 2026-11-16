# 191 · Requests per Endpoint and Status From Access Logs


def count_requests(lines: list[str]) -> list[str]:
    counts: dict[str, int] = {}  # "METHOD PATH STATUS" -> requests
    for line in lines:
        fields = line.split(" ")
        if len(fields) != 5 or "" in fields:
            continue
        key = f"{fields[1]} {fields[2]} {fields[3]}"
        counts[key] = counts.get(key, 0) + 1
    ordered = sorted(counts.items(), key=lambda kv: (-kv[1], kv[0]))
    return [f"{key} {n}" for key, n in ordered]


if __name__ == "__main__":
    assert count_requests([
        "2026-03-02T09:00:01Z GET /books 200 12",
        "2026-03-02T09:00:02Z POST /loans 201 40",
        "2026-03-02T09:00:03Z GET /books 200 9",
        "2026-03-02T09:00:04Z POST /loans 409 15",
        "2026-03-02T09:00:05Z GET /books 200 11",
        "2026-03-02T09:00:06Z POST /loans 201 38",
    ]) == ["GET /books 200 3", "POST /loans 201 2", "POST /loans 409 1"]
    assert count_requests([
        "2026-03-02T09:01:00Z GET /members/7 404 3",
        "",
        "garbage",
        "2026-03-02T09:01:02Z DELETE /holds/9 204 5",
        "2026-03-02T09:01:03Z GET /members/7 200 4",
    ]) == ["DELETE /holds/9 204 1", "GET /members/7 200 1", "GET /members/7 404 1"]
    assert count_requests([]) == []
    assert count_requests([
        "2026-03-02T09:02:00Z GET  /books 200 7",
        "2026-03-02T09:02:01Z GET /books 200 7 ",
        "2026-03-02T09:02:02Z GET /books 200",
    ]) == []
    assert count_requests([
        "t1 GET /a 500 1",
        "t2 GET /b 200 1",
        "t3 GET /a 500 1",
        "t4 GET /b 200 1",
        "t5 GET /a 200 1",
    ]) == ["GET /a 500 2", "GET /b 200 2", "GET /a 200 1"]
    print("191 ok")
