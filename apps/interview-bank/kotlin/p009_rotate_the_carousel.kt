// 009 · Rotate the Carousel
package p009

fun rotateCarousel(slides: IntArray, k: Int) {
    val n = slides.size
    if (n == 0) return
    val shift = k % n
    slides.reverse()
    slides.reverse(0, shift) // toIndex is exclusive
    slides.reverse(shift, n)
}

fun rotated(slides: IntArray, k: Int): IntArray {
    rotateCarousel(slides, k)
    return slides
}

fun main() {
    check(rotated(intArrayOf(1, 2, 3, 4, 5), 2).contentEquals(intArrayOf(4, 5, 1, 2, 3)))
    check(rotated(intArrayOf(10, 20), 5).contentEquals(intArrayOf(20, 10)))
    check(rotated(intArrayOf(1, 2, 3), 0).contentEquals(intArrayOf(1, 2, 3)))
    check(rotated(intArrayOf(1, 2, 3), 3).contentEquals(intArrayOf(1, 2, 3)))
    check(rotated(intArrayOf(7), 4).contentEquals(intArrayOf(7)))
    check(rotated(intArrayOf(), 3).isEmpty())
    println("009 ok")
}
