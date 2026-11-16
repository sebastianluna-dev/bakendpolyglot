# 170 · Fewest Coins for an Amount


def fewest_coins(coins: list[int], amount: int) -> int:
    unreachable = amount + 1  # more coins than any real answer needs
    fewest = [0] + [unreachable] * amount
    for a in range(1, amount + 1):
        for coin in coins:
            if coin <= a:
                fewest[a] = min(fewest[a], fewest[a - coin] + 1)
    return -1 if fewest[amount] == unreachable else fewest[amount]


if __name__ == "__main__":
    assert fewest_coins([1, 4, 6], 8) == 2
    assert fewest_coins([5, 10], 3) == -1
    assert fewest_coins([7], 0) == 0
    assert fewest_coins([2], 3) == -1
    assert fewest_coins([3, 7], 20) == 4
    assert fewest_coins([2, 5, 10, 25], 99) == 7
    assert fewest_coins([4, 7], 5000) == 716
    print("170 ok")
