# 041 · Compare Version Strings


def compare_versions(a: str, b: str) -> int:
    i = j = 0
    while i < len(a) or j < len(b):
        x = 0
        while i < len(a) and a[i] != ".":
            x = x * 10 + int(a[i])
            i += 1
        y = 0
        while j < len(b) and b[j] != ".":
            y = y * 10 + int(b[j])
            j += 1
        if x != y:
            return -1 if x < y else 1
        i += 1  # step over the dot; past the end, the next part reads as 0
        j += 1
    return 0


if __name__ == "__main__":
    assert compare_versions("1.2.10", "1.2.9") == 1
    assert compare_versions("3.0.0", "3") == 0
    assert compare_versions("1.01", "1.001") == 0
    assert compare_versions("0.9", "1.0") == -1
    assert compare_versions("7.5.2.4", "7.5.3") == -1
    assert compare_versions("1.0.1", "1") == 1
    assert compare_versions("10.4", "9.12") == 1
    print("041 ok")
