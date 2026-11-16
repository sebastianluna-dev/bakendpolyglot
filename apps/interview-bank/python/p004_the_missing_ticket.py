# 004 · The Missing Ticket


def missing_ticket(scanned: list[int]) -> int:
    n = len(scanned) + 1
    total = n * (n + 1) // 2
    for t in scanned:
        total -= t
    return total


if __name__ == "__main__":
    many = [t for t in range(1, 70001) if t != 31415]

    assert missing_ticket([3, 1, 4, 5]) == 2
    assert missing_ticket([2]) == 1
    assert missing_ticket([1]) == 2
    assert missing_ticket([]) == 1
    assert missing_ticket(many) == 31415
    print("004 ok")
