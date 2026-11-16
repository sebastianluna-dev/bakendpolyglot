# 015 · The k Most Borrowed Books


def most_borrowed(loans: list[str], k: int) -> list[str]:
    counts: dict[str, int] = {}
    for title in loans:
        counts[title] = counts.get(title, 0) + 1
    # Counter.most_common breaks ties by first appearance, not by title
    return sorted(counts, key=lambda title: (-counts[title], title))[:k]


if __name__ == "__main__":
    loans = ["dune", "emma", "dune", "ulysses", "emma", "dune", "beloved"]
    assert most_borrowed(loans, 2) == ["dune", "emma"]
    assert most_borrowed(["emma", "beloved", "ulysses", "beloved", "emma"], 2) == ["beloved", "emma"]
    assert most_borrowed(["dune"], 3) == ["dune"]
    assert most_borrowed([], 1) == []
    assert most_borrowed(["c", "b", "a"], 2) == ["a", "b"]
    print("015 ok")
