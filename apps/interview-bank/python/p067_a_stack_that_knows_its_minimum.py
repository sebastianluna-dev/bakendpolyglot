# 067 · A Stack That Knows Its Minimum


class MinStack:
    def __init__(self) -> None:
        self.values: list[int] = []
        self.mins: list[int] = []  # mins[i] is the smallest of values[0..i]

    def push(self, x: int) -> None:
        self.values.append(x)
        self.mins.append(min(x, self.mins[-1]) if self.mins else x)

    def pop(self) -> int:
        self.mins.pop()
        return self.values.pop()

    def top(self) -> int:
        return self.values[-1]

    def min(self) -> int:
        return self.mins[-1]


if __name__ == "__main__":
    s = MinStack()
    s.push(5)
    s.push(3)
    s.push(7)
    assert s.min() == 3
    assert s.top() == 7
    assert s.pop() == 7
    assert s.min() == 3
    assert s.pop() == 3
    assert s.min() == 5
    assert s.pop() == 5

    s.push(4)
    s.push(1)
    s.push(1)
    assert s.pop() == 1
    assert s.min() == 1
    assert s.pop() == 1
    assert s.min() == 4
    s.push(-2)
    assert s.min() == -2
    assert s.top() == -2
    assert s.pop() == -2
    assert s.top() == 4
    assert s.min() == 4
    print("067 ok")
