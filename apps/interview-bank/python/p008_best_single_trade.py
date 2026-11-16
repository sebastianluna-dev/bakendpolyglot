# 008 · Best Single Trade
import math


def best_trade(prices: list[int]) -> int:
    lowest = math.inf
    best = 0
    for price in prices:
        lowest = min(lowest, price)
        best = max(best, price - lowest)
    return int(best)


if __name__ == "__main__":
    assert best_trade([8, 3, 6, 1, 7, 4]) == 6
    assert best_trade([9, 7, 4, 2]) == 0
    assert best_trade([5, 9, 1]) == 4
    assert best_trade([3]) == 0
    assert best_trade([]) == 0
    print("008 ok")
