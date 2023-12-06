import kotlin.math.pow
fun main() {

    fun parseNumbers(input: String):Pair<Set<Int>, Set<Int>> {
        var winNum = input.split(": ")[1].split(" | ")[0].split(' ')
            .filter{it.isNotBlank()}.map { it.toInt() }.toSet()
        var myNum = input.split(": ")[1].split(" | ")[1].split(' ')
            .filter{it.isNotBlank()}.map { it.toInt() }.toSet()
        return Pair(winNum, myNum)
    }

    fun part1(input: List<String>): Int {
        var ans = 0
        for (line in input) {
            var (winNum, myNum) = parseNumbers(line)

            if (myNum.intersect(winNum).isNotEmpty()) {
                ans += 2.0.pow(myNum.intersect(winNum).size - 1).toInt()
            }
        }
        return ans
    }

    fun part2(input: List<String>): Int {
        var numCards = mutableMapOf<Int, Int>().withDefault { 1 }
        var ans = 0
        for (line in input) {
            var cardId = line.split(": ")[0].split(' ').last().toInt()
            var (winNum, myNum) = parseNumbers(line)
            for (crd in cardId+1..myNum.intersect(winNum).size+cardId) {
                numCards[crd] = numCards.getValue(crd) + numCards.getValue(cardId)
            }
            ans += numCards.getValue(cardId)
        }
        return ans
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part2(testInput) == 30)

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}
