package day20

import java.io.File

fun main() {
    val input = File("src/day20/input.txt").readLines().map { it.toLong() }

    println(grove(mix(input.mapIndexed { index, delta -> index to delta }.toMutableList())))
    println(grove((1..10).fold(input.mapIndexed { index, delta -> index to delta * 811589153L }
        .toMutableList()) { positions, _ -> mix(positions) }))
}

fun mix(positions: MutableList<Pair<Int, Long>>): MutableList<Pair<Int, Long>> {
    positions.indices.forEach { index ->
        val from = positions.indexOfFirst { (initialIndex) -> initialIndex == index }
        val value = positions[from]
        val delta = value.second
        if (delta != 0L) {
            positions.removeAt(from)
            val to = (from + delta).mod(positions.size)
            positions.add(to, value)
        }
    }
    return positions
}

fun grove(positions: List<Pair<Int, Long>>): Long {
    val zeroPosition = positions.indexOfFirst { (_, delta) -> delta == 0L }
    return listOf(1000, 2000, 3000).sumOf { positions[(zeroPosition + it).mod(positions.size)].second }
}
