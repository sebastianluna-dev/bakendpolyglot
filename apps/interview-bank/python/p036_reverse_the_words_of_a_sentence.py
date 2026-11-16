# 036 · Reverse the Words of a Sentence


def reverse_words(sentence: str) -> str:
    words = sentence.split()  # no argument: splits on runs of spaces and drops the empty pieces
    words.reverse()
    return " ".join(words)


if __name__ == "__main__":
    assert reverse_words("seat 14 is free") == "free is 14 seat"
    assert reverse_words("  return   the copy ") == "copy the return"
    assert reverse_words("hold") == "hold"
    assert reverse_words("") == ""
    assert reverse_words("   ") == ""
    assert reverse_words("a b c") == "c b a"
    print("036 ok")
