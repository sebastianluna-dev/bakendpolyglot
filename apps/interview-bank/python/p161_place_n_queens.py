# 161 · Place n Queens


def place_queens(n: int) -> list[list[str]]:
    result: list[list[str]] = []
    queens: list[int] = []  # queens[r] is the column of the queen in row r
    cols = [False] * n
    downs = [False] * (2 * n - 1)  # r + c is the same along a down-left diagonal
    ups = [False] * (2 * n - 1)  # r - c + n - 1 is the same along a down-right diagonal

    def walk(r: int) -> None:
        if r == n:
            result.append(["." * c + "Q" + "." * (n - c - 1) for c in queens])
            return
        for c in range(n):
            if cols[c] or downs[r + c] or ups[r - c + n - 1]:
                continue
            cols[c] = downs[r + c] = ups[r - c + n - 1] = True
            queens.append(c)
            walk(r + 1)
            queens.pop()
            cols[c] = downs[r + c] = ups[r - c + n - 1] = False

    walk(0)
    return result


if __name__ == "__main__":
    assert place_queens(4) == [[".Q..", "...Q", "Q...", "..Q."], ["..Q.", "Q...", "...Q", ".Q.."]]
    assert place_queens(1) == [["Q"]]
    assert place_queens(2) == []
    assert place_queens(3) == []
    assert len(place_queens(6)) == 4
    eight = place_queens(8)
    assert len(eight) == 92
    assert eight[0] == ["Q.......", "....Q...", ".......Q", ".....Q..", "..Q.....", "......Q.", ".Q......", "...Q...."]
    print("161 ok")
