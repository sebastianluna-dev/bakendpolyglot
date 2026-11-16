# 137 · Change One Letter at a Time
from collections import deque
from string import ascii_lowercase


def shortest_chain(start: str, target: str, words: list[str]) -> int:
    dictionary = set(words)
    length = {start: 1}  # words in the shortest chain that ends here
    queue = deque([start])
    while queue:
        word = queue.popleft()
        if word == target:
            return length[word]
        for i in range(len(word)):
            for c in ascii_lowercase:
                nxt = word[:i] + c + word[i + 1 :]
                if nxt in dictionary and nxt not in length:
                    length[nxt] = length[word] + 1
                    queue.append(nxt)
    return 0


if __name__ == "__main__":
    assert shortest_chain("cold", "warm", ["cord", "card", "ward", "warm", "worm", "word", "wore"]) == 5
    assert shortest_chain("lead", "gold", ["load", "goad"]) == 0
    assert shortest_chain("cat", "cot", ["cot"]) == 2
    assert shortest_chain("cat", "dog", []) == 0
    assert shortest_chain("hit", "hot", ["hot", "hot", "hit"]) == 2
    assert shortest_chain("mine", "bond", ["mind", "bind", "bond", "mend"]) == 4
    assert shortest_chain("a", "c", ["b", "c"]) == 2
    print("137 ok")
