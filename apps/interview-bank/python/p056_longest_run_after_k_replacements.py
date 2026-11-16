# 056 · Longest Run After k Replacements


def longest_run_after_replacements(s: str, k: int) -> int:
    counts = [0] * 26
    left = best = 0
    for right in range(len(s)):
        counts[ord(s[right]) - ord("A")] += 1
        # Letters to replace = window length minus the most common letter in it.
        while right - left + 1 - max(counts) > k:
            counts[ord(s[left]) - ord("A")] -= 1
            left += 1
        best = max(best, right - left + 1)
    return best


if __name__ == "__main__":
    assert longest_run_after_replacements("ABAACBA", 1) == 4
    assert longest_run_after_replacements("XYXY", 2) == 4
    assert longest_run_after_replacements("", 0) == 0
    assert longest_run_after_replacements("AAAA", 0) == 4
    assert longest_run_after_replacements("ABCD", 0) == 1
    assert longest_run_after_replacements("AB", 5) == 2
    assert longest_run_after_replacements("BAAABBBBA", 2) == 6
    print("056 ok")
