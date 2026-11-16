# 007 · Add One to a Digit Array


def add_one(digits: list[int]) -> list[int]:
    result = digits.copy()
    for i in range(len(result) - 1, -1, -1):
        if result[i] < 9:
            result[i] += 1
            return result
        result[i] = 0
    return [1] + result  # every digit was 9


if __name__ == "__main__":
    digits = [1, 2, 9]
    assert add_one(digits) == [1, 3, 0]
    assert digits == [1, 2, 9]
    assert add_one([9, 9, 9]) == [1, 0, 0, 0]
    assert add_one([4, 5]) == [4, 6]
    assert add_one([0]) == [1]
    assert add_one([9] * 20) == [1] + [0] * 20
    print("007 ok")
