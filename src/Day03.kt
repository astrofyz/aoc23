fun main() {

    fun parseField(input: List<String>): Pair<MutableMap<Pair<Int, Int>, Char>, MutableMap<Pair<Int, Pair<Int, Int>>, Int>> {
        var symbols = mutableMapOf<Pair<Int, Int>,Char>()
        var numbers = mutableMapOf<Pair<Int, Pair<Int, Int>>,Int>()
        for ((i, line) in input.withIndex()) {
            var st = -1
            var end = -1
            for ((j, elem) in line.withIndex()) {
                if (elem.isDigit()) {
                    if (st > -1) end = j
                    else st = j; end = j
                }
                else {
                    if (st > -1) {
                        numbers[Pair(i, Pair(st, end))] = line.slice(st..end).toInt()
                        st = -1
                        end = -1
                    }
                    if (elem != '.') {
                        symbols[Pair(i, j)] = elem
                    }
                }
            }
        }
        return Pair(symbols, numbers)
    }

    fun part1(input: List<String>): Int {
        var (symbols, numbers) = parseField(input)
        var ans = 0
        loop@for (num in numbers.keys) {
            for (y in num.first-1 .. num.first+1) {
                for (x in num.second.first-1 .. num.second.second+1) {
                    if (Pair(y, x) in symbols.keys) {ans += numbers[num]!!; continue@loop}
                }
            }
        }
        return ans
    }

    fun part2(input: List<String>): Int {
        var (symbols, numbers) = parseField(input)
        var ans = 0
        for (sym in symbols) {
            if (sym.value == '*') {
                var validNumbers = numbers.filterKeys { (it.first in sym.key.first-1 .. sym.key.first + 1)
                        &&(sym.key.second in it.second.first-1 .. it.second.second+1) }
                if (validNumbers.size == 2) ans += validNumbers.values.reduce{acc, elem -> acc*elem}
            }
        }
        return ans
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part2(testInput) == 467835)

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}
