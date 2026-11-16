# 146 · Rearrange So No Neighbours Match
import heapq
from collections import Counter


def rearrange(s: str) -> str:
    # (-copies left, letter): most copies first, then the earlier letter
    heap = [(-c, ch) for ch, c in Counter(s).items()]
    heapq.heapify(heap)
    out: list[str] = []
    held = (0, "")  # the letter just placed, kept out of the heap for one step
    while heap:
        neg, ch = heapq.heappop(heap)
        out.append(ch)
        if held[0] < 0:
            heapq.heappush(heap, held)
        held = (neg + 1, ch)
    return "".join(out) if len(out) == len(s) else ""


if __name__ == "__main__":
    assert rearrange("aab") == "aba"
    assert rearrange("aaab") == ""
    assert rearrange("vvvlo") == "vlvov"
    assert rearrange("aabbcc") == "abcabc"
    assert rearrange("aaabbc") == "ababac"
    assert rearrange("a") == "a"
    assert rearrange("zz") == ""
    print("146 ok")
