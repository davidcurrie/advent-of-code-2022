package day17

import java.io.File

fun main() {
    val jetPattern = File("src/day17/input.txt").readLines().first()

    val rockShapes = listOf(
        listOf(Pair(0,0), Pair(1,0), Pair(2,0), Pair(3,0)),
        listOf(Pair(0,1), Pair(1,0), Pair(1,1), Pair(1,2), Pair(2,1)),
        listOf(Pair(0,0), Pair(1,0), Pair(2,0), Pair(2,1), Pair(2,2)),
        listOf(Pair(0,0), Pair(0,1), Pair(0,2), Pair(0,3)),
        listOf(Pair(0,0), Pair(0,1), Pair(1,0), Pair(1,1))
    )

    var jetPatternIndex = 0
    val stoppedRocks = mutableSetOf<Pair<Long, Long>>()

    val heights = mutableListOf<Long>()
    val cycleHeights = mutableListOf<Long>()
    val cycleRocks = mutableListOf<Long>()

    for (rock in 0 .. 1_000_000_000_000) {
        val rockShapeIndex = (rock % rockShapes.size).toInt()
        val rockShape = rockShapes[rockShapeIndex]
        var coord = Pair(2L, 3 + (stoppedRocks.maxOfOrNull { it.second + 1 } ?: 0))
        while (true) {
            val jet = jetPattern[jetPatternIndex]
            jetPatternIndex = (jetPatternIndex + 1) % jetPattern.length
            val acrossCoord = if (jet == '>') {
                Pair(coord.first + 1L, coord.second)
            } else {
                Pair(coord.first - 1L, coord.second)
            }
            if (allowed(rockCoords(acrossCoord, rockShape), stoppedRocks)) {
                coord = acrossCoord
            }
            val downCoord = Pair(coord.first, coord.second - 1)
            if (allowed(rockCoords(downCoord, rockShape), stoppedRocks)) {
                coord = downCoord
            } else {
                stoppedRocks.addAll(rockCoords(coord, rockShape))
                break
            }
        }
        val height = stoppedRocks.maxOf { it.second } + 1
        heights.add(height)
        if (rock == 2022L - 1) println(height)
        if (jetPatternIndex == 0) {
            cycleHeights.add(height)
            cycleRocks.add(rock)
        }
        if (cycleHeights.size > 3 && (cycleHeights[cycleHeights.size - 1] - cycleHeights[cycleHeights.size - 2]) ==
            (cycleHeights[cycleHeights.size - 2] - cycleHeights[cycleHeights.size - 3])) break
    }

    val cycleHeightDiff = cycleHeights[cycleHeights.size - 1] - cycleHeights[cycleHeights.size - 2]
    val cycleRockDiff = cycleRocks[cycleRocks.size - 1] - cycleRocks[cycleRocks.size - 2]
    val cycleStarts = cycleRocks[0] + 1

    val remainder = (1_000_000_000_000 - cycleStarts) % cycleRockDiff
    val remainderHeight = heights[(cycleStarts + remainder - 1).toInt()]

    println(remainderHeight + (((1_000_000_000_000 - cycleStarts) / cycleRockDiff) * cycleHeightDiff))
}

fun allowed(rockCoords: List<Pair<Long, Long>>, stoppedRocks: Set<Pair<Long, Long>>): Boolean {
    rockCoords.forEach {
        if (it.second < 0) return false
        if (it.first < 0) return false
        if (it.first > 6) return false
        if (it in stoppedRocks) return false
    }
    return true
}

fun rockCoords(coord: Pair<Long, Long>, rockShape: List<Pair<Int, Int>>): List<Pair<Long, Long>> =
    rockShape.map { Pair(coord.first + it.first, coord.second + it.second) }