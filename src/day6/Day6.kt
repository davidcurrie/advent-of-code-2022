package day6

import java.io.File

fun main() {
    val input = File("src/day6/input.txt").readText(Charsets.UTF_8)
    println(solve(input, 4))
    println(solve(input, 14))
}

fun solve(input: String, length: Int): Int {
    for (i in 0 .. input.length - length) {
        if (input.substring(i, i + length).toSet().size == length) {
            return i + length
        }
    }
    return 0
}