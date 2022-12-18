package day13

import java.io.File

fun main() {
    val pairs = File("src/day13/input.txt").readText().split("\n\n")
        .map { it.split("\n") }
        .map { listOf(parse(it[0]), parse(it[1])) }

    println(pairs.mapIndexed { index, pair -> if (check(pair[0], pair[1])!!) index + 1 else 0 }.sumOf { it })

    val dividers = listOf(mutableListOf(mutableListOf(2)), mutableListOf(mutableListOf(6)))

    val packets = pairs.flatten().toMutableList()
    packets.addAll(dividers)
    packets.sortWith { left, right -> if (check(left, right)!!) -1 else 1 }

    println(packets.mapIndexed { i, p -> if (p in dividers) i + 1 else 1 }.fold(1, Int::times))
}


fun check(left: MutableList<*>, right: MutableList<*>): Boolean? {
    for (i in left.indices) {
        var l = left[i]
        if (i == right.size) return false
        var r = right[i]
        if (l is Int && r is Int) {
            if (l < r) {
                return true
            } else if (l > r) {
                return false
            }
        } else {
            if (l is Int) {
                l = mutableListOf(l)
            }
            if (r is Int) {
                r = mutableListOf(r)
            }
            if (l is MutableList<*> && r is MutableList<*>) {
                val check = check(l, r)
                if (check != null) return check
            }
        }
    }
    if (right.size > left.size) return true
    return null
}

fun parse(s: String): MutableList<*> {
    val stack = mutableListOf<MutableList<Any>>()
    var n: Int? = null
    var result: MutableList<Any>? = null
    for (c in s) {
        when (c) {
            '[' -> {
                val list = mutableListOf<Any>()
                if (stack.isEmpty()) {
                    result = list
                } else {
                    stack.last().add(list)
                }
                stack.add(list)
                n = null
            }
            ']' -> {
                if (n != null) {
                    stack.last().add(n)
                }
                n = null
                stack.removeLast()
            }
            ',' -> {
                if (n != null) {
                    stack.last().add(n)
                }
                n = null
            }
            else -> {
                n = if (n != null) (10 * n) else 0
                n += c.toString().toInt()
            }
        }
    }
    return result!!
}
