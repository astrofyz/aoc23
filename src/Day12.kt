import java.util.Queue
import kotlin.math.abs
fun main() {


    fun part1(input: List<String>): Int {
        var ans = 0
        for (line in input) {
            var rules = line.split(' ')[0]
            var pattern = line.split(' ')[1].split(',').map { it.toInt() }.toList()
            var placeholders = rules.mapIndexed { index, c -> if (c == '?') index else -1}.filter { it > -1 }.toList()
            var numberToPlace = pattern.sum() - rules.count { it == '#' }


            fun makeCombinations(n: Int, left: Int, k: Int, tmp: MutableList<Int>): MutableList<Int> {
                if (k == 0) {
                    var validIndices = placeholders.filterIndexed { index, unit -> tmp.contains(index) }
                    var newString = rules.mapIndexed { index, c -> if (validIndices.contains(index)) '#' else c }.joinToString(separator = "")
                    var matches = Regex("#+").findAll(newString)
                    if (matches.map { it.value.length }.toList() == pattern) {
                        ans += 1}
                    return tmp
                }

                for (i in left until n) {
                    tmp.add(i)
                    makeCombinations(n, i+1, k-1, tmp)

                    tmp.removeLast()
                }
                return mutableListOf()
            }

            makeCombinations(placeholders.size, 0, numberToPlace, mutableListOf())

        }

        return ans
    }


    fun part2(input: List<String>): Int {
        var ans = 0
        for (line in input) {
            var rulesSmall = line.split(' ')[0]
            var patternSmall = line.split(' ')[1].split(',').map { it.toInt() }.toList()
            var rules = IntRange(0, 4).joinToString(separator = "?") { rulesSmall }
            var pattern = IntRange(0, 4).map { patternSmall }.toList().flatten()
            println(rules)
            println(pattern)
            var placeholders = rules.mapIndexed { index, c -> if (c == '?') index else -1}.filter { it > -1 }.toList()
            var numberToPlace = pattern.sum() - rules.count { it == '#' }


            fun makeCombinations(n: Int, left: Int, k: Int, tmp: MutableList<Int>): MutableList<Int> {
                if (k == 0) {
                    var validIndices = placeholders.filterIndexed { index, unit -> tmp.contains(index) }
                    var newString = rules.mapIndexed { index, c -> if (validIndices.contains(index)) '#' else c }.joinToString(separator = "")
                    var matches = Regex("#+").findAll(newString)
                    if (matches.map { it.value.length }.toList() == pattern) {
                        ans += 1}
                    return tmp
                }

                for (i in left until n) {
                    tmp.add(i)
                    makeCombinations(n, i+1, k-1, tmp)

                    tmp.removeLast()
                }
                return mutableListOf()
            }

            println("${placeholders.size}, $numberToPlace")
            makeCombinations(placeholders.size, 0, numberToPlace, mutableListOf())

        }

        return ans
    }




    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test")
//    check(part1(testInput) == 0)

    val input = readInput("Day12")
    // test if implementation meets criteria from the description, like:
    part1(input).println()
//    part2(input).println()
}
