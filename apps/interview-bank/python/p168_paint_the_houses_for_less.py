# 168 · Paint the Houses for Less


def min_paint_cost(costs: list[list[int]]) -> int:
    red = green = blue = 0  # cheapest totals so far, by the colour of the last house
    for r, g, b in costs:
        red, green, blue = r + min(green, blue), g + min(red, blue), b + min(red, green)
    return min(red, green, blue)


if __name__ == "__main__":
    assert min_paint_cost([[8, 3, 6], [4, 9, 2], [7, 5, 1]]) == 8
    assert min_paint_cost([[1, 2, 9], [1, 9, 9]]) == 3
    assert min_paint_cost([]) == 0
    assert min_paint_cost([[5, 1, 9]]) == 1
    assert min_paint_cost([[7, 6, 2], [3, 8, 9], [4, 1, 6], [9, 2, 3]]) == 9
    assert min_paint_cost([[4, 4, 4], [4, 4, 4]]) == 8
    print("168 ok")
