# Chapter 49 — notes for the author

Details that could not be confirmed for a specific version, and decisions you may want to revisit. Not published: this file is for the author.

## Decisions taken without asking

- **Print language for the bank: Python.** One constant switches it: `BANK_PRINT_STACK` in `src/components/book/stacks.ts`. The web edition shows all six in synced tabs.
- **The bank lives on 13 topic pages plus an index**, not inside the chapter-49 page: 1,200 code blocks on one page would be very heavy. They have `continues: 49` in their frontmatter, so the print edition renders them inside Chapter 49, after its body, each topic on a new page. The book's ending is the page `49-15-the-end.mdx`, which prints last.
- **The book shows each solution straight from `apps/interview-bank`** (the `Solutions` component), so the chapter and CI can never disagree. The whole file is shown, checks included, so every solution is complete and runnable as printed.
- **Each Go problem is its own package** (`go/p002/`), so every file can declare its own `ListNode` or helpers.
- **Chapter 48** keeps its gate checklist under "Gate: Phase 5"; its closing paragraph moved to the start of "The End of the Book", and a one-line bridge to Chapter 49 replaces it.
- **Commits** are authored as Sebastian Luna, without a co-author line (earlier commits in this repo were authored as "Claude").

## Part 1
- (a) Node 22.18+ is needed to run `.ts` files without a flag (type stripping on by default).
- (b) `dotnet run file.cs` (file-based apps) needs the .NET 10 SDK.
- (c) CI downloads kotlinc 2.1.21 from GitHub releases.
- (d) TypeScript section: minimum versions quoted from memory — `toSorted` Node 20+, `Map.groupBy` Node 21+, Set methods (`intersection`, `union`, `difference`) Node 22+. Checked only on Node 24.13.
- (e) `node --test` picking up `*.test.ts` files automatically: checked on Node 24, not on 22.18.
- (f) `Math.max(...a)`: threw `RangeError` at 200,000 elements and worked at 100,000 on Node 24; the text says "very large arrays" without a number.
- (g) `shift()` is described as "O(n) in the worst case"; the spec does not promise O(1) and the text does not claim what V8 does.

## Problem bank
- 004: the entry says Go's `int` is 64 bits "on the usual platforms" (it is 32 bits on 32-bit targets).
- 007: Python's `int` can hold the digits, but since Python 3.11 converting an `int` of more than 4,300 digits to or from a string raises `ValueError` by default. Not mentioned in the entry.
- 013 and 017: Java class names keep "IDs" as in the titles (`P013CollapseConsecutiveIDsIntoRanges`, `P017LongestRunOfConsecutiveIDs`). Rename to `Ids` if you prefer.
- 014: the "Common mistake" says Go maps, Java's `HashMap` and C#'s `Dictionary` do not promise insertion order. True as documented, though C#'s `Dictionary` usually keeps it when nothing is removed; the entry says groups "can" come back out of order.
- 023: the production random calls listed are Kotlin `Random.nextInt(n)` (`kotlin.random`) and Go `rand.Intn(n)` (`math/rand`); in `math/rand/v2` it is `rand.IntN`.
- 026: the Approach says every character test except the TypeScript regex also accepts non-ASCII letters (`char.IsLetterOrDigit`, `Character.isLetterOrDigit`, Kotlin `isLetterOrDigit()`, Go `unicode.IsLetter`/`IsDigit`, `str.isalnum`). Worth a glance.
- 033: the complexity depends on each library's substring search; the entry says "O(n) for a linear algorithm, up to O(n²) for a simple search" without claiming what any one library does.
- 036: C# uses `Array.Reverse(words)`. Checked on .NET 10: `words.Reverse()` on an `int[]` leaves the array unchanged (it binds to an overload that returns a new sequence), so the explicit call is the safe one.
- 038: checked on .NET 10 that `int.Parse(" 42 ")` returns 42 (default number style allows surrounding spaces). Not run: `strconv.Atoi` accepting any 64-bit value on 64-bit platforms.
- C# section: `PriorityQueue.Remove` is described as added in .NET 9 (present in .NET 10; version not confirmed).
- C# section: "small arrays use insertion sort" (stable up to 16 elements, unstable from 17 on .NET 10) describes current runtime behaviour, not a documented promise.
- C# section: the culture-sensitive string order example assumes ICU; in invariant-globalization mode comparisons become ordinal and the example would print differently.
- Backend round (49.8): the Spring Boot Java and Kotlin handlers were compiled only against stand-in classes, not real Spring. To confirm against Boot 3.5: `ResponseEntity.status(..).contentType(APPLICATION_PROBLEM_JSON).body(ProblemDetail)` with `setProperty("errors", ..)` appearing as a top-level member, and `spring.mvc.problemdetails.enabled=true` turning the unreadable-body 400 into Problem Details.
- Backend round: Jackson probably also coerces `"1"` to a number by default (not tested), so the text names only ASP.NET Core and Pydantic as lenient.
- Backend round: when a member is at the loan limit and the copy is also on loan, the simplified handlers answer `loan-limit`, a different order from Shelfwise's.
- Backend round: verified TypeScript (tsc + node --test), Go (vet, build, httptest), C# (.NET 10, run with curl), FastAPI (TestClient), and the SQL on Postgres 17 with the Shelfwise migration and EXPLAIN ANALYZE.
- Java section: names `@ParameterizedTest`/`@MethodSource` in one sentence; Chapter 17 shows no JUnit parameterized tests, so check it fits.
- Java section: recommends typing `import java.util.*;` in the round; the bank's files use explicit imports.
- Java section: the `PriorityQueue` print `[3, 14, 7]` and "removing the second-to-last element of an `ArrayList` in a for-each skips the last one" are observed JDK 21 behaviour, worded as examples.
- Java section: `void main()` is described as "a preview in Java 21", with nothing said about later versions.
- Kotlin section: `copies.removeLast()` on a `MutableList` compiles against JDK 21 to the JDK's own `List.removeLast()`; fine on JVM 21, a known break on older JVMs and Android. `removeAt(lastIndex)` avoids it if you prefer.
- Kotlin section: the coroutine `mapLimit` was checked against the `kotlinx-coroutines-core-jvm.jar` bundled in the kotlinc 2.1.21 distribution (version unknown); the text says coroutines are not in the standard library.
- 081: says 10⁴ nodes is past CPython's default recursion limit of 1000.
- 092: the Approach says Python's `bisect_left` and Go's `sort.Search` return the first position ≥ x, and that Java's `Arrays.binarySearch` and C#'s `Array.BinarySearch` may return any of several equal positions. Matches the documentation as far as we know; not re-checked against the docs.
- Python section: the 4,300-digit int/str conversion limit is described as "since 3.11, in base 10"; it was also backported to security releases of 3.7–3.10 and applies to every base that isn't a power of two.
- Python section: `@cache` recursion can fail on a C-stack limit below the recursion limit (observed at about 3,300 levels on macOS arm64, Python 3.12.16); the text now says it depends on platform and version, without a number.
- Python section: `map_limit[T, R]` uses the 3.12 type-parameter syntax. Leaves out heapq's public max-heap functions (believed new in 3.14) and the free-threaded build's status after 3.13.
- Go section: says `wg.Go` arrives in Go 1.25 (confirmed: vet on the 1.24 module reports "requires go1.25 or later"). Cut the clause if you'd rather not mention later versions.
- Go section: most code blocks are fragments meant to sit inside `func main()`; pasted alone, Go rejects unused variables. The verified scratch files add the uses.
- 110: Java class `P110SumOfAKeyRangeInABst` treats "BST" as the word "Bst" (as 013 and 017 kept "IDs" uppercase); rename if you prefer one rule.
- Cheat sheets: the TypeScript "Sort by two keys" row escapes `||` as `\|\|` inside the table; three cells may wrap in print (TS and Go "Sort by two keys", the C#/Java `Check` helper). The Python "Map" row uses `Counter(items)` without showing `from collections import Counter`.
- Part 1 length: about 17,800 words of prose without code and tables (24,300 with tables), above the 12–15k target. The language sections are 1,800–2,500 each and the backend round 2,600; the places to cut first are the Python and Kotlin sections and the backend round.
- 147: says Go's `int` is 64 bits "on the platforms the bank runs on" (CI is ubuntu-latest on amd64).
- 152: the remark that C#'s default culture sort can put `"b7x"` first is from one run on macOS; other cultures or invariant-globalization mode may order differently (the entry says "can").
- 185: Java uses `List.getLast()` (new in Java 21).
- 194: checked on Python 3.12 that `parse_qs` drops blank values unless `keep_blank_values=True`.
- 199: the checks require the peak concurrency to equal the limit (so a sequential version fails), with tasks sleeping 20–40 ms. Passed three reruns locally and in CI; if a slow CI runner ever makes it flaky, relax the check to `2 ≤ peak ≤ limit`.
- 200: FNV-1a alone clustered the virtual nodes (`cache-a#0`, `#1`, `#2`), so the ring adds the MurmurHash3 32-bit finalizer (`mix32`); the entry explains it.
