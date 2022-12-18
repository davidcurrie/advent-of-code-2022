package day14

import java.io.File
import kotlin.math.max
import kotlin.math.min

fun main() {
    val rocks = File("src/day14/input.txt").readLines().map { line ->
        Rock(line.split(" -> ").map { coord -> coord.split(",").let { Pair(it[0].toInt(), it[1].toInt()) } })
    }

    println(solve(rocks, false))
    println(solve(rocks, true))
}

fun solve(rocks: List<Rock>, floor: Boolean): Int {

    val rocksBoundary = rocks.map { it.boundary }.reduce(Box::merge)

    val settledSand = mutableSetOf<Pair<Int, Int>>()
    val start = Pair(500, 0)
    outer@ while (true) {
        var sand = start
        while (true) {
            sand = listOf(Pair(0, 1), Pair(-1, 1), Pair(1, 1)).map { delta -> sand + delta }
                .firstOrNull { option ->
                    option !in settledSand
                            && (!rocksBoundary.contains(option) || rocks.none { rock -> rock.hit(option) })
                            && (!floor || option.second != rocksBoundary.bottom + 2)
                } ?: break
            if (!floor && sand.second > rocksBoundary.bottom) break@outer
        }
        settledSand.add(sand)
        if (sand == start) break
    }

    return settledSand.size
}

operator fun Pair<Int, Int>.plus(other: Pair<Int, Int>): Pair<Int, Int> {
    return Pair(first + other.first, second + other.second)
}

data class Box(val a: Pair<Int, Int>, val b: Pair<Int, Int>) {
    private val topLeft = Pair(min(a.first, b.first), min(a.second, b.second))
    private val bottomRight = Pair(max(a.first, b.first), max(a.second, b.second))
    val bottom = bottomRight.second

    fun contains(coord: Pair<Int, Int>): Boolean =
        coord.first >= topLeft.first && coord.first <= bottomRight.first &&
                coord.second >= topLeft.second && coord.second <= bottomRight.second

    fun merge(other: Box): Box =
        Box(
            Pair(min(topLeft.first, other.topLeft.first), min(topLeft.second, other.topLeft.second)),
            Pair(max(bottomRight.first, other.bottomRight.first), max(bottomRight.second, other.bottomRight.second))
        )
}

data class Rock(val coords: List<Pair<Int, Int>>) {
    private val boxes = coords.zipWithNext().map { pair -> Box(pair.first, pair.second) }
    val boundary = boxes.reduce(Box::merge)
    fun hit(coord: Pair<Int, Int>): Boolean {
        return boundary.contains(coord) && boxes.any { it.contains(coord) }
    }
}