package day12

import java.io.File

fun main() {
    val grid = File("src/day12/input.txt").readLines().mapIndexed {
        y, line -> line.mapIndexed {
            x, c -> Pair(x, y) to c
        }
    }.flatten().toMap()

    val start = grid.filter { it.value == 'S' }.map { it.key }.first()
    val end = grid.filter { it.value == 'E' }.map { it.key }.first()
    val lowest = grid.filter { height(it.value) == 0 }.map { it.key }.toSet()

    println(solve(start, setOf(end), grid) { last, next -> next <= last + 1 })
    println(solve(end, lowest, grid) { last, next -> next >= last - 1 })
}

private fun solve(
    start: Pair<Int, Int>,
    end: Set<Pair<Int, Int>>,
    grid: Map<Pair<Int, Int>, Char>,
    validTransition: (Int, Int) -> Boolean
): Int {
    val directions = listOf(Pair(-1, 0), Pair(1, 0), Pair(0, -1), Pair(0, 1))

    val visited = mutableSetOf<Pair<Int, Int>>()
    val stack = mutableListOf(listOf(start))
    while (stack.isNotEmpty()) {
        val path = stack.removeAt(0)
        if (path.last() in end) {
            return (path.size - 1)
        }
        if (path.last() in visited) {
            continue
        }
        visited.add(path.last())
        val options = directions
            .asSequence()
            .map { direction -> Pair(path.last().first + direction.first, path.last().second + direction.second) }
            .filter { next -> next in grid.keys }
            .filter { next -> validTransition(height(grid[path.last()]!!), height(grid[next]!!)) }
            .map { next -> path + next }
            .toList()
        stack.addAll(options)
    }

    throw IllegalStateException("No solution")
}

fun height(c: Char): Int {
    return when (c) {
        'S' -> 0
        'E' -> 25
        else -> c - 'a'
    }
}
