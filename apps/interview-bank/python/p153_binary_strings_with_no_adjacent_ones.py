# 153 · Binary Strings With No Adjacent Ones


def no_adjacent_ones(n: int) -> list[str]:
    result: list[str] = []
    path: list[str] = []

    def walk() -> None:
        if len(path) == n:
            result.append("".join(path))
            return
        path.append("0")
        walk()
        path.pop()
        if not path or path[-1] == "0":
            path.append("1")
            walk()
            path.pop()

    walk()
    return result


if __name__ == "__main__":
    assert no_adjacent_ones(3) == ["000", "001", "010", "100", "101"]
    assert no_adjacent_ones(1) == ["0", "1"]
    assert no_adjacent_ones(0) == [""]
    assert no_adjacent_ones(2) == ["00", "01", "10"]
    assert no_adjacent_ones(4) == ["0000", "0001", "0010", "0100", "0101", "1000", "1001", "1010"]
    assert len(no_adjacent_ones(10)) == 144
    print("153 ok")
