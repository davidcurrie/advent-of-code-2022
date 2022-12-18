package day16

import java.io.File
import kotlin.math.max

fun main() {
    val regexp = Regex("Valve (.*) has flow rate=(.*); tunnels? leads? to valves? (.*)")
    val valves = File("src/day16/input.txt").readLines()
        .map { regexp.matchEntire(it)!!.groupValues }.associate { values ->
            values[1] to Valve(values[1], values[2].toInt(), values[3].split(", "))
        }

    val distances = mutableMapOf<Pair<String, String>, Int>()
    valves.forEach { (from, valve) -> valves.keys.forEach { to -> distances[Pair(from, to)] = if (from == to) 0 else if (to in valve.leadsTo) 1 else 1000 } }
    for (k in valves.keys) {
        for (i in valves.keys) {
            for (j in valves.keys) {
                if (distances[Pair(i, j)]!! > (distances[Pair(i, k)]!! + distances[Pair(k, j)]!!)) {
                    distances[Pair(i, j)] = distances[Pair(i, k)]!! + distances[Pair(k, j)]!!
                }
            }
        }
    }

    println(solve(valves, distances, 30))

    val nonZeroValves = valves.values.filter { it.rate > 0 }.map { it.name }

    var maxRate = 0L
    for (i in 0..nonZeroValves.size / 2) {
        combinations(nonZeroValves, i).forEach { myValves ->
            val myRate = solve(valves.filter { (k, _) -> k == "AA" || k in myValves }, distances, 26)
            val elephantRate = solve(valves.filter { (k, _) -> k == "AA" || k !in myValves }, distances, 26)
            maxRate = max(maxRate, myRate + elephantRate)
        }
    }
    println(maxRate)
}

fun combinations(values: List<String>, size: Int): Set<Set<String>> {
    val result = mutableSetOf<Set<String>>()
    combinations(values, ArrayList(), result, size, 0)
    return result
}

fun combinations(values: List<String>, current: MutableList<String>, accumulator: MutableSet<Set<String>>, size: Int, pos: Int) {
    if (current.size == size) {
        accumulator.add(current.toSet())
        return
    }
    for (i in pos..values.size - size + current.size) {
        current.add(values[i])
        combinations(values, current, accumulator, size, i + 1)
        current.removeAt(current.size - 1)
    }
}

fun solve(valves: Map<String, Valve>, distances: Map<Pair<String, String>, Int>, maxTime: Int): Long {

    val stack = mutableListOf(Path(0, 0, listOf("AA")))
    val visited = mutableMapOf<Set<String>, Long>()
    var maxRate = 0L
    while (stack.isNotEmpty()) {
        val path = stack.removeAt(0)
        if (path.time > maxTime) continue
        if ((visited[path.turnedOn.toSet()] ?: 0) > path.totalRate) continue
        visited[path.turnedOn.toSet()] = path.totalRate
        val currentName = path.turnedOn.last()
        maxRate = max(maxRate, path.totalRate)
        valves.values.filter { it.rate != 0 }.filter { it.name !in path.turnedOn }.forEach { valve ->
            val distance = distances[Pair(currentName, valve.name)]!!
            val turnedOnAt = path.time + distance + 1
            stack.add(Path(turnedOnAt, path.totalRate + (valve.rate * (maxTime - turnedOnAt)), path.turnedOn + valve.name))
        }
    }

    return maxRate
}

data class Valve(val name: String, val rate: Int, val leadsTo: List<String>)

data class Path(val time: Int, val totalRate: Long, val turnedOn: List<String>)