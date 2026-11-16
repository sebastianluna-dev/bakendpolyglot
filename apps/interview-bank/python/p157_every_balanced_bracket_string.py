# 157 · Every Balanced Bracket String


def balanced_brackets(n: int) -> list[str]:
    result: list[str] = []
    path: list[str] = []

    def walk(opened: int, closed: int) -> None:
        if closed == n:
            result.append("".join(path))
            return
        if opened < n:
            path.append("(")
            walk(opened + 1, closed)
            path.pop()
        if closed < opened:
            path.append(")")
            walk(opened, closed + 1)
            path.pop()

    walk(0, 0)
    return result


if __name__ == "__main__":
    assert balanced_brackets(3) == ["((()))", "(()())", "(())()", "()(())", "()()()"]
    assert balanced_brackets(1) == ["()"]
    assert balanced_brackets(0) == [""]
    assert balanced_brackets(2) == ["(())", "()()"]
    assert len(balanced_brackets(4)) == 14
    print("157 ok")
