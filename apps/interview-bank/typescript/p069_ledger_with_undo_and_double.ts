// 069 · Ledger With Undo and Double
import assert from "node:assert/strict";

export function ledgerTotal(commands: string[]): number {
  const amounts: number[] = [];
  for (const cmd of commands) {
    const n = amounts.length;
    if (cmd === "U") amounts.pop();
    else if (cmd === "D") amounts.push(2 * amounts[n - 1]);
    else if (cmd === "+") amounts.push(amounts[n - 1] + amounts[n - 2]);
    else amounts.push(Number(cmd));
  }
  return amounts.reduce((sum, x) => sum + x, 0);
}

assert.equal(ledgerTotal(["5", "-2", "D", "+", "U", "10", "+"]), 15);
assert.equal(ledgerTotal(["7", "U"]), 0);
assert.equal(ledgerTotal([]), 0);
assert.equal(ledgerTotal(["100"]), 100);
assert.equal(ledgerTotal(["2", "3", "+"]), 10);
assert.equal(ledgerTotal(["3", "D", "D", "U", "+"]), 18);
assert.equal(ledgerTotal(["-4", "D", "U", "U"]), 0);
console.log("069 ok");
