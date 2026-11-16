# 075 · Simplify a File Path


def simplify_path(path: str) -> str:
    names: list[str] = []
    for part in path.split("/"):
        if part == "..":
            if names:
                names.pop()
        elif part and part != ".":
            names.append(part)
    return "/" + "/".join(names)


if __name__ == "__main__":
    assert simplify_path("/shelfwise//covers/./2024/../thumbs/") == "/shelfwise/covers/thumbs"
    assert simplify_path("/../a/.../b/..") == "/a/..."
    assert simplify_path("/") == "/"
    assert simplify_path("/../..") == "/"
    assert simplify_path("/a//b////c/d//././/..") == "/a/b/c"
    assert simplify_path("/.hidden/..x/.") == "/.hidden/..x"
    print("075 ok")
