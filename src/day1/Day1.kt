package day1

import java.io.File

fun main() {
    File("src/day1/input.txt").readText(Charsets.UTF_8)
        .split("\n\n")
        .map { elf -> elf.split("\n").filter { it.isNotBlank() }.sumOf { line -> line.toInt() } }
        .sorted()
        .let { calories ->
            println(calories.last())
            println(calories.takeLast(3).sum())
        }
}