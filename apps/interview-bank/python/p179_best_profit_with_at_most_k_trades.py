# 179 · Best Profit With at Most k Trades


def best_profit(prices: list[int], k: int) -> int:
    if not prices or k == 0:
        return 0
    bought = [-prices[0]] * (k + 1)  # bought[t]: most cash holding the t-th share
    sold = [0] * (k + 1)  # sold[t]: most cash after t complete trades
    for p in prices:
        for t in range(1, k + 1):
            bought[t] = max(bought[t], sold[t - 1] - p)
            sold[t] = max(sold[t], bought[t] + p)
    return sold[k]


if __name__ == "__main__":
    prices = [3, 8, 5, 1, 7, 8]
    zigzag = [2, 5, 1, 4, 3, 6]
    assert best_profit(prices, 2) == 12
    assert best_profit(prices, 1) == 7
    assert best_profit([9, 7, 4, 1], 2) == 0
    assert best_profit(prices, 0) == 0
    assert best_profit([], 3) == 0
    assert best_profit([7], 1) == 0
    assert best_profit([1, 2, 3, 4, 5], 2) == 4
    assert best_profit(zigzag, 1) == 5
    assert best_profit(zigzag, 2) == 8
    assert best_profit(zigzag, 3) == 9
    assert best_profit(zigzag, 10) == 9
    assert best_profit([1, 3, 2, 8], 1) == 7
    print("179 ok")
