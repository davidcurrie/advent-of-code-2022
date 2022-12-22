package day19

import java.io.File
import java.lang.IllegalStateException
import java.util.PriorityQueue
import kotlin.math.ceil
import kotlin.math.max

fun main() {
    val regexp = Regex("\\d+")
    val blueprints = File("src/day19/input.txt").readLines()
        .map {
            regexp.findAll(it).drop(1).map { it.value.toInt() }.toList().let { matches ->
                listOf(
                    Quadruple(matches[0], 0, 0, 0),
                    Quadruple(matches[1], 0, 0, 0),
                    Quadruple(matches[2], matches[3], 0, 0),
                    Quadruple(matches[4], 0, matches[5], 0)
                )
            }
        }

    println(blueprints.map { maxGeodes(it, 24) }.mapIndexed { i, geodes -> (i + 1) * geodes }.sum())
    println(blueprints.take(3).map { maxGeodes(it, 32) }.reduce(Int::times))
}

fun maxGeodes(blueprint: List<Quadruple>, totalMinutes: Int): Int {
    var maxGeodes = 0
    val queue = PriorityQueue<State>().apply { add(State(1, Quadruple(1, 0, 0, 0), Quadruple(1, 0, 0, 0))) }
    while (queue.isNotEmpty()) {
        val state = queue.poll()
        maxGeodes = max(maxGeodes, state.materials.geode)
        if (state.minute == totalMinutes) continue
        val theoreticalMaxGeodes =
            state.materials.geode + (state.minute..totalMinutes).sumOf { it + state.robots.geode }
        if (theoreticalMaxGeodes > maxGeodes) {
            queue.addAll(state.next(blueprint, totalMinutes))
        }
    }
    return maxGeodes
}


data class Quadruple(val ore: Int, val clay: Int, val obsidian: Int, val geode: Int) {
    operator fun plus(other: Quadruple) =
        Quadruple(ore + other.ore, clay + other.clay, obsidian + other.obsidian, geode + other.geode)

    operator fun minus(other: Quadruple) =
        Quadruple(ore - other.ore, clay - other.clay, obsidian - other.obsidian, geode - other.geode)

    operator fun times(multiple: Int) =
        Quadruple(ore * multiple, clay * multiple, obsidian * multiple, geode * multiple)

    fun timeToBuild(required: Quadruple): Int = maxOf(
        if (required.ore <= 0) 0 else ceil(required.ore.toDouble() / ore).toInt(),
        if (required.clay <= 0) 0 else ceil(required.clay.toDouble() / clay).toInt(),
        if (required.obsidian <= 0) 0 else ceil(required.obsidian.toDouble() / obsidian).toInt(),
        if (required.geode <= 0) 0 else ceil(required.geode.toDouble() / geode).toInt()
    ) + 1

    companion object {
        fun of(index: Int) =
            when (index) {
                0 -> Quadruple(1, 0, 0, 0)
                1 -> Quadruple(0, 1, 0, 0)
                2 -> Quadruple(0, 0, 1, 0)
                3 -> Quadruple(0, 0, 0, 1)
                else -> throw IllegalStateException()
            }
    }
}

data class State(val minute: Int, val robots: Quadruple, val materials: Quadruple) : Comparable<State> {

    fun next(blueprint: List<Quadruple>, totalMinutes: Int): List<State> {
        val nextBuildStates = mutableListOf<State>()
        if (blueprint.maxOf { it.ore } > robots.ore && materials.ore > 0) {
            nextBuildStates += nextForRobot(blueprint, 0)
        }
        if (blueprint.maxOf { it.clay } > robots.clay && materials.ore > 0) {
            nextBuildStates += nextForRobot(blueprint, 1)
        }
        if (blueprint.maxOf { it.obsidian } > robots.obsidian && materials.ore > 0 && materials.clay > 0) {
            nextBuildStates += nextForRobot(blueprint, 2)
        }
        if (materials.ore > 0 && materials.obsidian > 0) {
            nextBuildStates += nextForRobot(blueprint, 3)
        }
        val nextBuildStatesWithinTime = nextBuildStates.filter { it.minute <= totalMinutes }.toMutableList()
        return nextBuildStatesWithinTime.ifEmpty { listOf(State(totalMinutes, robots, materials + (robots * (totalMinutes - minute)))) }
    }

    private fun nextForRobot(blueprint: List<Quadruple>, index: Int): State {
        return robots.timeToBuild(blueprint[index] - materials)
            .let { time ->
                State(
                    minute + time,
                    robots + Quadruple.of(index),
                    materials - blueprint[index] + (robots * time)
                )
            }

    }

    override fun compareTo(other: State) = other.materials.geode.compareTo(materials.geode)
}