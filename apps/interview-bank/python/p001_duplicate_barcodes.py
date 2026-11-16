# 001 · Duplicate Barcodes


def has_duplicate(barcodes: list[str]) -> bool:
    seen: set[str] = set()
    for code in barcodes:
        if code in seen:
            return True
        seen.add(code)
    return False


if __name__ == "__main__":
    assert has_duplicate(["SW-1042", "SW-0007", "SW-2210", "SW-0007"]) is True
    assert has_duplicate(["SW-1042", "SW-0007", "SW-2210"]) is False
    assert has_duplicate(["SW-0007", "sw-0007"]) is False
    assert has_duplicate(["SW-1042"]) is False
    assert has_duplicate([]) is False
    print("001 ok")
