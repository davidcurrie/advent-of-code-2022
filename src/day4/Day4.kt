package day4

import java.io.File

fun main() {
    File("src/day4/input.txt").readLines()
        .map { line -> line.split(',').map(String::toRange) }
        .let { ranges ->
            println(ranges.count { pair ->
                pair[0].contains(pair[1]) || pair[1].contains(pair[0])
            })
            println(ranges.count { pair ->
                pair[0].overlaps(pair[1])
            })
        }


}

fun String.toRange(): Range {
    val parts = this.split('-')
    return Range(parts[0].toInt(), parts[1].toInt())
}

data class Range(val start: Int, val end: Int) {
    fun contains(other: Range): Boolean {
        return start <= other.start && end >= other.end
    }
    fun overlaps(other: Range): Boolean {
        return other.start in start..end || other.end in start .. end || other.contains(this)
    }
}