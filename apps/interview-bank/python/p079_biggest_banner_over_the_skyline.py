# 079 · Biggest Banner Over the Skyline


def biggest_banner(heights: list[int]) -> int:
    best = 0
    stack: list[int] = []  # indices of rising heights
    for i in range(len(heights) + 1):
        h = heights[i] if i < len(heights) else 0  # a zero at the end empties the stack
        while stack and heights[stack[-1]] >= h:
            top = stack.pop()
            left = stack[-1] if stack else -1
            best = max(best, heights[top] * (i - left - 1))
        stack.append(i)
    return best


if __name__ == "__main__":
    assert biggest_banner([3, 1, 4, 5, 4, 2]) == 12
    assert biggest_banner([2, 4, 2]) == 6
    assert biggest_banner([]) == 0
    assert biggest_banner([7]) == 7
    assert biggest_banner([1, 2, 3, 4, 5]) == 9
    assert biggest_banner([5, 4, 3, 2, 1]) == 9
    assert biggest_banner([2, 1, 2]) == 3
    assert biggest_banner([0, 0]) == 0
    assert biggest_banner([4, 0, 4]) == 4
    print("079 ok")
