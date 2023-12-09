import kotlin.math.pow
fun main() {

    fun processValues(input: List<Int>): Pair<Int, Int> {
        var curLine = input.map{it}.toMutableList()
        var lastElems = mutableListOf<Int>()
        var firstElems = mutableListOf<Int>()

        while (!curLine.all { it == 0 }) {
            lastElems.add(curLine.last())
            firstElems.add(curLine.first())
            curLine = curLine.windowed(2).map { it.last() - it.first() }.toMutableList()
        }
        return Pair(lastElems.sum(), firstElems.mapIndexed { index, i -> if (index % 2 == 0) i else -i }.sum())
    }

    fun part1(input: List<String>): Int {
        return input.sumOf { it -> processValues(it.split(' ').map { it.toInt() }).first }
    }


    fun part2(input: List<String>): Int {
        return input.sumOf { it -> processValues(it.split(' ').map { it.toInt() }).second }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    check(part2(testInput) == 2)

    val input = readInput("Day09")
    // test if implementation meets criteria from the description, like:
    part1(input).println()
    part2(input).println()
}
