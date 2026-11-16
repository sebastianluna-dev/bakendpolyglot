# 023 · A Set With Random Pick in O(1)
from collections.abc import Callable


class RandomSet:
    def __init__(self, random_index: Callable[[int], int]) -> None:
        self.random_index = random_index
        self.items: list[int] = []
        self.position: dict[int, int] = {}  # value -> its index in items

    def add(self, value: int) -> bool:
        if value in self.position:
            return False
        self.position[value] = len(self.items)
        self.items.append(value)
        return True

    def remove(self, value: int) -> bool:
        i = self.position.get(value)
        if i is None:
            return False
        last = self.items.pop()
        if last != value:
            self.items[i] = last
            self.position[last] = i
        del self.position[value]
        return True

    def pick(self) -> int:
        return self.items[self.random_index(len(self.items))]


def picks(s: RandomSet, count: int) -> list[int]:
    return sorted(s.pick() for _ in range(count))


if __name__ == "__main__":
    calls = 0

    def cycling(n: int) -> int:
        global calls
        calls += 1
        return (calls - 1) % n

    s = RandomSet(cycling)
    assert s.add(12) is True
    assert s.add(7) is True
    assert s.add(12) is False
    assert s.add(30) is True
    assert s.remove(7) is True
    assert s.remove(7) is False
    assert s.remove(99) is False
    assert picks(s, 2) == [12, 30]
    assert s.add(5) is True
    assert picks(s, 3) == [5, 12, 30]
    assert s.remove(30) is True
    assert s.remove(5) is True
    assert s.add(5) is True
    assert s.remove(12) is True
    assert s.pick() == 5
    print("023 ok")
