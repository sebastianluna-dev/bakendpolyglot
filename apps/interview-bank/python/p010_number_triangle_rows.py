# 010 · Number Triangle Rows


def triangle_rows(n: int) -> list[list[int]]:
    rows: list[list[int]] = []
    for r in range(n):
        row = [1] * (r + 1)
        for c in range(1, r):
            row[c] = rows[r - 1][c - 1] + rows[r - 1][c]
        rows.append(row)
    return rows


if __name__ == "__main__":
    assert triangle_rows(5) == [[1], [1, 1], [1, 2, 1], [1, 3, 3, 1], [1, 4, 6, 4, 1]]
    assert triangle_rows(2) == [[1], [1, 1]]
    assert triangle_rows(1) == [[1]]
    assert triangle_rows(0) == []
    assert triangle_rows(30)[29][14] == 77558760
    print("010 ok")
