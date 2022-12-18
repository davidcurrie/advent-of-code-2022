package day8

import java.io.File

fun main() {
    val lines = File("src/day8/input.txt").readLines()
    val trees = mutableMapOf<Pair<Int, Int>, Int>()
    for (j in lines.indices) {
        for (i in 0 until lines[j].length) {
            trees[Pair(i, j)] = lines[j][i].toString().toInt()
        }
    }

    val visible = mutableSetOf<Pair<Int, Int>>()
    for (j in lines.indices) {
        visible.addAll(visible(trees, Pair(0, j), Pair(1, 0)))
        visible.addAll(visible(trees, Pair(lines[j].length - 1, j), Pair(-1, 0)))
    }
    for (i in 0 until lines[0].length) {
        visible.addAll(visible(trees, Pair(i, 0), Pair(0, 1)))
        visible.addAll(visible(trees, Pair(i, lines.size - 1), Pair(0, -1)))
    }

    println(visible.size)

    println(trees.keys.maxOfOrNull { scenicScore(trees, it) })
}

fun scenicScore(trees: Map<Pair<Int, Int>, Int>, tree: Pair<Int, Int>): Int {
    val deltas = listOf(Pair(0, 1), Pair(1, 0), Pair(-1, 0), Pair(0, -1))
    var total = 1
    for (delta in deltas) {
        var coord = tree
        var score = 0
        while (true) {
            coord = Pair(coord.first + delta.first, coord.second + delta.second)
            if (coord in trees.keys) {
                if (trees[coord]!! < trees[tree]!!) {
                    score++
                } else {
                    score++
                    break
                }
            } else {
                break
            }
        }
        total *= score
    }
    return total
}

fun visible(trees: Map<Pair<Int, Int>, Int>, start: Pair<Int, Int>, delta: Pair<Int, Int>) : Set<Pair<Int, Int>> {
    val visible = mutableSetOf<Pair<Int, Int>>()
    visible.add(start)
    var height = trees[start]!!
    var coord = start
    while (true) {
        coord = Pair(coord.first + delta.first, coord.second + delta.second)
        if (coord !in trees.keys) {
            break
        }
        if (trees[coord]!! > height) {
            height = trees[coord]!!
            visible.add(coord)
        }
    }
    return visible
}