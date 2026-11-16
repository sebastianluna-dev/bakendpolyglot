# 154 · All Subsets


def all_subsets(nums: list[int]) -> list[list[int]]:
    result: list[list[int]] = []
    path: list[int] = []

    def walk(start: int) -> None:
        result.append(path.copy())  # a copy: path keeps changing
        for i in range(start, len(nums)):
            path.append(nums[i])
            walk(i + 1)
            path.pop()

    walk(0)
    return result


if __name__ == "__main__":
    assert all_subsets([1, 2, 3]) == [[], [1], [1, 2], [1, 2, 3], [1, 3], [2], [2, 3], [3]]
    assert all_subsets([4, 1]) == [[], [4], [4, 1], [1]]
    assert all_subsets([]) == [[]]
    assert all_subsets([7]) == [[], [7]]
    assert all_subsets([-1, 0, 2]) == [[], [-1], [-1, 0], [-1, 0, 2], [-1, 2], [0], [0, 2], [2]]
    assert len(all_subsets(list(range(10)))) == 1024
    print("154 ok")
