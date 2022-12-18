package day18

import java.io.File
import kotlin.math.max
import kotlin.math.min

fun main() {
    val lavas = File("src/day18/input.txt").readLines().map { it.split(',') }.map { Triple(it[0].toInt(), it[1].toInt(), it[2].toInt()) }
    val deltas = listOf(
        Triple(1, 0, 0), Triple(-1, 0, 0),
        Triple(0, 1, 0), Triple(0, -1, 0),
        Triple(0, 0, 1), Triple(0, 0, -1)
    )
    println(lavas.sumOf { lava -> deltas.map { delta -> lava + delta }.count { other -> other !in lavas } })

    val min = lavas.reduce { t1, t2 -> Triple(min(t1.first, t2.first), min(t1.second, t2.second), min(t1.third, t2.third))}
    val max = lavas.reduce { t1, t2 -> Triple(max(t1.first, t2.first), max(t1.second, t2.second), max(t1.third, t2.third))}

    val pockets = mutableListOf<Triple<Int, Int, Int>>()
    val nonPockets = mutableListOf<Triple<Int, Int, Int>>()

    for (x in min.first .. max.first) {
        for (y in min.second .. max.second) {
            loop@ for (z in min.third .. max.third) {
                val start = Triple(x, y, z)
                if (start in lavas || start in pockets || start in nonPockets) continue
                val potentialPocket = mutableListOf(start)
                val stack = mutableListOf(start)
                while (stack.isNotEmpty()) {
                    val next = stack.removeAt(0)
                    val surrounding =
                        deltas.map { delta -> next + delta }.filter { it !in lavas }.filter { it !in potentialPocket }
                    val reachedEdge =
                        surrounding.any { it.first <= min.first || it.second <= min.second || it.third <= min.third
                                || it.first >= max.first || it.second >= max.second || it.third >= max.third }
                    if (reachedEdge) {
                        nonPockets.addAll(potentialPocket)
                        continue@loop
                    }
                    stack.addAll(surrounding)
                    potentialPocket.addAll(surrounding)
                }
                pockets.addAll(potentialPocket) // it is a pocket!
            }
        }
    }

    println(lavas.sumOf { lava -> deltas.map { delta -> lava + delta }.count { other -> other !in lavas && other !in pockets } })
}

private operator fun Triple<Int, Int, Int>.plus(other: Triple<Int, Int, Int>)
    = Triple(first + other.first, second + other.second, third + other.third)
