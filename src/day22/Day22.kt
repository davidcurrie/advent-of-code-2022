package day22

import java.io.File
import kotlin.reflect.KFunction3

fun main() {
    val input = File("src/day22/input.txt").readText().split("\n\n")
    val tiles = mutableSetOf<Pair<Int, Int>>()
    val walls = mutableSetOf<Pair<Int, Int>>()
    input[0].split("\n").forEachIndexed { row, s ->
        s.toList().forEachIndexed { column, c ->
            when (c) {
                '.' -> tiles.add(Pair(column + 1, row + 1))
                '#' -> walls.add(Pair(column + 1, row + 1))
            }
        }
    }
    val directions = input[1].toList().fold(emptyList<Any>()) { acc, c ->
        when (c) {
            'L' -> acc + 'L'
            'R' -> acc + 'R'
            else -> if (acc.isNotEmpty() && acc.last() is Int) (acc.dropLast(1) + ((acc.last() as Int * 10) + c.toString()
                .toInt())) else (acc + c.toString().toInt())
        }
    }
    println(solve(tiles, walls, directions, ::flatWrap))
    println(solve(tiles, walls, directions, ::cubeWrap))
}

private fun solve(
    tiles: Set<Pair<Int, Int>>,
    walls: Set<Pair<Int, Int>>,
    directions: List<Any>,
    wrap: KFunction3<Set<Pair<Int, Int>>, Set<Pair<Int, Int>>, State, State>
): Int {
    var state = State(tiles.filter { it.second == 1 }.minByOrNull { it.first }!!, 0)
    val deltas = listOf(Pair(1, 0), Pair(0, 1), Pair(-1, 0), Pair(0, -1))
    directions.forEach { direction ->
        when (direction) {
            'L' -> state = State(state.coord, (state.facing - 1).mod(deltas.size))
            'R' -> state = State(state.coord, (state.facing + 1).mod(deltas.size))
            else -> for (i in 1..direction as Int) {
                var nextState = State(
                    Pair(
                        state.coord.first + deltas[state.facing].first,
                        state.coord.second + deltas[state.facing].second
                    ), state.facing
                )
                if (nextState.coord !in (walls + tiles)) {
                    nextState = wrap(tiles, walls, nextState)
                }
                if (nextState.coord in walls) break
                state = nextState
            }
        }
    }
    return state.password()
}

data class State(val coord: Pair<Int, Int>, val facing: Int) {
    fun password() = 4 * coord.first + 1000 * coord.second + facing
}

private fun flatWrap(tiles: Set<Pair<Int, Int>>, walls: Set<Pair<Int, Int>>, state: State): State {
    return State(when (state.facing) {
        0 -> (tiles + walls).filter { it.second == state.coord.second }.minByOrNull { it.first }!!
        1 -> (tiles + walls).filter { it.first == state.coord.first }.minByOrNull { it.second }!!
        2 -> (tiles + walls).filter { it.second == state.coord.second }.maxByOrNull { it.first }!!
        3 -> (tiles + walls).filter { it.first == state.coord.first }.maxByOrNull { it.second }!!
        else -> throw IllegalStateException()
    }, state.facing
    )
}

private fun cubeWrap(tiles: Set<Pair<Int, Int>>, walls: Set<Pair<Int, Int>>, state: State): State {
    return when (state.facing) {
        0 -> {
            when (state.coord.second) {
                in 1..50 -> {
                    State((tiles + walls).filter { it.second == 151 - state.coord.second }.maxByOrNull { it.first }!!, 2)
                }
                in 51..100 -> {
                    State((tiles + walls).filter { it.first == 50 + state.coord.second }.maxByOrNull { it.second }!!, 3)
                }
                in 101..150 -> {
                    State((tiles + walls).filter { it.second == 151 - state.coord.second }.maxByOrNull { it.first }!!, 2)
                }
                else -> {
                    State((tiles + walls).filter { it.first == state.coord.second - 100 }.maxByOrNull { it.second }!!, 3)
                }
            }
        }

        1 -> {
            when (state.coord.first) {
                in 1..50 -> {
                    State((tiles + walls).filter { it.first == 100 + state.coord.first }.minByOrNull { it.second }!!, 1)
                }
                in 51..100 -> {
                    State((tiles + walls).filter { it.second == 100 + state.coord.first }.maxByOrNull { it.first }!!, 2)
                }
                else -> {
                    State((tiles + walls).filter { it.second == state.coord.first - 50 }.maxByOrNull { it.first }!!, 2)
                }
            }
        }

        2 -> {
            when (state.coord.second) {
                in 1..50 -> {
                    State((tiles + walls).filter { it.second == 151 - state.coord.second }.minByOrNull { it.first }!!, 0)
                }
                in 51..100 -> {
                    State((tiles + walls).filter { it.first == state.coord.second - 50 }.minByOrNull { it.second }!!, 1)
                }
                in 101..150 -> {
                    State((tiles + walls).filter { it.second == 151 - state.coord.second }.minByOrNull { it.first }!!, 0)
                }
                else -> {
                    State((tiles + walls).filter { it.first == state.coord.second - 100 }.minByOrNull { it.second }!!, 1)
                }
            }
        }

        3 -> {
            when (state.coord.first) {
                in 1..50 -> {
                    State((tiles + walls).filter { it.second == 50 + state.coord.first }.minByOrNull { it.first }!!, 0)
                }
                in 51..100 -> {
                    State((tiles + walls).filter { it.second == 100 + state.coord.first }.minByOrNull { it.first }!!, 0)
                }
                else -> {
                    State((tiles + walls).filter { it.first == state.coord.first - 100 }.maxByOrNull { it.second }!!, 3)
                }
            }
        }

        else -> throw IllegalStateException()
    }
}