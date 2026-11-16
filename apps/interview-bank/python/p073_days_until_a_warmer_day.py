# 073 · Days Until a Warmer Day


def days_until_warmer(temps: list[int]) -> list[int]:
    wait = [0] * len(temps)
    stack: list[int] = []  # indices still waiting, their temperatures never rising from bottom to top
    for i, t in enumerate(temps):
        while stack and temps[stack[-1]] < t:
            j = stack.pop()
            wait[j] = i - j
        stack.append(i)
    return wait


if __name__ == "__main__":
    assert days_until_warmer([18, 16, 17, 21, 20, 20, 23]) == [3, 1, 1, 3, 2, 1, 0]
    assert days_until_warmer([30, 25, 20]) == [0, 0, 0]
    assert days_until_warmer([]) == []
    assert days_until_warmer([12]) == [0]
    assert days_until_warmer([5, 5, 5]) == [0, 0, 0]
    assert days_until_warmer([-3, -1, 0, 2]) == [1, 1, 1, 0]
    print("073 ok")
