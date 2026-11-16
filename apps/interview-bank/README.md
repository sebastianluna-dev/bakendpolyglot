# Interview bank

The 200 problems of Chapter 49, each solved in TypeScript, C#, Java, Kotlin, Go and Python with the
same algorithm, the same function name and the same examples. Every file runs on its own and checks
its examples; CI runs them all on every change.

| Language | One problem | All problems |
| --- | --- | --- |
| TypeScript | `node typescript/p002_two_prices.ts` (Node 22.18+ strips types) | `make typescript` |
| C# | `dotnet run csharp/p002_two_prices.cs` (.NET 10 file-based apps) | `make csharp` |
| Java | `java java/P002TwoPrices.java` (single-file source launcher) | `make java` |
| Kotlin | `kotlinc kotlin/p002_two_prices.kt -include-runtime -d p.jar && java -jar p.jar` | `make kotlin` |
| Go | `cd go && go test ./p002/` (one package per problem) | `make go` |
| Python | `python3 python/p002_two_prices.py` | `make python` |

To check a few problems in all six languages at once, before pushing: `./check.sh 001-010`, or
`ONLY=go,py ./check.sh 002` for some languages only. It needs every toolchain on `PATH` and
`KOTLIN_STDLIB` pointing at `kotlin-stdlib.jar`.

Each Go problem is its own package (`go/p002/`), so every file can declare its own helper types,
such as a `ListNode`, without clashing with another problem's.
