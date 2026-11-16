// 160 · Split Into Palindromes
import assert from "node:assert/strict";

export function palindromeSplits(s: string): string[][] {
  const result: string[][] = [];
  const path: string[] = [];
  function isPalindrome(lo: number, hi: number): boolean {
    while (lo < hi) {
      if (s[lo] !== s[hi]) return false;
      lo++;
      hi--;
    }
    return true;
  }
  function walk(start: number): void {
    if (start === s.length) {
      result.push([...path]);
      return;
    }
    for (let end = start; end < s.length; end++) {
      if (!isPalindrome(start, end)) continue;
      path.push(s.slice(start, end + 1));
      walk(end + 1);
      path.pop();
    }
  }
  walk(0);
  return result;
}

assert.deepEqual(palindromeSplits("aab"), [["a", "a", "b"], ["aa", "b"]]);
assert.deepEqual(palindromeSplits("noon"), [["n", "o", "o", "n"], ["n", "oo", "n"], ["noon"]]);
assert.deepEqual(palindromeSplits("a"), [["a"]]);
assert.deepEqual(palindromeSplits("ab"), [["a", "b"]]);
assert.deepEqual(palindromeSplits("aaa"), [["a", "a", "a"], ["a", "aa"], ["aa", "a"], ["aaa"]]);
assert.deepEqual(palindromeSplits("racecar").at(-1), ["racecar"]);
console.log("160 ok");
