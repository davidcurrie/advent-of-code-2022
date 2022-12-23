package day23

import java.io.File
import java.util.*
import kotlin.math.max
import kotlin.math.min

fun main() {

    var elves = File("src/day23/input.txt").readLines().mapIndexed { row, line ->
        line.toList().mapIndexed { column, char ->
            if (char == '#') Pair(column, row) else null
        }.filterNotNull()
    }.flatten()

    val directions = listOf(
        listOf(Pair(0, -1), Pair(-1, -1), Pair(1, -1)),
        listOf(Pair(0, 1), Pair(-1, 1), Pair(1, 1)),
        listOf(Pair(-1, 0), Pair(-1, -1), Pair(-1, 1)),
        listOf(Pair(1, 0), Pair(1, -1), Pair(1, 1))
    )

    val deltas = directions.flatten().toSet()

    var round = 1
    while(true) {
        val proposedMoves = elves.map { elf ->
            val neighbouringElves = deltas.map { delta -> elf + delta }.filter { location -> location in elves }
            if (neighbouringElves.isEmpty()) {
                elf
            } else {
                val direction = directions.indices.map { i -> directions[(i + round - 1).mod(directions.size)] }
                    .firstOrNull { direction -> direction.none { d -> (elf + d) in neighbouringElves } }
                elf + (direction?.first() ?: Pair(0, 0))
            }
        }
        val frequencies = proposedMoves.groupingBy { it }.eachCount()
        val newElves = elves.indices.map { i -> if (frequencies[proposedMoves[i]] == 1) proposedMoves[i] else elves[i] }
        if (elves == newElves) {
            println(round)
            break
        }
        elves = newElves
        if (round == 10) {
            val (topLeft, bottomRight) = rectangle(elves)
            val area = (1 + bottomRight.first - topLeft.first) * (1 + bottomRight.second - topLeft.second)
            val empty = area - elves.size
            println(empty)
        }
        round++
    }

}

fun printElves(elves: List<Pair<Int, Int>>) {
    val (topLeft, bottomRight) = rectangle(elves)
    for (row in topLeft.second .. bottomRight.second) {
        for (column in topLeft.first .. bottomRight.first) {
            print(if (Pair(column, row) in elves) '#' else '.')
        }
        println()
    }
    println()
}

private fun rectangle(elves: List<Pair<Int, Int>>): Pair<Pair<Int, Int>, Pair<Int, Int>> {
    val topLeft = elves.reduce { a, b -> Pair(min(a.first, b.first), min(a.second, b.second)) }
    val bottomRight = elves.reduce { a, b -> Pair(max(a.first, b.first), max(a.second, b.second)) }
    return Pair(topLeft, bottomRight)
}

operator fun Pair<Int, Int>.plus(other: Pair<Int, Int>): Pair<Int, Int> {
    return Pair(first + other.first, second + other.second)
}