# 002 · Two Prices for a Budget


def two_prices(prices: list[int], budget: int) -> list[int]:
    first_index: dict[int, int] = {}  # price -> earliest index
    for j, price in enumerate(prices):
        i = first_index.get(budget - price)
        if i is not None:
            return [i, j]
        first_index.setdefault(price, j)
    return []


if __name__ == "__main__":
    assert two_prices([12, 7, 3, 9, 5], 12) == [2, 3]
    assert two_prices([4, 4, 1], 8) == [0, 1]
    assert two_prices([1, 2], 10) == []
    print("002 ok")
