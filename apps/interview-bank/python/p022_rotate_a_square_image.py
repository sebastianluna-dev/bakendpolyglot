# 022 · Rotate a Square Image


def rotate_image(image: list[list[int]]) -> None:
    n = len(image)
    for i in range(n):
        for j in range(i + 1, n):
            image[i][j], image[j][i] = image[j][i], image[i][j]
    for row in image:
        row.reverse()


def rotated(image: list[list[int]]) -> list[list[int]]:
    rotate_image(image)
    return image


if __name__ == "__main__":
    assert rotated([[1, 2], [3, 4]]) == [[3, 1], [4, 2]]
    assert rotated([[11, 12, 13], [21, 22, 23], [31, 32, 33]]) == [[31, 21, 11], [32, 22, 12], [33, 23, 13]]
    assert rotated([]) == []
    assert rotated([[7]]) == [[7]]
    assert rotated([[1, 2, 3, 4], [5, 6, 7, 8], [9, 10, 11, 12], [13, 14, 15, 16]]) == [
        [13, 9, 5, 1], [14, 10, 6, 2], [15, 11, 7, 3], [16, 12, 8, 4],
    ]
    print("022 ok")
