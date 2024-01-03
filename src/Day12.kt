import java.util.Queue
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.pow
fun main() {


    fun part1(input: List<String>): Int {
        var ans = 0
        for (line in input) {
            var rules = line.split(' ')[0]
            var pattern = line.split(' ')[1].split(',').map { it.toInt() }.toList()
            println(rules)
//            println(pattern)
            println("-------------------")
            var placeholders = rules.mapIndexed { index, c -> if (c == '?') index else -1}.filter { it > -1 }.toList()
            var numberToPlace = pattern.sum() - rules.count { it == '#' }


            fun makeCombinations(n: Int, left: Int, k: Int, tmp: MutableList<Int>): MutableList<Int> {
                if (k == 0) {
                    var validIndices = placeholders.filterIndexed { index, unit -> tmp.contains(index) }
                    var newString = rules.mapIndexed { index, c -> if (validIndices.contains(index)) '#' else c }.joinToString(separator = "")
                    var matches = Regex("#+").findAll(newString)
                    if (matches.map { it.value.length }.toList() == pattern) {
//                        println(tmp)
//                        println(newString)
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
//            println(ans)

        }

        return ans
    }


    // cheated (watched discussion)
    fun part2(input: List<String>): Long {
        var ans = mutableListOf<Long>()

        for (line in input) {
            var rules = IntRange(1,5).map { line.split(' ')[0] }.joinToString  (separator = "?")
            var pattern = IntRange(1, 5).map { line.split(' ')[1] }.joinToString ( separator = ",").split(",").map { it.toInt() }.toList()
//            println(rules)
//            println(pattern)

            // максимальная длина блока, которую можно разместить до данной клетки
            var maxBlockLen = rules.map { 0 }.toMutableList()
            maxBlockLen.add(0)
            for (i in 0 until rules.length) {
                if (rules.toCharArray()[i] != '.') maxBlockLen[i+1] = maxBlockLen[i] + 1
            }

//            println(maxBlockLen)

            var counts = IntRange(0, rules.length).map { i -> IntRange(0, pattern.size).map { 0.toLong() }.toMutableList() }.toMutableList()

            counts[0][0] = 1.toLong()

            fun fitBlock(start: Int, length: Int): Boolean {
                if (start < 0) return false
                if (start + length > rules.length) return false
                return (maxBlockLen[start+length] >= length)&&(start == 0 || rules[start - 1] != '#')
            }

            for (i in 1 .. rules.length) {
                for (j in 0 .. pattern.size) {
                    var count = 0.toLong()
                    if (rules[i - 1] != '#') {
                        count += counts[i - 1][j]
                    }

                    if (j > 0) {
                        var lenBlock = pattern[j - 1]
//                        println("setting block ${j-1} with length $lenBlock")

                        if (fitBlock(i - lenBlock, lenBlock)) {
                            var gap = 0
                            if (i != lenBlock) gap = 1
//                            println("gap = $gap")
                            count += counts[i - lenBlock - gap][j - 1]
                        }
                    }
//                    println(count)
                    counts[i][j] = count
                }
            }
//            println(counts)
            ans.add(counts.last().last())

        }

        println(ans.sum()) //.chunked(5))
        return 0
    }




    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test")
//    check(part1(testInput) == 0)
    part2(testInput)

    val input = readInput("Day12")
    // test if implementation meets criteria from the description, like:
//    part1(input).println()
    part2(input).println()
}
