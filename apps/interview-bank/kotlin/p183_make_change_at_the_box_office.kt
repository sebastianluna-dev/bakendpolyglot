// 183 · Make Change at the Box Office
package p183

fun canMakeChange(bills: IntArray): Boolean {
    var fives = 0
    var tens = 0
    for (bill in bills) {
        if (bill == 5) {
            fives++
        } else if (bill == 10) {
            if (fives == 0) return false
            fives--
            tens++
        } else if (tens > 0 && fives > 0) {
            // a 10 can only ever change a 20, so spend it before the fives
            tens--
            fives--
        } else if (fives >= 3) {
            fives -= 3
        } else {
            return false
        }
    }
    return true
}

fun main() {
    check(canMakeChange(intArrayOf(5, 5, 10, 5, 20)))
    check(!canMakeChange(intArrayOf(5, 10, 10, 20)))
    check(canMakeChange(intArrayOf()))
    check(!canMakeChange(intArrayOf(10)))
    check(!canMakeChange(intArrayOf(5, 5, 20)))
    check(canMakeChange(intArrayOf(5, 5, 5, 20)))
    check(canMakeChange(intArrayOf(5, 5, 5, 5, 10, 20, 10)))
    println("183 ok")
}
