// 136 · Exchange Rates From Known Pairs
import assert from "node:assert/strict";

export function exchangeRates(pairs: string[][], rates: number[], queries: string[][]): number[] {
  const graph = new Map<string, [string, number][]>();
  pairs.forEach(([from, to], i) => {
    if (!graph.has(from)) graph.set(from, []);
    if (!graph.has(to)) graph.set(to, []);
    graph.get(from)!.push([to, rates[i]]);
    graph.get(to)!.push([from, 1 / rates[i]]);
  });
  const group = new Map<string, number>();
  const value = new Map<string, number>(); // units of this currency worth one unit of its group's first
  for (const start of graph.keys()) {
    if (group.has(start)) continue;
    group.set(start, group.size);
    value.set(start, 1);
    const queue = [start];
    let head = 0;
    while (head < queue.length) {
      const cur = queue[head++];
      for (const [next, rate] of graph.get(cur)!) {
        if (group.has(next)) continue;
        group.set(next, group.get(start)!);
        value.set(next, value.get(cur)! * rate);
        queue.push(next);
      }
    }
  }
  return queries.map(([x, y]) =>
    group.has(x) && group.get(x) === group.get(y) ? value.get(y)! / value.get(x)! : -1.0,
  );
}

function near(got: number[], want: number[]): boolean {
  return got.length === want.length && got.every((g, i) => Math.abs(g - want[i]) < 1e-9);
}

assert.ok(
  near(
    exchangeRates(
      [["EUR", "USD"], ["USD", "MXN"]],
      [1.25, 20.0],
      [["EUR", "MXN"], ["MXN", "EUR"], ["USD", "USD"], ["EUR", "JPY"], ["JPY", "JPY"]],
    ),
    [25.0, 0.04, 1.0, -1.0, -1.0],
  ),
);
assert.ok(
  near(
    exchangeRates(
      [["GBP", "EUR"], ["CHF", "SEK"], ["SEK", "NOK"]],
      [1.2, 12.0, 0.5],
      [["EUR", "GBP"], ["CHF", "NOK"], ["NOK", "CHF"], ["GBP", "NOK"]],
    ),
    [0.8333333333, 6.0, 0.1666666667, -1.0],
  ),
);
assert.ok(near(exchangeRates([["EUR", "USD"]], [1.25], []), []));
assert.ok(
  near(
    exchangeRates([["USD", "CAD"], ["CAD", "AUD"], ["USD", "AUD"]], [1.5, 1.2, 1.8], [["AUD", "USD"], ["CAD", "CAD"]]),
    [0.5555555556, 1.0],
  ),
);
console.log("136 ok");
