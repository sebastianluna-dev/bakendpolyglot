# 078 · Remove k Digits for the Smallest Number


def remove_k_digits(num: str, k: int) -> str:
    kept: list[str] = []
    for d in num:
        while k > 0 and kept and kept[-1] > d:
            kept.pop()
            k -= 1
        kept.append(d)
    del kept[len(kept) - k :]  # digits never fell, so drop the rest from the end
    return "".join(kept).lstrip("0") or "0"


if __name__ == "__main__":
    assert remove_k_digits("4172", 2) == "12"
    assert remove_k_digits("30200", 1) == "200"
    assert remove_k_digits("9", 1) == "0"
    assert remove_k_digits("12345", 2) == "123"
    assert remove_k_digits("10", 1) == "0"
    assert remove_k_digits("1432219", 0) == "1432219"
    assert remove_k_digits("112", 1) == "11"
    assert remove_k_digits("100200", 1) == "200"
    print("078 ok")
