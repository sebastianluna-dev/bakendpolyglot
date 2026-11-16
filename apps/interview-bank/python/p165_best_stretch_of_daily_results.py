# 165 · Best Stretch of Daily Results


def best_stretch(results: list[int]) -> int:
    ending_here, best = 0, results[0]  # ending_here: best stretch that ends on day r
    for r in results:
        ending_here = max(r, ending_here + r)
        best = max(best, ending_here)
    return best


if __name__ == "__main__":
    assert best_stretch([-2, 4, -1, 3, -7, 2]) == 6
    assert best_stretch([-8, -3, -5]) == -3
    assert best_stretch([5]) == 5
    assert best_stretch([1, 2, 3]) == 6
    assert best_stretch([-1, 0, -2]) == 0
    assert best_stretch([2, -1, 2, -9, 4]) == 4
    assert best_stretch([3, -2, 5, -1]) == 6
    print("165 ok")
