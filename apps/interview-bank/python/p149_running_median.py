# 149 · Running Median
import heapq


class RunningMedian:
    def __init__(self) -> None:
        self.low: list[int] = []  # the smaller half, negated so that its largest value is on top
        self.high: list[int] = []  # the larger half, smallest on top

    def add_num(self, num: int) -> None:
        heapq.heappush(self.low, -num)
        heapq.heappush(self.high, -heapq.heappop(self.low))
        if len(self.high) > len(self.low):
            heapq.heappush(self.low, -heapq.heappop(self.high))

    def find_median(self) -> float:
        if len(self.low) > len(self.high):
            return float(-self.low[0])
        return (-self.low[0] + self.high[0]) / 2


if __name__ == "__main__":
    m = RunningMedian()
    m.add_num(5)
    assert m.find_median() == 5.0
    m.add_num(15)
    assert m.find_median() == 10.0
    m.add_num(1)
    assert m.find_median() == 5.0
    m.add_num(3)
    assert m.find_median() == 4.0

    neg = RunningMedian()
    neg.add_num(-2)
    assert neg.find_median() == -2.0
    neg.add_num(-2)
    assert neg.find_median() == -2.0
    neg.add_num(7)
    assert neg.find_median() == -2.0
    neg.add_num(8)
    assert neg.find_median() == 2.5

    down = RunningMedian()
    medians: list[float] = []
    for x in [9, 8, 7, 6, 5]:
        down.add_num(x)
        medians.append(down.find_median())
    assert medians == [9.0, 8.5, 8.0, 7.5, 7.0]

    big = RunningMedian()
    big.add_num(2_147_483_647)
    big.add_num(2_147_483_647)
    assert big.find_median() == 2_147_483_647.0
    big.add_num(-2_147_483_648)
    big.add_num(-2_147_483_648)
    assert big.find_median() == -0.5
    print("149 ok")
