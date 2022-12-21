package day21

import java.io.File
import kotlin.math.roundToLong

fun main() {
    val input = File("src/day21/input.txt").readLines()
        .map { it.split(": ") }
        .associate { it[0] to it[1] }
    println(part1(input, "root"))

    input["root"]!!.split(" ").let {
        val lhs = part2(input, it[0])
        val rhs = part2(input, it[2])
        println(((rhs.second - lhs.second) / (lhs.first - rhs.first)).roundToLong())
    }
}

fun part1(input: Map<String, String>, monkey: String): Long {
    val output = input[monkey]!!
    if (output.contains(" ")) {
        output.split(" ").let { parts ->
            val a = part1(input, parts[0])
            val b = part1(input, parts[2])
            return when (parts[1]) {
                "+" -> a + b
                "-" -> a - b
                "*" -> a * b
                "/" -> a / b
                else -> throw IllegalStateException("Unknown operation ${parts[1]}")
            }
        }
    }
    return output.toLong()
}

fun part2(input: Map<String, String>, monkey: String): Pair<Double, Double> {
    val result: Pair<Double, Double>
    if (monkey == "humn") {
        result = Pair(1.0, 0.0)
    } else {
        val output = input[monkey]!!
        if (output.contains(" ")) {
            output.split(" ").let { parts ->
                val a = part2(input, parts[0])
                val b = part2(input, parts[2])
                result = when (parts[1]) {
                    "+" -> Pair(a.first + b.first, a.second + b.second)
                    "-" -> Pair(a.first - b.first, a.second - b.second)
                    "*" -> if (a.first == 0.0) Pair(
                        a.second * b.first,
                        a.second * b.second
                    ) else if (b.first == 0.0) Pair(
                        b.second * a.first,
                        b.second * a.second
                    ) else throw IllegalStateException("Multiplication by humn")

                    "/" -> if (b.first == 0.0) Pair(
                        a.first / b.second,
                        a.second / b.second
                    ) else throw IllegalStateException("Division by humn")

                    else -> throw IllegalStateException("Unknown operation ${parts[1]}")
                }
            }
        } else {
            result = Pair(0.0, output.toDouble())
        }
    }
    return result
}