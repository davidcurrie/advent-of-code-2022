package day9

import java.io.File
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sign

fun main() {
    val input = File("src/day9/input.txt").readLines()
        .map { line -> line.split(" ") }
        .map { parts -> Pair(parts[0], parts[1].toInt()) }
        .toList()
    println(solve(input, 2))
    println(solve(input, 10))
}

fun solve(input: List<Pair<String, Int>>, knots: Int): Int {
    val rope = MutableList(knots) { Pair(0, 0) }
    val visited = mutableSetOf<Pair<Int, Int>>()
    visited.add(rope.last())
    val deltas = mapOf("U" to Pair(0, 1), "D" to Pair(0, -1), "L" to Pair(-1, 0), "R" to Pair(1, 0))
    input.forEach { pair ->
        for (i in 1..pair.second) {
            val delta = deltas[pair.first]!!
            rope[0] = Pair(rope[0].first + delta.first, rope[0].second + delta.second)
            for (j in 1 until knots) {
                if (abs(rope[j - 1].second - rope[j].second) == 2 || abs(rope[j - 1].first - rope[j].first) == 2) {
                    rope[j] =
                        Pair(
                            rope[j].first + sign(rope[j - 1].first - rope[j].first),
                            rope[j].second + sign(rope[j - 1].second - rope[j].second)
                        )
                }
            }
            visited.add(rope.last())
        }
    }
    return visited.size
}

fun sign(input: Int): Int {
    return sign(input.toDouble()).toInt()
}

fun outputRope(rope: MutableList<Pair<Int, Int>>) {
    for (j in max(0, rope.maxOf { it.second }) downTo min(0, rope.minOf { it.second })) {
        for (i in min(0, rope.minOf { it.first })..max(0, rope.maxOf { it.first })) {
            var output = "."
            if (i == 0 && j == 0) {
                output = "s"
            }
            for (k in rope.indices) {
                if (i == rope[k].first && j == rope[k].second) {
                    output = when (k) {
                        0 -> "H"
                        rope.size - 1 -> "T"
                        else -> k.toString()
                    }
                    break
                }
            }
            print(output)
        }
        println()
    }
    println()
}
