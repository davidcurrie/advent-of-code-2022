package day10

import java.io.File

fun main() {
    val program = Day10Program(File("src/day10/input.txt").readLines()).execute()
    println(program.total)
    println(program.pixels())
}

class Day10Program(private val instructions: List<String>) {

    private var x = 1
    private var cycle = 1
    var total = 0
        private set
    private val pixels = StringBuilder()

    fun execute(): Day10Program {
        for (instruction in instructions) {
            if (instruction.startsWith("addx")) {
                cycle()
                cycle()
                x += instruction.substring(5).toInt()
            } else {
                cycle()
            }
        }
        return this
    }

    private fun cycle() {
        val pixel = (cycle - 1) % 40
        pixels.append(if (pixel in x - 1 .. x + 1) "#" else ".")
        if (pixel == 19) {
            total += cycle * x
        }
        if (pixel == 39) {
            pixels.appendLine()
        }
        cycle++
    }

    fun pixels(): String {
        return pixels.toString()
    }

}