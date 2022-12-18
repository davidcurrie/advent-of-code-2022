package day2

import java.io.File

fun main() {
    File("src/day2/input.txt").readLines().let { lines ->
        println(lines.sumOf { line -> score1(line[0], line[2]) })
        println(lines.sumOf { line -> score2(line[0], line[2]) })
    }
}

fun score1(elf: Char, you: Char): Int =
    when (elf) {
        'A' -> when (you) {
            'X' -> 1 + 3
            'Y' -> 2 + 6
            'Z' -> 3 + 0
            else -> throw IllegalArgumentException()
        }

        'B' -> when (you) {
            'X' -> 1 + 0
            'Y' -> 2 + 3
            'Z' -> 3 + 6
            else -> throw IllegalArgumentException()
        }

        'C' -> when (you) {
            'X' -> 1 + 6
            'Y' -> 2 + 0
            'Z' -> 3 + 3
            else -> throw IllegalArgumentException()
        }

        else -> throw IllegalArgumentException()
    }

fun score2(elf: Char, outcome: Char): Int =
    when (elf) {
        'A' -> when (outcome) {
            'X' -> 3 + 0
            'Y' -> 1 + 3
            'Z' -> 2 + 6
            else -> throw IllegalArgumentException()
        }

        'B' -> when (outcome) {
            'X' -> 1 + 0
            'Y' -> 2 + 3
            'Z' -> 3 + 6
            else -> throw IllegalArgumentException()
        }

        'C' -> when (outcome) {
            'X' -> 2 + 0
            'Y' -> 3 + 3
            'Z' -> 1 + 6
            else -> throw IllegalArgumentException()
        }

        else -> throw IllegalArgumentException()
    }
