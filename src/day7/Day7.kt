package day7

import java.io.File

fun main() {
    val lines = File("src/day7/input.txt").readLines()
    val directories = mutableSetOf<String>()
    val files = mutableMapOf<String, Int>()
    val cwd = mutableListOf<String>()
    for (line in lines) {
        if (line.startsWith("$ cd ..")) {
            cwd.removeLast()
        } else if (line.startsWith("$ cd")) {
            cwd.add(line.substring(5))
        } else if (line.startsWith("$ ls")) {
            directories.add(cwd.joinToString("/"))
        } else if (line.startsWith("dir")) {
            // do nothing
        } else {
            val parts = line.split(" ")
            files[cwd.joinToString("/") + "/" + parts[1]] = parts[0].toInt()
        }
    }

    val directoriesWithSize = directories.associateWith { dir ->
        files.filterKeys { key -> key.startsWith(dir) }.values.sum()
    }

    println(directoriesWithSize.filterValues { value -> value <= 100000 }.values.sum())

    val requiredSpace = directoriesWithSize["/"]!! - (70000000 - 30000000)
    println(directoriesWithSize.values.sorted().first { value -> value >= requiredSpace })
}