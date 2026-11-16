# 009 · Rotate the Carousel


def rotate_carousel(slides: list[int], k: int) -> None:
    n = len(slides)
    if n == 0:
        return
    k %= n
    reverse(slides, 0, n - 1)
    reverse(slides, 0, k - 1)
    reverse(slides, k, n - 1)


def reverse(a: list[int], lo: int, hi: int) -> None:
    while lo < hi:
        a[lo], a[hi] = a[hi], a[lo]
        lo += 1
        hi -= 1


def rotated(slides: list[int], k: int) -> list[int]:
    rotate_carousel(slides, k)
    return slides


if __name__ == "__main__":
    assert rotated([1, 2, 3, 4, 5], 2) == [4, 5, 1, 2, 3]
    assert rotated([10, 20], 5) == [20, 10]
    assert rotated([1, 2, 3], 0) == [1, 2, 3]
    assert rotated([1, 2, 3], 3) == [1, 2, 3]
    assert rotated([7], 4) == [7]
    assert rotated([], 3) == []
    print("009 ok")
