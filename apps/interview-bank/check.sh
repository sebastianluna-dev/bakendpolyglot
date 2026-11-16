#!/usr/bin/env bash
# Runs some problems in all six languages, for checking a batch locally before CI does.
#   ./check.sh 001 002 003        # those problems
#   ./check.sh 001-010            # a range
#   ONLY=go,py ./check.sh 001     # some languages only (ts, cs, java, kotlin, go, py)
# Needs node 22.18+, the .NET 10 SDK, a JDK 21, kotlinc, Go 1.24+ and Python 3.12 on PATH.
set -u
cd "$(dirname "$0")"

nums=()
for arg in "$@"; do
  if [[ $arg == *-* ]]; then
    for ((k = 10#${arg%-*}; k <= 10#${arg#*-}; k++)); do nums+=("$(printf '%03d' "$k")"); done
  else
    nums+=("$arg")
  fi
done
[ ${#nums[@]} -gt 0 ] || { echo "usage: ./check.sh 001 [002 …] | 001-010"; exit 2; }

only=",${ONLY:-ts,cs,java,kotlin,go,py},"
failed=0
fail() { echo "FAIL $1"; failed=1; }
want() { [[ $only == *",$1,"* ]]; }

for n in "${nums[@]}"; do
  if want ts; then for f in typescript/p${n}_*.ts; do node "$f" >/dev/null || fail "ts $n"; done; fi
  if want py; then for f in python/p${n}_*.py; do python3 "$f" >/dev/null || fail "py $n"; done; fi
  if want java; then for f in java/P${n}*.java; do java "$f" >/dev/null || fail "java $n"; done; fi
  if want go; then (cd go && go vet "./p$n/" && go test -count=1 "./p$n/" >/dev/null) || fail "go $n"; fi
  if want cs; then for f in csharp/p${n}_*.cs; do dotnet run "$f" >/dev/null || fail "cs $n"; done; fi
done

if want kotlin; then
  files=()
  for n in "${nums[@]}"; do for f in kotlin/p${n}_*.kt; do files+=("$f"); done; done
  mkdir -p build
  if kotlinc "${files[@]}" -d build/check-$$.jar 2>build/kotlinc-$$.log; then
    for f in "${files[@]}"; do
      c=$(basename "$f" .kt)
      pkg=$(sed -n 's/^package //p' "$f" | head -1)
      java -cp "build/check-$$.jar:${KOTLIN_STDLIB:?set KOTLIN_STDLIB to kotlin-stdlib.jar}" "$pkg.$(echo "${c:0:1}" | tr a-z A-Z)${c:1}Kt" >/dev/null || fail "kotlin $c"
    done
  else
    cat build/kotlinc-$$.log; fail "kotlin compile"
  fi
fi

[ $failed -eq 0 ] && echo "ok: ${nums[*]}"
exit $failed
