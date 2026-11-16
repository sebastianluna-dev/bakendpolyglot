# 194 · Parse a Query String


def hex_value(c: str) -> int:
    if "0" <= c <= "9":
        return ord(c) - ord("0")
    if "a" <= c <= "f":
        return ord(c) - ord("a") + 10
    if "A" <= c <= "F":
        return ord(c) - ord("A") + 10
    return -1


def decode(s: str) -> str:
    out = bytearray()
    i = 0
    while i < len(s):
        c = s[i]
        if c == "+":
            out.append(ord(" "))
        elif c == "%" and i + 2 < len(s) and hex_value(s[i + 1]) >= 0 and hex_value(s[i + 2]) >= 0:
            out.append(hex_value(s[i + 1]) * 16 + hex_value(s[i + 2]))
            i += 2
        else:
            out.append(ord(c))  # a malformed % stays as it is
        i += 1
    return out.decode("utf-8")  # %XX escapes are bytes; several can make one character


def parse_query(query: str) -> dict[str, list[str]]:
    params: dict[str, list[str]] = {}
    for piece in query.split("&"):
        raw_key, _, raw_value = piece.partition("=")  # split at the first = only
        key = decode(raw_key)
        if key == "":
            continue
        params.setdefault(key, []).append(decode(raw_value))
    return params


if __name__ == "__main__":
    assert parse_query("genre=sci-fi&sort=title&genre=history") == {
        "genre": ["sci-fi", "history"],
        "sort": ["title"],
    }
    assert parse_query("q=war+%26+peace&note=50%25&empty=&flag") == {
        "q": ["war & peace"],
        "note": ["50%"],
        "empty": [""],
        "flag": [""],
    }
    assert parse_query("") == {}
    assert parse_query("a=1&&b=2&") == {"a": ["1"], "b": ["2"]}
    assert parse_query("=5&k=v") == {"k": ["v"]}
    assert parse_query("expr=a=b") == {"expr": ["a=b"]}
    assert parse_query("name=Jos%C3%A9&price=%E2%82%AC5") == {"name": ["José"], "price": ["€5"]}
    assert parse_query("bad=%zz%4&tail=%") == {"bad": ["%zz%4"], "tail": ["%"]}
    assert parse_query("sort%5Bby%5D=title&path=%2fa%2F") == {"sort[by]": ["title"], "path": ["/a/"]}
    assert parse_query("plus=%2B+") == {"plus": ["+ "]}
    print("194 ok")
