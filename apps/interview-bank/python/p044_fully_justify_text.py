# 044 · Fully Justify Text


def justify_text(words: list[str], width: int) -> list[str]:
    lines: list[str] = []
    i = 0
    while i < len(words):
        j = i + 1
        used = len(words[i])  # the words plus one space between each pair
        while j < len(words) and used + 1 + len(words[j]) <= width:
            used += 1 + len(words[j])
            j += 1
        gaps = j - i - 1
        if j == len(words) or gaps == 0:
            lines.append(" ".join(words[i:j]).ljust(width))
        else:
            spaces = width - (used - gaps)
            each, extra = divmod(spaces, gaps)
            parts: list[str] = []
            for k in range(i, j - 1):
                parts.append(words[k])
                parts.append(" " * (each + (1 if k - i < extra else 0)))
            parts.append(words[j - 1])
            lines.append("".join(parts))
        i = j
    return lines


if __name__ == "__main__":
    assert justify_text(["Your", "loan", "of", "Dune", "is", "due", "on", "Friday."], 16) == [
        "Your   loan   of",
        "Dune  is  due on",
        "Friday.         ",
    ]
    assert justify_text(["Hold", "expires", "in", "ten", "minutes"], 10) == [
        "Hold      ",
        "expires in",
        "ten       ",
        "minutes   ",
    ]
    assert justify_text(["to", "be", "or", "not"], 6) == ["to  be", "or not"]
    assert justify_text(["exact"], 5) == ["exact"]
    assert justify_text([], 8) == []
    print("044 ok")
