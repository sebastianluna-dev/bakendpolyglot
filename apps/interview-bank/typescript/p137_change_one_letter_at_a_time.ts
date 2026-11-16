// 137 · Change One Letter at a Time
import assert from "node:assert/strict";

export function shortestChain(start: string, target: string, words: string[]): number {
  const dictionary = new Set(words);
  const length = new Map<string, number>([[start, 1]]); // words in the shortest chain that ends here
  const queue = [start];
  let head = 0;
  while (head < queue.length) {
    const word = queue[head++];
    if (word === target) return length.get(word)!;
    for (let i = 0; i < word.length; i++) {
      for (let code = 97; code <= 122; code++) {
        const next = word.slice(0, i) + String.fromCharCode(code) + word.slice(i + 1);
        if (dictionary.has(next) && !length.has(next)) {
          length.set(next, length.get(word)! + 1);
          queue.push(next);
        }
      }
    }
  }
  return 0;
}

assert.equal(shortestChain("cold", "warm", ["cord", "card", "ward", "warm", "worm", "word", "wore"]), 5);
assert.equal(shortestChain("lead", "gold", ["load", "goad"]), 0);
assert.equal(shortestChain("cat", "cot", ["cot"]), 2);
assert.equal(shortestChain("cat", "dog", []), 0);
assert.equal(shortestChain("hit", "hot", ["hot", "hot", "hit"]), 2);
assert.equal(shortestChain("mine", "bond", ["mind", "bind", "bond", "mend"]), 4);
assert.equal(shortestChain("a", "c", ["b", "c"]), 2);
console.log("137 ok");
