# 125 · Find the Trusted Judge


def find_judge(n: int, trust: list[list[int]]) -> int:
    score = [0] * (n + 1)  # trusted by, minus trusts
    for a, b in trust:
        score[a] -= 1
        score[b] += 1
    for person in range(1, n + 1):
        if score[person] == n - 1:
            return person
    return -1


if __name__ == "__main__":
    assert find_judge(4, [[1, 3], [2, 3], [4, 3], [4, 1]]) == 3
    assert find_judge(3, [[1, 3], [2, 3], [3, 1]]) == -1
    assert find_judge(1, []) == 1
    assert find_judge(2, []) == -1
    assert find_judge(2, [[1, 2], [2, 1]]) == -1
    assert find_judge(3, [[1, 2], [2, 3]]) == -1
    print("125 ok")
