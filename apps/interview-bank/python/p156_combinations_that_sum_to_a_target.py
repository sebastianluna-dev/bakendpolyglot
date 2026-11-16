# 156 · Combinations That Sum to a Target


def combinations_to_target(candidates: list[int], target: int) -> list[list[int]]:
    sizes = sorted(candidates)
    result: list[list[int]] = []
    path: list[int] = []

    def walk(start: int, remaining: int) -> None:
        if remaining == 0:
            result.append(path.copy())
            return
        for i in range(start, len(sizes)):
            if sizes[i] > remaining:
                break  # sorted: every later size is too big as well
            path.append(sizes[i])
            walk(i, remaining - sizes[i])  # i, not i + 1: the same size may be used again
            path.pop()

    walk(0, target)
    return result


if __name__ == "__main__":
    assert combinations_to_target([2, 3, 5], 8) == [[2, 2, 2, 2], [2, 3, 3], [3, 5]]
    assert combinations_to_target([7, 3, 2], 7) == [[2, 2, 3], [7]]
    assert combinations_to_target([4, 6], 5) == []
    assert combinations_to_target([1], 3) == [[1, 1, 1]]
    assert combinations_to_target([9], 9) == [[9]]
    assert combinations_to_target([5, 1], 6) == [[1, 1, 1, 1, 1, 1], [1, 5]]
    print("156 ok")
