# 046 · Is One a Subsequence of the Other?


def is_subsequence(plan: str, scans: str) -> bool:
    i = j = 0
    while i < len(plan) and j < len(scans):
        if plan[i] == scans[j]:
            i += 1
        j += 1
    return i == len(plan)


if __name__ == "__main__":
    assert is_subsequence("ACE", "ABCDE") is True
    assert is_subsequence("AEC", "ABCDE") is False
    assert is_subsequence("", "ABC") is True
    assert is_subsequence("A", "") is False
    assert is_subsequence("AA", "ABA") is True
    assert is_subsequence("AAB", "ABAC") is False
    print("046 ok")
