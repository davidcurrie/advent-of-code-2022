package day11

import java.io.File

fun main() {
    println(solve(readInput(), 20, 3))
    println(solve(readInput(), 10000, 1))
}

private fun readInput(): List<Monkey> {
    val regex = """Monkey \d*:
  Starting items: (.*)
  Operation: new = (.*)
  Test: divisible by (\d*)
    If true: throw to monkey (\d*)
    If false: throw to monkey (\d*)""".toRegex()

    return File("src/day11/input.txt").readText().split("\n\n").map {
        val values = regex.matchEntire(it)!!.groupValues
        Monkey(
            values[1].split(", ").map(String::toLong).toMutableList(),
            values[2].split(" "),
            values[3].toLong(),
            values[4].toInt(),
            values[5].toInt()
        )
    }.toList()
}

fun solve(monkeys: List<Monkey>, rounds: Int, relief: Int): Long {

    val common = monkeys.fold(1L) { acc, m -> acc * m.divisibleBy }

    for (round in 1 .. rounds) {
        for (i in monkeys.indices) {
            monkeys[i].process(relief, common).forEach { pair ->
                monkeys[pair.first].items.add(pair.second)
            }
        }
    }

    return monkeys.map { it.inspects }.sorted().reversed().let { (first, second) -> first * second }
}

class Monkey(var items: MutableList<Long>, private val test: List<String>, val divisibleBy: Long, private val ifTrue: Int, private val ifFalse: Int) {

    var inspects = 0L

    fun process(relief: Int, common: Long): List<Pair<Int, Long>> {
        val result = items.map { item ->
            val b = if (test[2] == "old") item else test[2].toLong()
            val testResult: Long = if (test[1] == "*") item * b else item + b
            val afterRelief = (testResult / relief) % common
            Pair(if (afterRelief % divisibleBy == 0L) ifTrue else ifFalse, afterRelief)
        }.toList()
        inspects += items.size
        items.clear()
        return result
    }

}
