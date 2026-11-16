# 005 · The Majority Genre


def majority_genre(loans: list[str]) -> str:
    counts: dict[str, int] = {}
    for genre in loans:
        counts[genre] = counts.get(genre, 0) + 1
        if counts[genre] * 2 > len(loans):
            return genre
    return ""


if __name__ == "__main__":
    assert majority_genre(["mystery", "poetry", "mystery", "history", "mystery"]) == "mystery"
    assert majority_genre(["poetry", "history", "poetry", "history"]) == ""
    assert majority_genre(["drama", "comedy", "drama"]) == "drama"
    assert majority_genre(["sci-fi"]) == "sci-fi"
    assert majority_genre([]) == ""
    print("005 ok")
