# 068 · A Queue Built From Two Stacks


class TwoStackQueue:
    def __init__(self) -> None:
        self.inbox: list[int] = []  # newest at the end
        self.outbox: list[int] = []  # oldest at the end

    def enqueue(self, x: int) -> None:
        self.inbox.append(x)

    def dequeue(self) -> int:
        self._refill()
        return self.outbox.pop()

    def peek(self) -> int:
        self._refill()
        return self.outbox[-1]

    def is_empty(self) -> bool:
        return not self.inbox and not self.outbox

    # Only when outbox is empty, or newer items would jump ahead of older ones.
    def _refill(self) -> None:
        if self.outbox:
            return
        while self.inbox:
            self.outbox.append(self.inbox.pop())


if __name__ == "__main__":
    q = TwoStackQueue()
    assert q.is_empty() is True
    q.enqueue(1)
    q.enqueue(2)
    assert q.peek() == 1
    assert q.dequeue() == 1
    q.enqueue(3)
    assert q.dequeue() == 2
    assert q.peek() == 3
    assert q.is_empty() is False
    assert q.dequeue() == 3
    assert q.is_empty() is True
    q.enqueue(4)
    q.enqueue(5)
    assert q.dequeue() == 4
    q.enqueue(6)
    assert q.dequeue() == 5
    assert q.dequeue() == 6
    assert q.is_empty() is True
    print("068 ok")
