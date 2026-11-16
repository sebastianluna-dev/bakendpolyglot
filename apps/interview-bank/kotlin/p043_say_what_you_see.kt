// 043 · Say What You See
package p043

fun sayWhatYouSee(start: String, rounds: Int): String {
    var s = start
    repeat(rounds) {
        s = buildString {
            var i = 0
            while (i < s.length) {
                var j = i
                while (j < s.length && s[j] == s[i]) j++
                append(j - i).append(s[i])
                i = j
            }
        }
    }
    return s
}

fun main() {
    check(sayWhatYouSee("1", 4) == "111221")
    check(sayWhatYouSee("3322251", 1) == "23321511")
    check(sayWhatYouSee("1", 0) == "1")
    check(sayWhatYouSee("22", 5) == "22")
    check(sayWhatYouSee("1111111111", 1) == "101")
    check(sayWhatYouSee("1", 9) == "13211311123113112211")
    println("043 ok")
}
