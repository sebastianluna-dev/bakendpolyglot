# 136 · Exchange Rates From Known Pairs
from collections import deque


def exchange_rates(pairs: list[list[str]], rates: list[float], queries: list[list[str]]) -> list[float]:
    graph: dict[str, list[tuple[str, float]]] = {}
    for (frm, to), rate in zip(pairs, rates):
        graph.setdefault(frm, []).append((to, rate))
        graph.setdefault(to, []).append((frm, 1 / rate))
    group: dict[str, int] = {}
    value: dict[str, float] = {}  # units of this currency worth one unit of its group's first
    for start in graph:
        if start in group:
            continue
        gid = len(group)
        group[start] = gid
        value[start] = 1.0
        queue = deque([start])
        while queue:
            cur = queue.popleft()
            for nxt, rate in graph[cur]:
                if nxt in group:
                    continue
                group[nxt] = gid
                value[nxt] = value[cur] * rate
                queue.append(nxt)
    return [
        value[y] / value[x] if x in group and group[x] == group.get(y) else -1.0
        for x, y in queries
    ]


def near(got: list[float], want: list[float]) -> bool:
    return len(got) == len(want) and all(abs(g - w) < 1e-9 for g, w in zip(got, want))


if __name__ == "__main__":
    assert near(
        exchange_rates(
            [["EUR", "USD"], ["USD", "MXN"]],
            [1.25, 20.0],
            [["EUR", "MXN"], ["MXN", "EUR"], ["USD", "USD"], ["EUR", "JPY"], ["JPY", "JPY"]],
        ),
        [25.0, 0.04, 1.0, -1.0, -1.0],
    )
    assert near(
        exchange_rates(
            [["GBP", "EUR"], ["CHF", "SEK"], ["SEK", "NOK"]],
            [1.2, 12.0, 0.5],
            [["EUR", "GBP"], ["CHF", "NOK"], ["NOK", "CHF"], ["GBP", "NOK"]],
        ),
        [0.8333333333, 6.0, 0.1666666667, -1.0],
    )
    assert near(exchange_rates([["EUR", "USD"]], [1.25], []), [])
    assert near(
        exchange_rates([["USD", "CAD"], ["CAD", "AUD"], ["USD", "AUD"]], [1.5, 1.2, 1.8], [["AUD", "USD"], ["CAD", "CAD"]]),
        [0.5555555556, 1.0],
    )
    print("136 ok")
