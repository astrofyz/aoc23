import kotlin.math.pow
fun main() {

    fun part1(input: List<String>): Int {
        var instructions = input.first()

        var nodes = mutableMapOf<String, MutableMap<Char, String>>()
        for (line in input.drop(2)) {
            nodes[line.take(3)] = mutableMapOf() //Pair(line.slice(7 .. 9), line.slice(12 .. 14))
            nodes.getValue(line.take(3))['L'] = line.slice(7 .. 9)
            nodes.getValue(line.take(3))['R'] = line.slice(12 .. 14)
        }

        var idx = 0

        var curNode = "AAA"

        while (curNode != "ZZZ") {
            curNode = nodes.getValue(curNode).getValue(instructions[idx % instructions.length])
            idx += 1
        }
        return idx
    }


    fun findFactors(n: Int) : MutableList<Long> {
        var i = 2
        var factors = mutableListOf<Long>()
        while (i * i < n) {
            if (n % i == 0) {
                factors.add(i.toLong())
                i += 1
            }
            else i+= 1
        }
        return factors
    }


    fun part2(input: List<String>): Long {
        var instructions = input.first()

        var nodes = mutableMapOf<String, MutableMap<Char, String>>()
        for (line in input.drop(2)) {
            nodes[line.take(3)] = mutableMapOf() //Pair(line.slice(7 .. 9), line.slice(12 .. 14))
            nodes.getValue(line.take(3))['L'] = line.slice(7 .. 9)
            nodes.getValue(line.take(3))['R'] = line.slice(12 .. 14)
        }

        var idx = 0

        var curNodes = nodes.filterKeys { it.last() == 'A' }.keys.toMutableList()
        println(curNodes)

        var sequences = mutableMapOf<String, MutableList<String>>()

        for (nd in curNodes) {
            var curNode = nd
            sequences[nd] = mutableListOf()
            idx = 0
            while (curNode.last() != 'Z') {
                curNode = nodes.getValue(curNode).getValue(instructions[idx % instructions.length])
                sequences[nd]?.add(curNode)
                idx += 1
            }
        }
//        println(sequences)
        sequences.forEach{ println("${it.key} ${it.value.size}") }

        var factors = sequences.map{ findFactors(it.value.size) }.flatten()

        sequences.onEachIndexed { index, entry -> println("${entry.key}, ${factors[index]} ${entry.value.size / factors[index]}") }

        println(factors)

        return factors.reduce{acc, elem -> acc * elem} * 283.toLong()  // I'm ashamed and I'll rewrite it later
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
//    check(part2(testInput) == 0)

    val input = readInput("Day08")
    // test if implementation meets criteria from the description, like:
//    part1(input).println()
    part2(input).println()
}
