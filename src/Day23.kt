import java.io.File
import java.security.KeyStore.TrustedCertificateEntry
import java.util.ArrayDeque
import java.util.InputMismatchException
import kotlin.math.abs
//import kotlin.math.mod
fun main() {

    fun part1(input: List<String>) : Int {
        var pathsMap = mutableMapOf<Pair<Int, Int>, Char>()

        for ((i, line) in input.withIndex()) {
            line.mapIndexed {j, c -> pathsMap[Pair(i, j)] = c }
        }

        fun Pair<Int, Int>.neighbors(): List<Pair<Int, Int>> {
            when (pathsMap.getValue(this)) {
                '.' -> return listOf(Pair(-1, 0), Pair(0, 1), Pair(1, 0), Pair(0, -1)).map { Pair(this.first+it.first, this.second+it.second)}.toList()
                '>' -> return listOf(Pair(this.first, this.second + 1))
                '^' -> return listOf(Pair(this.first - 1, this.second))
                '<' -> return listOf(Pair(this.first, this.second - 1))
                'v' -> return listOf(Pair(this.first + 1, this.second))
                else -> return listOf<Pair<Int, Int>>()
            }
        }

        var end = Pair(input.size-1, input.first().length-2)

        var hikeLengths = mutableListOf<Int>(0)


        fun findHike(start: Pair<Int, Int>, visited: MutableSet<Pair<Int, Int>>, hikeLen: Int): MutableList<Int> {
            if (start == end) {
                hikeLengths.add(hikeLen)
                return hikeLengths
            }
            else {
                var nextSteps = start.neighbors().filter { (it !in visited)
                            &&(it.first in input.indices)
                            &&(it.second in 0 until input.first().length)
                            &&(pathsMap.getValue(it) != '#') }


                for (step in nextSteps) {
                    findHike(step, visited.union(setOf(start)).toMutableSet(), hikeLen+1)
                }
                return hikeLengths
            }
        }

        var lens = findHike(Pair(0, 1), mutableSetOf<Pair<Int, Int>>(), 0)

        println(lens.sortedByDescending { it })
        return  0
    }

    fun part2(input: List<String>): Int {
        var pathsMap = mutableMapOf<Pair<Int, Int>, Char>()

        for ((i, line) in input.withIndex()) {
            line.mapIndexed {j, c -> pathsMap[Pair(i, j)] = c }
        }

        var junctions = mutableMapOf<Pair<Int, Int>, MutableSet<Pair<Pair<Int, Int>, Int>>>()

        fun Pair<Int, Int>.neighbors(): List<Pair<Int, Int>> {
            when {
                (pathsMap.getValue(this)) != '#' -> return listOf(Pair(0, -1), Pair(-1, 0), Pair(0, 1), Pair(1, 0)).map { Pair(this.first+it.first, this.second+it.second)}.toList()
                else -> return listOf<Pair<Int, Int>>()
            }
        }

        var end = Pair(input.size-1, input.first().length-2)
        var start = Pair(0, 1)

        for (crd in pathsMap.keys) {
            if ((crd.neighbors().filter{ (it.first in input.indices)
                        &&(it.second in 0 until input.first().length)
                        &&(pathsMap.getValue(it) != '#') }.size > 2)||(crd == start)||(crd == end)) {
                var visited = mutableListOf(crd)
                var queueNodes = ArrayDeque<Pair<Int, Int>>()
                queueNodes.addAll(crd.neighbors().filter{ (it !in visited)
                        &&(it.first in input.indices)
                        &&(it.second in 0 until input.first().length)
                        &&(pathsMap.getValue(it) != '#') })
                var lenHike = 1

                while (queueNodes.isNotEmpty()) {
                    var curNod = queueNodes.removeLast()

                    var nextsteps = curNod.neighbors().filter{ (it !in visited)
                            &&(it.first in input.indices)
                            &&(it.second in 0 until input.first().length)
                            &&(pathsMap.getValue(it) != '#') }
                    visited.add(curNod)
                    if ((nextsteps.size > 1)||(curNod == end)) {

                        if (crd in junctions) {
                            junctions.getValue(crd).add(Pair(curNod, lenHike))
                        }
                        else junctions[crd] = mutableSetOf(Pair(curNod, lenHike))

                        if (curNod in junctions) {
                            junctions.getValue(curNod).add(Pair(crd, lenHike))
                        }
                        else junctions[curNod] = mutableSetOf(Pair(crd, lenHike))

                        lenHike = 1
                    }
                    else if (nextsteps.size == 1){
                        queueNodes.add(nextsteps.first())
                        lenHike += 1
                    }
                }
            }
        }

        println(junctions.map { it.value.size }.toList().joinToString(separator = ", "))
        println(junctions.keys)

        var hikeLengths = mutableListOf<Int>(0)


        fun findHike(start: Pair<Int, Int>, visited: MutableSet<Pair<Int, Int>>, hikeLen: Int): MutableList<Int> {
            if (start == end) {
                if (hikeLen > hikeLengths.max()) println(hikeLen)
                hikeLengths.add(hikeLen)
                return hikeLengths
            }
            else {
                var nextSteps = junctions.getValue(start).filter { it.first !in visited }.sortedByDescending { it.second }.map { it.first }
                var nextLens = junctions.getValue(start).filter { it.first !in visited }.sortedByDescending { it.second }.map {it.second}

                for ((step, lens) in nextSteps zip nextLens) {
                    findHike(step, visited.union(setOf(start)).toMutableSet(), hikeLen+lens)
                }

                return hikeLengths
            }
        }

        var lengths = findHike(Pair(0, 1), mutableSetOf<Pair<Int, Int>>(), 0)
        println(lengths.toSet().sortedByDescending { it })

        return  0
    }



    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day23_test")
//    part2(testInput).println()

    val input = readInput("Day23")
    // test if implementation meets criteria from the description, like:
//    part1(input).println()
    part2(input).println()
}
