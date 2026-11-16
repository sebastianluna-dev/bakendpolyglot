# 069 · Ledger With Undo and Double


def ledger_total(commands: list[str]) -> int:
    amounts: list[int] = []
    for cmd in commands:
        if cmd == "U":
            amounts.pop()
        elif cmd == "D":
            amounts.append(2 * amounts[-1])
        elif cmd == "+":
            amounts.append(amounts[-1] + amounts[-2])
        else:
            amounts.append(int(cmd))
    return sum(amounts)


if __name__ == "__main__":
    assert ledger_total(["5", "-2", "D", "+", "U", "10", "+"]) == 15
    assert ledger_total(["7", "U"]) == 0
    assert ledger_total([]) == 0
    assert ledger_total(["100"]) == 100
    assert ledger_total(["2", "3", "+"]) == 10
    assert ledger_total(["3", "D", "D", "U", "+"]) == 18
    assert ledger_total(["-4", "D", "U", "U"]) == 0
    print("069 ok")
