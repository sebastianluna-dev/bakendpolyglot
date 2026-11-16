# 053 · Three Readings That Sum to Zero


def three_to_zero(readings: list[int]) -> list[list[int]]:
    s = sorted(readings)
    result: list[list[int]] = []
    for i in range(len(s) - 2):
        if i > 0 and s[i] == s[i - 1]:
            continue  # same first value, same triplets
        lo, hi = i + 1, len(s) - 1
        while lo < hi:
            total = s[i] + s[lo] + s[hi]
            if total < 0:
                lo += 1
            elif total > 0:
                hi -= 1
            else:
                result.append([s[i], s[lo], s[hi]])
                lo += 1
                hi -= 1
                while lo < hi and s[lo] == s[lo - 1]:
                    lo += 1
    return result


if __name__ == "__main__":
    assert three_to_zero([-4, 1, 3, 0, -1, 2, -1]) == [[-4, 1, 3], [-1, -1, 2], [-1, 0, 1]]
    assert three_to_zero([0, 0, 0, 0]) == [[0, 0, 0]]
    assert three_to_zero([1, 2, -4]) == []
    assert three_to_zero([]) == []
    assert three_to_zero([5, -5]) == []
    assert three_to_zero([2, -2, 0, 2, 0]) == [[-2, 0, 2]]
    print("053 ok")
