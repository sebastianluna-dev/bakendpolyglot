# 021 · Read a Matrix in a Spiral


def spiral_order(grid: list[list[int]]) -> list[int]:
    result: list[int] = []
    if not grid:
        return result
    top, bottom, left, right = 0, len(grid) - 1, 0, len(grid[0]) - 1
    while top <= bottom and left <= right:
        for c in range(left, right + 1):
            result.append(grid[top][c])
        top += 1
        for r in range(top, bottom + 1):
            result.append(grid[r][right])
        right -= 1
        # after the two steps above, a single row or column may be used up
        if top <= bottom:
            for c in range(right, left - 1, -1):
                result.append(grid[bottom][c])
            bottom -= 1
        if left <= right:
            for r in range(bottom, top - 1, -1):
                result.append(grid[r][left])
            left += 1
    return result


if __name__ == "__main__":
    assert spiral_order([[1, 2, 3], [8, 9, 4], [7, 6, 5]]) == [1, 2, 3, 4, 5, 6, 7, 8, 9]
    assert spiral_order([[10, 11, 12, 13], [20, 21, 22, 23], [30, 31, 32, 33]]) == [
        10, 11, 12, 13, 23, 33, 32, 31, 30, 20, 21, 22,
    ]
    assert spiral_order([]) == []
    assert spiral_order([[4, 5, 6]]) == [4, 5, 6]
    assert spiral_order([[4], [5], [6]]) == [4, 5, 6]
    assert spiral_order([[1, 2], [3, 4], [5, 6]]) == [1, 2, 4, 6, 5, 3]
    print("021 ok")
