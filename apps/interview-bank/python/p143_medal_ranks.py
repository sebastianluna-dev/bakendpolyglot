# 143 · Medal Ranks
import heapq


def medal_ranks(scores: list[int]) -> list[str]:
    heap: list[tuple[int, int]] = []
    for i, score in enumerate(scores):
        heapq.heappush(heap, (-score, i))  # negated: highest score on top
    medals = ["Gold Medal", "Silver Medal", "Bronze Medal"]
    ranks = [""] * len(scores)
    place = 1
    while heap:
        _, i = heapq.heappop(heap)
        ranks[i] = medals[place - 1] if place <= 3 else str(place)
        place += 1
    return ranks


if __name__ == "__main__":
    assert medal_ranks([88, 95, 70, 91]) == ["Bronze Medal", "Gold Medal", "4", "Silver Medal"]
    assert medal_ranks([3, 9, 1, 12, 5, 7]) == ["5", "Silver Medal", "6", "Gold Medal", "4", "Bronze Medal"]
    assert medal_ranks([42]) == ["Gold Medal"]
    assert medal_ranks([0, 5]) == ["Silver Medal", "Gold Medal"]
    assert medal_ranks([]) == []
    assert medal_ranks([10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 110]) == [
        "11", "10", "9", "8", "7", "6", "5", "4", "Bronze Medal", "Silver Medal", "Gold Medal",
    ]
    print("143 ok")
