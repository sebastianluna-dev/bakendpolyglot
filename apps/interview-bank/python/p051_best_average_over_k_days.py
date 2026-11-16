# 051 · Best Average Over k Days


def best_average(balances: list[int], k: int) -> float:
    total = sum(balances[:k])
    best = total
    for i in range(k, len(balances)):
        total += balances[i] - balances[i - k]  # the day entering minus the day leaving
        best = max(best, total)
    return best / k


def close(got: float, want: float) -> bool:
    return abs(got - want) < 1e-9


if __name__ == "__main__":
    assert close(best_average([4, -2, 7, 5, -1, 6], 4), 4.25)
    assert close(best_average([-3, -1, -4], 2), -2.0)
    assert close(best_average([5], 1), 5.0)
    assert close(best_average([1, 2, 3, 4], 1), 4.0)
    assert close(best_average([-7, -7, -7], 3), -7.0)
    assert close(best_average([1, 1, 2], 3), 4 / 3)
    print("051 ok")
