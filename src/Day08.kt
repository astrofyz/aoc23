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
                factors.add((n / i).toLong())
                i += 1
            }
            else i+= 1
        }
        if (factors.isEmpty()) factors.addAll(listOf(1.toLong(), n.toLong()))
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

        sequences.forEach{ println("${it.key} ${it.value.size}") }

        var factors = sequences.map{ findFactors(it.value.size) }.flatten().toSet()

        println(factors)

        return factors.reduce{acc, elem -> acc * elem}
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    check(part2(testInput) == 6.toLong())

    val input = readInput("Day08")
    // test if implementation meets criteria from the description, like:
//    part1(input).println()
    part2(input).println()
}
