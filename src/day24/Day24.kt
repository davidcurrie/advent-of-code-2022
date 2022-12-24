package day24

import java.io.File
import java.util.*

fun main() {
    val valley = Valley.parse(File("src/day24/input.txt").readLines())
    val there = solve(valley, Valley.START, valley.finish)
    println(there.first)
    val back = solve(there.second, valley.finish, Valley.START)
    val thereAgain = solve(back.second, Valley.START, valley.finish)
    println(there.first + back.first + thereAgain.first)
}

fun solve(valley: Valley, from: Pair<Int, Int>, to: Pair<Int, Int>): Pair<Int, Valley> {
    val maps = mutableListOf(valley)
    val visited = mutableSetOf<Pair<Int, Pair<Int, Int>>>()
    val queue = PriorityQueue<Pair<Int, Pair<Int, Int>>>(
        compareBy {
            it.first + (to.first - it.second.first) + (to.second - it.second.second)
        }
    )
    queue.add(Pair(0, from))
    while (queue.isNotEmpty()) {
        val pair = queue.poll()
        if (pair in visited) continue
        visited.add(pair)
        val (minute, location) = pair
        if (location == to) {
            return Pair(minute, maps[minute])
        }
        val nextMinute = minute + 1
        if (maps.size == nextMinute) {
            maps.add(maps.last().next())
        }
        val map = maps[nextMinute]
        queue.addAll(map.options(location).map { Pair(minute + 1, it) })
    }

    throw IllegalStateException("End never reached")
}

data class Valley(val width: Int, val height: Int, val blizzardsAndWalls: Map<Pair<Int, Int>, List<Char>>) {
    val finish = Pair(width - 2, height - 1)
    companion object {
        val START = Pair(1, 0)
        fun parse(lines: List<String>): Valley {
            val blizzardWalls = lines.mapIndexed { row, line ->
                line.mapIndexed { column, char ->
                    Pair(column, row) to listOf(char)
                }.filter { '.' !in it.second }.toMap()
            }.reduce { a, b -> a + b }
            return Valley(lines.first().length, lines.size, blizzardWalls)
        }
    }

    fun print(location: Pair<Int, Int>): String {
        val buffer = StringBuffer()
        for (row in (0 until height)) {
            for (column in (0 until width)) {
                val c = Pair(column, row)
                if (c == location) {
                    buffer.append("E")
                } else {
                    val b = blizzardsAndWalls[c] ?: emptyList()
                    buffer.append(if (b.isEmpty()) '.' else if (b.size == 1) b.first() else b.size)
                }
            }
            buffer.appendLine()
        }
        return buffer.toString()
    }

    fun next(): Valley {
        return Valley(width, height, blizzardsAndWalls.map { (c, l) -> l.map { d -> Pair(c, d) } }.flatten().map {
                p -> Pair(when (p.second) {
            '^' -> Pair(p.first.first, if (p.first.second == 1) (height - 2) else (p.first.second - 1))
            '>' -> Pair(if (p.first.first == width - 2) 1 else (p.first.first + 1), p.first.second)
            'v' -> Pair(p.first.first, if (p.first.second == height - 2) 1 else (p.first.second + 1))
            '<' -> Pair(if (p.first.first == 1) (width - 2) else (p.first.first - 1), p.first.second)
            '#' -> p.first
            else -> throw IllegalStateException("Unknown direction ${p.second}")
        }, p.second)
        }.fold(mutableMapOf()) { m, p -> m[p.first] = (m[p.first] ?: emptyList()) + p.second; m })
    }

    fun options(location: Pair<Int, Int>): List<Pair<Int, Int>> {
        val deltas = listOf(Pair(0, 0), Pair(0, -1), Pair(1, 0), Pair(0, 1), Pair(-1, 0))
        return deltas.map { d -> Pair(location.first + d.first, location.second + d.second) }
            .filter { c -> (c.first > 0 && c.first < width - 1 && c.second > 0 && c.second < height - 1) || c == finish || c == START }
            .filterNot { c -> c in blizzardsAndWalls }
    }
}