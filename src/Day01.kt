fun main() {
    fun findNumbers(input: String): Int{
        var firstDigit = 0
        var lastDigit = 0
        for (it:Char in input)  {
            if (it.isDigit()) {
                firstDigit = it.digitToInt()
                break
            }
        }
        for (it:Char in input.reversed()) {
            if (it.isDigit()) {
                lastDigit = it.digitToInt()
                break
            }
        }
        return firstDigit*10 + lastDigit
    }


    fun findNumbersStrings(input: String): Int{
        println(input)
        var mapNums = mapOf(
            "one" to 1,
            "two" to 2,
            "three" to 3,
            "four" to 4,
            "five" to 5,
            "six" to 6,
            "seven" to 7,
            "eight" to 8,
            "nine" to 9)

        var firstDigit = -1
        var lastDigit = -1
        var lastDigitIndex = 1000
        var firstDigitIndex = 1000


        for ((i,it) in input.withIndex())  {
            if (it.isDigit()) {
                firstDigit = it.digitToInt()
                firstDigitIndex = i
                break
            }
        }

        for ((i,it) in input.reversed().withIndex()) {
            if (it.isDigit()) {
                lastDigit = it.digitToInt()
                lastDigitIndex = i
                break
            }
        }

        for (key in mapNums.keys) {
            if ((input.indexOf(key) > -1)&&(input.indexOf(key) < firstDigitIndex)) {
                firstDigitIndex = input.indexOf(key)
                firstDigit = mapNums[key]!!
            }
        }


        for (key in mapNums.keys) {
            if ((input.reversed().indexOf(key.reversed()) > -1)&&(input.reversed().indexOf(key.reversed()) < lastDigitIndex)) {
                lastDigitIndex = input.reversed().indexOf(key.reversed())
                lastDigit = mapNums[key]!!
            }
        }

        return firstDigit * 10 + lastDigit
    }


    fun part1(input: List<String>): Int {
        var arrayNum = input.map {findNumbers(it)}.reduce{sum, element -> sum + element}
        return arrayNum
    }

    fun part2(input: List<String>): Int {
        var arrayNum = input.map {findNumbersStrings(it)}.reduce{sum, element -> sum + element}
        return arrayNum
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
//    check(part1(testInput) == 142)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
