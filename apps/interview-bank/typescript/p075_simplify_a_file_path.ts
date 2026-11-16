// 075 · Simplify a File Path
import assert from "node:assert/strict";

export function simplifyPath(path: string): string {
  const names: string[] = [];
  for (const part of path.split("/")) {
    if (part === "..") {
      if (names.length > 0) names.pop();
    } else if (part !== "" && part !== ".") {
      names.push(part);
    }
  }
  return "/" + names.join("/");
}

assert.equal(simplifyPath("/shelfwise//covers/./2024/../thumbs/"), "/shelfwise/covers/thumbs");
assert.equal(simplifyPath("/../a/.../b/.."), "/a/...");
assert.equal(simplifyPath("/"), "/");
assert.equal(simplifyPath("/../.."), "/");
assert.equal(simplifyPath("/a//b////c/d//././/.."), "/a/b/c");
assert.equal(simplifyPath("/.hidden/..x/."), "/.hidden/..x");
console.log("075 ok");
