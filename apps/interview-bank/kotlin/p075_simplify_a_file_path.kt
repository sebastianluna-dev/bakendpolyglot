// 075 · Simplify a File Path
package p075

fun simplifyPath(path: String): String {
    val names = ArrayDeque<String>()
    for (part in path.split("/")) {
        if (part == "..") {
            if (names.isNotEmpty()) names.removeLast()
        } else if (part.isNotEmpty() && part != ".") {
            names.addLast(part)
        }
    }
    return "/" + names.joinToString("/")
}

fun main() {
    check(simplifyPath("/shelfwise//covers/./2024/../thumbs/") == "/shelfwise/covers/thumbs")
    check(simplifyPath("/../a/.../b/..") == "/a/...")
    check(simplifyPath("/") == "/")
    check(simplifyPath("/../..") == "/")
    check(simplifyPath("/a//b////c/d//././/..") == "/a/b/c")
    check(simplifyPath("/.hidden/..x/.") == "/.hidden/..x")
    println("075 ok")
}
