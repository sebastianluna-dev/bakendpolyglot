// 122 · A Word Dictionary With Wildcards
import assert from "node:assert/strict";

class TrieNode {
  children: (TrieNode | null)[] = new Array<TrieNode | null>(26).fill(null);
  isWord = false;
}

export class WordDictionary {
  root = new TrieNode();

  addWord(word: string): void {
    let node = this.root;
    for (const ch of word) {
      const i = ch.charCodeAt(0) - 97;
      node = node.children[i] ??= new TrieNode();
    }
    node.isWord = true;
  }

  search(pattern: string): boolean {
    return this.match(this.root, pattern, 0);
  }

  private match(node: TrieNode, pattern: string, i: number): boolean {
    if (i === pattern.length) return node.isWord;
    if (pattern[i] === ".") {
      return node.children.some((child) => child !== null && this.match(child, pattern, i + 1));
    }
    const next = node.children[pattern.charCodeAt(i) - 97];
    return next !== null && this.match(next, pattern, i + 1);
  }
}

const d = new WordDictionary();
d.addWord("loan");
d.addWord("lean");
assert.equal(d.search("l.an"), true);
assert.equal(d.search("lo"), false);
assert.equal(d.search("l..."), true);
d.addWord("load");
assert.equal(d.search("loan"), true);
assert.equal(d.search("lo.d"), true);
assert.equal(d.search("le.d"), false);
assert.equal(d.search("loans"), false);
assert.equal(d.search("...."), true);
assert.equal(d.search("..."), false);
assert.equal(d.search("x..."), false);

const e = new WordDictionary();
assert.equal(e.search("."), false);
e.addWord("a");
assert.equal(e.search("."), true);
assert.equal(e.search("a"), true);
assert.equal(e.search("b"), false);
e.addWord("a");
assert.equal(e.search("a"), true);
console.log("122 ok");
