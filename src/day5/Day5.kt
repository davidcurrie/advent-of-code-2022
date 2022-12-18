package day5

import java.io.File

fun main() {
    val parts = File("src/day5/input.txt").readText(Charsets.UTF_8).split("\n\n")
    val lines = parts[0].split("\n")
    val length = (lines.last().length + 1) / 4
    val columns = List(length) { mutableListOf<Char>() }
    lines.dropLast(1).forEach { line ->
        for (i in 0 until length) {
            val char = line[(i * 4) + 1]
            if (char != ' ') {
                columns[i].add(char)
            }
        }
    }

    val pattern = "move (\\d*) from (\\d*) to (\\d*)".toRegex()

    val partOne = columns.map { column -> column.map { it }.toMutableList() }
    parts[1].split("\n").dropLast(1).forEach { line ->
        val numbers = pattern.find(line)!!.groupValues.drop(1).map(String::toInt)
        for (i in 0 until numbers[0]) {
            partOne[numbers[2]-1].add(0, partOne[numbers[1]-1].removeAt(0))
        }
    }
    println(partOne.map { column -> column.first() }.joinToString(""))

    val partTwo = columns.map { column -> column.map { it }.toMutableList() }
    parts[1].split("\n").dropLast(1).forEach { line ->
        val numbers = pattern.find(line)!!.groupValues.drop(1).map(String::toInt)
        for (i in 0 until numbers[0]) {
            partTwo[numbers[2]-1].add(0, partTwo[numbers[1]-1].removeAt(numbers[0] - i - 1))
        }
    }
    println(partTwo.map { column -> column.first() }.joinToString(""))
}