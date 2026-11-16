# 076 · Colliding Parcels on a Conveyor


def surviving_parcels(parcels: list[int]) -> list[int]:
    track: list[int] = []
    for p in parcels:
        alive = True
        while alive and p < 0 and track and track[-1] > 0:
            if track[-1] < -p:
                track.pop()
            elif track[-1] == -p:
                track.pop()
                alive = False
            else:
                alive = False
        if alive:
            track.append(p)
    return track


if __name__ == "__main__":
    assert surviving_parcels([4, 7, -5]) == [4, 7]
    assert surviving_parcels([3, 6, -6, -8, 2]) == [-8, 2]
    assert surviving_parcels([]) == []
    assert surviving_parcels([-3, 4]) == [-3, 4]
    assert surviving_parcels([5, -5]) == []
    assert surviving_parcels([1, -2, -2, -2]) == [-2, -2, -2]
    assert surviving_parcels([10, 2, -5]) == [10]
    print("076 ok")
