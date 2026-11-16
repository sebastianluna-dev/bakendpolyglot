# 077 · A Fixed-Size Ring Buffer


class RingBuffer:
    def __init__(self, capacity: int) -> None:
        self.items = [0] * capacity
        self.head = 0  # index of the oldest value
        self.count = 0

    def enqueue(self, value: int) -> bool:
        if self.is_full():
            return False
        self.items[(self.head + self.count) % len(self.items)] = value
        self.count += 1
        return True

    def dequeue(self) -> int:
        if self.is_empty():
            return -1
        value = self.items[self.head]
        self.head = (self.head + 1) % len(self.items)
        self.count -= 1
        return value

    def front(self) -> int:
        return -1 if self.is_empty() else self.items[self.head]

    def rear(self) -> int:
        if self.is_empty():
            return -1
        return self.items[(self.head + self.count - 1) % len(self.items)]

    def is_empty(self) -> bool:
        return self.count == 0

    def is_full(self) -> bool:
        return self.count == len(self.items)


if __name__ == "__main__":
    b = RingBuffer(3)
    assert b.is_empty() is True
    assert b.enqueue(5) is True
    assert b.enqueue(8) is True
    assert b.enqueue(2) is True
    assert b.enqueue(9) is False
    assert b.is_full() is True
    assert b.rear() == 2
    assert b.dequeue() == 5
    assert b.enqueue(9) is True
    assert b.front() == 8
    assert b.rear() == 9
    assert b.dequeue() == 8
    assert b.dequeue() == 2
    assert b.dequeue() == 9
    assert b.dequeue() == -1
    assert b.is_empty() is True
    assert b.front() == -1
    assert b.rear() == -1

    one = RingBuffer(1)
    assert one.enqueue(0) is True
    assert one.is_full() is True
    assert one.enqueue(1) is False
    assert one.front() == 0
    assert one.rear() == 0
    assert one.dequeue() == 0
    assert one.is_empty() is True
    print("077 ok")
