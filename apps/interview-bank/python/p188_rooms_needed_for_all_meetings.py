# 188 · Rooms Needed for All Meetings


def rooms_needed(meetings: list[list[int]]) -> int:
    starts = sorted(m[0] for m in meetings)
    ends = sorted(m[1] for m in meetings)
    rooms = j = 0  # ends[j] is the earliest end that has not freed a room yet
    for start in starts:
        if start >= ends[j]:
            j += 1  # a meeting is over: reuse its room
        else:
            rooms += 1
    return rooms


if __name__ == "__main__":
    assert rooms_needed([[9, 12], [10, 11], [11, 13], [13, 14]]) == 2
    assert rooms_needed([[8, 10], [10, 12], [12, 14]]) == 1
    assert rooms_needed([]) == 0
    assert rooms_needed([[1, 5], [2, 6], [3, 7]]) == 3
    assert rooms_needed([[2, 3], [2, 3]]) == 2
    assert rooms_needed([[5, 10], [1, 4], [4, 6]]) == 2
    print("188 ok")
