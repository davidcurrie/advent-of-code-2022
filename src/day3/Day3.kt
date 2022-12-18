package day3

import java.io.File

fun main() {
    val lines = File("src/day3/input.txt").readLines()
    println(lines.sumOf { contents ->
        val middle = contents.length / 2
        val firstCompartmentContents = contents.substring(0, middle).toSet()
        val secondCompartmentContents = contents.substring(middle).toSet()
        priority((firstCompartmentContents intersect secondCompartmentContents).first())
    })
    println(lines.map(String::toSet).chunked(3).sumOf { bags ->
        priority(bags.reduce(Set<Char>::intersect).first())
    })
}

fun priority(c: Char) =
    (if (c in 'a'..'z') c - 'a' + 1 else c - 'A' + 27)
