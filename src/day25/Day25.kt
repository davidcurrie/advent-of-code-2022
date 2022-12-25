package day25

import java.io.File

fun main() {
    println(File("src/day25/input.txt").readLines().sumOf { it.fromSnafu() }.toSnafu())
}

private fun Long.toSnafu(): String {
    var result = ""
    var num = this
    while (num > 0) {
        val r = num % 5
        num = (num / 5)
        result = when (r) {
            3L -> '='.also { num++ }
            4L -> '-'.also { num++ }
            0L -> '0'
            1L -> '1'
            2L -> '2'
            else -> throw IllegalStateException()
        } + result
    }
    return result
}

fun String.fromSnafu() = fold(0L) { result, c ->
    5 * result + when (c) {
        '=' -> -2
        '-' -> -1
        '0' -> 0
        '1' -> 1
        '2' -> 2
        else -> throw IllegalStateException()
    }
}
