# 147 · Single-Worker Job Order
import heapq


def job_order(jobs: list[list[int]]) -> list[int]:
    by_arrival = sorted(range(len(jobs)), key=lambda i: jobs[i][0])
    ready: list[tuple[int, int]] = []  # (duration, index): shortest first, then the smaller index
    order: list[int] = []
    time = 0
    nxt = 0  # position in by_arrival of the first job not yet queued
    while len(order) < len(jobs):
        if not ready and time < jobs[by_arrival[nxt]][0]:
            time = jobs[by_arrival[nxt]][0]  # idle
        while nxt < len(jobs) and jobs[by_arrival[nxt]][0] <= time:
            i = by_arrival[nxt]
            heapq.heappush(ready, (jobs[i][1], i))
            nxt += 1
        duration, j = heapq.heappop(ready)
        time += duration
        order.append(j)
    return order


if __name__ == "__main__":
    assert job_order([[1, 3], [2, 1], [3, 2], [4, 1]]) == [0, 1, 3, 2]
    assert job_order([[5, 2], [5, 2], [5, 1], [20, 1]]) == [2, 0, 1, 3]
    assert job_order([[7, 4]]) == [0]
    assert job_order([[1, 2], [3, 5], [3, 1]]) == [0, 2, 1]
    assert job_order([[9, 1], [1, 1]]) == [1, 0]
    assert job_order([[1, 1000000000], [1, 1000000000], [1000000000, 1]]) == [0, 2, 1]
    print("147 ok")
