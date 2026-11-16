# 040 · Pack and Unpack a List of Strings


def pack(words: list[str]) -> str:
    return "".join(f"{len(w)}#{w}" for w in words)


def unpack(packed: str) -> list[str]:
    words: list[str] = []
    i = 0
    while i < len(packed):
        length = 0
        while packed[i] != "#":
            length = length * 10 + (ord(packed[i]) - ord("0"))
            i += 1
        i += 1  # skip the "#"
        words.append(packed[i : i + length])
        i += length
    return words


if __name__ == "__main__":
    cases = [
        (["truck 7", "", "door#2"], "7#truck 70#6#door#2"),
        (["parcel-12345"], "12#parcel-12345"),
        ([], ""),
        ([""], "0#"),
        (["#", "12#", "a b", "3#x"], "1##3#12#3#a b3#3#x"),
    ]
    for words, packed in cases:
        assert pack(words) == packed
        assert unpack(packed) == words
    print("040 ok")
