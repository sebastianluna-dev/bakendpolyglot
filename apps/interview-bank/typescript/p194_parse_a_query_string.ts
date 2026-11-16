// 194 · Parse a Query String
import assert from "node:assert/strict";

function hexValue(c: string): number {
  if (c >= "0" && c <= "9") return c.charCodeAt(0) - 48;
  if (c >= "a" && c <= "f") return c.charCodeAt(0) - 97 + 10;
  if (c >= "A" && c <= "F") return c.charCodeAt(0) - 65 + 10;
  return -1;
}

function decode(s: string): string {
  const bytes: number[] = [];
  for (let i = 0; i < s.length; i++) {
    const c = s[i];
    if (c === "+") bytes.push(32);
    else if (c === "%" && i + 2 < s.length && hexValue(s[i + 1]) >= 0 && hexValue(s[i + 2]) >= 0) {
      bytes.push(hexValue(s[i + 1]) * 16 + hexValue(s[i + 2]));
      i += 2;
    } else bytes.push(c.charCodeAt(0)); // a malformed % stays as it is
  }
  return new TextDecoder().decode(new Uint8Array(bytes)); // %XX escapes are bytes; several can make one character
}

export function parseQuery(query: string): Map<string, string[]> {
  const params = new Map<string, string[]>();
  for (const piece of query.split("&")) {
    const eq = piece.indexOf("="); // split at the first = only
    const key = decode(eq < 0 ? piece : piece.slice(0, eq));
    if (key === "") continue;
    const value = eq < 0 ? "" : decode(piece.slice(eq + 1));
    const values = params.get(key);
    if (values === undefined) params.set(key, [value]);
    else values.push(value);
  }
  return params;
}

const m = (entries: Array<[string, string[]]>) => new Map(entries);
assert.deepEqual(parseQuery("genre=sci-fi&sort=title&genre=history"), m([["genre", ["sci-fi", "history"]], ["sort", ["title"]]]));
assert.deepEqual(
  parseQuery("q=war+%26+peace&note=50%25&empty=&flag"),
  m([["q", ["war & peace"]], ["note", ["50%"]], ["empty", [""]], ["flag", [""]]]),
);
assert.deepEqual(parseQuery(""), m([]));
assert.deepEqual(parseQuery("a=1&&b=2&"), m([["a", ["1"]], ["b", ["2"]]]));
assert.deepEqual(parseQuery("=5&k=v"), m([["k", ["v"]]]));
assert.deepEqual(parseQuery("expr=a=b"), m([["expr", ["a=b"]]]));
assert.deepEqual(parseQuery("name=Jos%C3%A9&price=%E2%82%AC5"), m([["name", ["José"]], ["price", ["€5"]]]));
assert.deepEqual(parseQuery("bad=%zz%4&tail=%"), m([["bad", ["%zz%4"]], ["tail", ["%"]]]));
assert.deepEqual(parseQuery("sort%5Bby%5D=title&path=%2fa%2F"), m([["sort[by]", ["title"]], ["path", ["/a/"]]]));
assert.deepEqual(parseQuery("plus=%2B+"), m([["plus", ["+ "]]]));
console.log("194 ok");
