import java.util.Queue
import kotlin.math.abs
fun main() {

    fun part1(input: List<String>): Int {
        var galaxies = mutableListOf<Pair<Int, Int>>()
        var rowExpansion = 0
        for ((i, line) in input.withIndex()) {
            if (line.all { it == '.' }) rowExpansion += 1
            else {
                galaxies.addAll(line.mapIndexed { index, c -> if (c == '#') index else -1 }.filter{ it > -1}.map { Pair(i+rowExpansion, it) }.toMutableList())
            }
            }
        var filledCols = galaxies.map { it.second }.toList()
        var unfilledCols = IntRange(0, input.first().length).filter { it !in filledCols }
        var galaxiesExp = galaxies.map { Pair(it.first, it.second + unfilledCols.filter { elem -> elem < it.second }.size) }

        var ans = 0
        for (i in galaxiesExp.indices) {
            for (j in i+1 until galaxiesExp.size) {
                ans += abs(galaxiesExp[i].first - galaxiesExp[j].first) + abs(galaxiesExp[i].second - galaxiesExp[j].second)
            }
        }

        return ans
    }


    fun part2(input: List<String>): Long {
        var galaxies = mutableListOf<Pair<Long, Long>>()
        var rowExpansion = 0.toLong()
        for ((i, line) in input.withIndex()) {
            if (line.all { it == '.' }) rowExpansion += (1000000-1)
            else {
                galaxies.addAll(line.mapIndexed { index, c -> if (c == '#') index else -1 }.filter{ it > -1}.map { Pair(i+rowExpansion, it.toLong()) }.toMutableList())
            }
        }

        var filledCols = galaxies.map { it.second }.toList()
        var unfilledCols = LongRange(0, input.first().length.toLong()).filter { it !in filledCols }
        var galaxiesExp = galaxies.map { Pair(it.first, it.second + unfilledCols.filter { elem -> elem < it.second }.size*(1000000-1)) }

        var ans = 0.toLong()
        for (i in galaxiesExp.indices) {
            for (j in i+1 until galaxiesExp.size) {
                ans += abs(galaxiesExp[i].first - galaxiesExp[j].first) + abs(galaxiesExp[i].second - galaxiesExp[j].second)
            }
        }

        return ans

    }




    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test")
//    check(part2(testInput) == 0)

    val input = readInput("Day11")
    // test if implementation meets criteria from the description, like:
    part1(input).println()
    part2(input).println()
}
