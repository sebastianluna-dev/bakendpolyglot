// 041 · Compare Version Strings
package p041

fun compareVersions(a: String, b: String): Int {
    var i = 0
    var j = 0
    while (i < a.length || j < b.length) {
        var x = 0
        while (i < a.length && a[i] != '.') {
            x = x * 10 + (a[i] - '0')
            i++
        }
        var y = 0
        while (j < b.length && b[j] != '.') {
            y = y * 10 + (b[j] - '0')
            j++
        }
        if (x != y) return if (x < y) -1 else 1
        i++ // step over the dot; past the end, the next part reads as 0
        j++
    }
    return 0
}

fun main() {
    check(compareVersions("1.2.10", "1.2.9") == 1)
    check(compareVersions("3.0.0", "3") == 0)
    check(compareVersions("1.01", "1.001") == 0)
    check(compareVersions("0.9", "1.0") == -1)
    check(compareVersions("7.5.2.4", "7.5.3") == -1)
    check(compareVersions("1.0.1", "1") == 1)
    check(compareVersions("10.4", "9.12") == 1)
    println("041 ok")
}
