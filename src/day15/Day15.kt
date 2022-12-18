package day15

import java.io.File
import kotlin.math.abs

fun main() {
    val regexp = Regex("Sensor at x=(-?\\d*), y=(-?\\d*): closest beacon is at x=(-?\\d*), y=(-?\\d*)")
    val sensors = File("src/day15/input.txt").readLines()
        .map { regexp.matchEntire(it)!!.groupValues }
        .map { values ->
            Sensor(Pair(values[1].toInt(), values[2].toInt()), Pair(values[3].toInt(), values[4].toInt()))
        }.toSet()

    val row = 2000000 // 10
    println(sensors.map { it.excludes(row) }.reduce().sumOf { it.last - it.first })

    val max = 4000000 // 20

    // Part 2 using the fact everything must be excluded by a single range in every other row
    for (row in 0 .. max) {
        val exclusions = sensors.map { sensor -> sensor.excludes(row) }.reduce()
        if (exclusions.size > 1) {
            val column = (0..max).firstOrNull { exclusions.none { exclusion -> exclusion.contains(it) } } ?: continue
            println(column * 4000000L + row)
            break
        }
    }

    // Part 2 using the fact that the beacon must be just outside a sensor's exclusion range
    for (row in 0 .. max) {
        val exclusions = sensors.map { sensor -> sensor.excludes(row) }
        val options = exclusions.map { range -> listOf(range.first - 1, range.last + 1) }.flatten().filter { it in 0..max }
        val column = options.firstOrNull { option -> exclusions.none { exclusion -> exclusion.contains(option) } }
        if (column != null) {
            println(column * 4000000L + row)
            break
        }
    }
}

fun List<IntRange>.reduce(): List<IntRange> {
    if (size < 2) return this
    for (i in 0 until size - 1) {
        for (j in i + 1 until size) {
            if (this[i].overlap(this[j]) || this[i].adjacent(this[j])) {
                val result = this.subList(0, i).toMutableList()
                result.addAll(this.subList(i + 1, j))
                result.addAll(this.subList(j + 1, size))
                result.addAll(this[i].merge(this[j]))
                return result.reduce()
            }
        }
    }
    return this
}

fun IntRange.overlap(other: IntRange): Boolean {
    return first <= other.last && last >= other.first
}

fun IntRange.adjacent(other: IntRange): Boolean {
    return first == other.last + 1 || last == other.first - 1
}

fun IntRange.merge(other: IntRange): List<IntRange> {
    return if (overlap(other) || adjacent(other)) listOf(IntRange(kotlin.math.min(first, other.first), kotlin.math.max(last, other.last))) else listOf(this, other)
}

data class Sensor(val sensor: Pair<Int, Int>, val beacon: Pair<Int, Int>) {
    private val distance = abs(sensor.first - beacon.first) + abs(sensor.second - beacon.second)
    fun excludes(row: Int): IntRange {
        val distanceFromRow = abs(sensor.second - row)
        val difference = distance - distanceFromRow
        if (difference < 0) return IntRange.EMPTY
        return (sensor.first - difference .. sensor.first + difference)
    }
}