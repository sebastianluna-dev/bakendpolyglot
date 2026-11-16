# 012 · Shared Members of Two Lists


def shared_members(first: list[int], second: list[int]) -> list[int]:
    pending = set(second)
    shared: list[int] = []
    for member_id in first:
        if member_id in pending:
            pending.remove(member_id)  # removed, so a repeat in first is skipped
            shared.append(member_id)
    return shared


if __name__ == "__main__":
    assert shared_members([104, 211, 104, 350, 87], [87, 104, 999, 104]) == [104, 87]
    assert shared_members([1, 2], [3, 4]) == []
    assert shared_members([], [1]) == []
    assert shared_members([5, 5, 5], [5]) == [5]
    assert shared_members([3, 1, 2], [2, 3]) == [3, 2]
    print("012 ok")
