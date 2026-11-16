# 049 · Push the Zeros to the End


def push_zeros_to_end(slots: list[int]) -> None:
    write = 0  # slots[:write] holds the parcels seen so far, in order
    for read in range(len(slots)):
        if slots[read] != 0:
            slots[write], slots[read] = slots[read], slots[write]
            write += 1


def pushed(slots: list[int]) -> list[int]:
    push_zeros_to_end(slots)
    return slots


if __name__ == "__main__":
    assert pushed([0, 41, 0, 0, 17, 23]) == [41, 17, 23, 0, 0, 0]
    assert pushed([5, 0, 3]) == [5, 3, 0]
    assert pushed([]) == []
    assert pushed([0, 0]) == [0, 0]
    assert pushed([1, 2]) == [1, 2]
    assert pushed([0, 0, 1]) == [1, 0, 0]
    print("049 ok")
