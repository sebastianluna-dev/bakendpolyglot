# 122 · A Word Dictionary With Wildcards


class TrieNode:
    def __init__(self) -> None:
        self.children: list[TrieNode | None] = [None] * 26
        self.is_word = False


class WordDictionary:
    def __init__(self) -> None:
        self.root = TrieNode()

    def add_word(self, word: str) -> None:
        node = self.root
        for ch in word:
            i = ord(ch) - ord("a")
            if node.children[i] is None:
                node.children[i] = TrieNode()
            node = node.children[i]
        node.is_word = True

    def search(self, pattern: str) -> bool:
        return self._match(self.root, pattern, 0)

    def _match(self, node: TrieNode, pattern: str, i: int) -> bool:
        if i == len(pattern):
            return node.is_word
        if pattern[i] == ".":
            return any(child is not None and self._match(child, pattern, i + 1) for child in node.children)
        child = node.children[ord(pattern[i]) - ord("a")]
        return child is not None and self._match(child, pattern, i + 1)


if __name__ == "__main__":
    d = WordDictionary()
    d.add_word("loan")
    d.add_word("lean")
    assert d.search("l.an")
    assert not d.search("lo")
    assert d.search("l...")
    d.add_word("load")
    assert d.search("loan")
    assert d.search("lo.d")
    assert not d.search("le.d")
    assert not d.search("loans")
    assert d.search("....")
    assert not d.search("...")
    assert not d.search("x...")

    e = WordDictionary()
    assert not e.search(".")
    e.add_word("a")
    assert e.search(".")
    assert e.search("a")
    assert not e.search("b")
    e.add_word("a")
    assert e.search("a")
    print("122 ok")
