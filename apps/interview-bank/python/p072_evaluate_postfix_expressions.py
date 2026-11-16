# 072 · Evaluate Postfix Expressions


def evaluate_postfix(tokens: list[str]) -> int:
    stack: list[int] = []
    for token in tokens:
        if token in ("+", "-", "*", "/"):
            b = stack.pop()
            a = stack.pop()
            if token == "+":
                stack.append(a + b)
            elif token == "-":
                stack.append(a - b)
            elif token == "*":
                stack.append(a * b)
            else:
                q = abs(a) // abs(b)  # // floors, so divide the sizes and fix the sign
                stack.append(q if (a < 0) == (b < 0) else -q)
        else:
            stack.append(int(token))
    return stack[-1]


if __name__ == "__main__":
    assert evaluate_postfix(["4", "13", "5", "/", "+"]) == 6
    assert evaluate_postfix(["-7", "2", "/", "3", "*"]) == -9
    assert evaluate_postfix(["42"]) == 42
    assert evaluate_postfix(["8", "2", "/", "3", "-"]) == 1
    assert evaluate_postfix(["2", "-5", "/"]) == 0
    assert evaluate_postfix(["-9", "-2", "/"]) == 4
    assert evaluate_postfix(["5", "1", "2", "+", "4", "*", "+", "3", "-"]) == 14
    print("072 ok")
