# 155 · All Orderings


def all_orderings(nums: list[int]) -> list[list[int]]:
    result: list[list[int]] = []
    path: list[int] = []
    used = [False] * len(nums)

    def walk() -> None:
        if len(path) == len(nums):
            result.append(path.copy())
            return
        for i, x in enumerate(nums):
            if used[i]:
                continue
            used[i] = True
            path.append(x)
            walk()
            path.pop()
            used[i] = False

    walk()
    return result


if __name__ == "__main__":
    assert all_orderings([1, 2, 3]) == [[1, 2, 3], [1, 3, 2], [2, 1, 3], [2, 3, 1], [3, 1, 2], [3, 2, 1]]
    assert all_orderings([3, 1, 2]) == [[3, 1, 2], [3, 2, 1], [1, 3, 2], [1, 2, 3], [2, 3, 1], [2, 1, 3]]
    assert all_orderings([]) == [[]]
    assert all_orderings([9]) == [[9]]
    assert all_orderings([5, -5]) == [[5, -5], [-5, 5]]
    assert len(all_orderings([1, 2, 3, 4, 5, 6])) == 720
    print("155 ok")
