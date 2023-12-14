import java.util.Queue
import kotlin.math.abs
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
                        println(tmp)
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


    fun part2(input: List<String>): Long {
        var ans = mutableListOf<Long>()
        for (line in input.take(2)) {
            ans.add(part1(listOf(line)).toLong())
            ans.add(part1(listOf("?$line")).toLong())
            ans.add(part1(listOf("${line.split(' ')[0]}? ${line.split(' ')[1]}")).toLong())
            ans.add(part1(listOf("${line.split(' ')[0]}?${line.split(' ')[0]} ${line.split(' ')[1]},${line.split(' ')[1]}")).toLong())
        }
        println(ans)

//        println(ans.chunked(3))
//        println(ans.chunked(3).map { it[0]*maxOf(it[1], it[2])*maxOf(it[1], it[2])*maxOf(it[1], it[2])*maxOf(it[1], it[2]) }.sum())

        return 0
    }




    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test")
//    check(part1(testInput) == 0)
//    part2(testInput)

    val input = readInput("Day12")
    // test if implementation meets criteria from the description, like:
//    part1(input).println()
    part2(input).println()
}
