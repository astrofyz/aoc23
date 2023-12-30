import java.io.File
import java.util.ArrayDeque
import java.util.InputMismatchException
import kotlin.math.abs
//import kotlin.math.mod
fun main() {

    fun part1(input: List<String>, steps: Int = 64) : Int {
        var gardenPlots = mutableSetOf<Pair<Int, Int>>()

        var start = Pair(0, 0)

        input.mapIndexed { i, s -> s.mapIndexed { j, c -> when  (c)  {
            '.' -> gardenPlots.add(Pair(i, j))
            'S' -> {start = Pair(i, j); gardenPlots.add(Pair(i, j))}
            else -> null
        }}}

        var currentSteps = mutableSetOf<Pair<Int, Int>>()
        var newSteps = mutableSetOf<Pair<Int, Int>>()

        currentSteps.add(start)

        for (i in 1 .. steps) {
            for (pos in currentSteps){
                for (dir in listOf(Pair(1, 0), Pair(-1, 0), Pair(0, 1), Pair(0, -1))) {
                    if (Pair(pos.first + dir.first, pos.second + dir.second) in gardenPlots) newSteps.add(Pair(pos.first + dir.first, pos.second + dir.second))
                }
            }
//            println(newSteps)
            currentSteps = newSteps.map { it.copy() }.toMutableSet()
            newSteps = mutableSetOf<Pair<Int, Int>>()
        }
        return  currentSteps.size
    }


    fun part2(input: List<String>): Int {

        // 26501365 = 11 * 5 * 481843

        var gardenPlots = mutableSetOf<Pair<Int, Int>>()

        var start = Pair(0, 0)

        input.mapIndexed { i, s -> s.mapIndexed { j, c -> when  (c)  {
            '.' -> gardenPlots.add(Pair(i, j))
            'S' -> {start = Pair(i, j); gardenPlots.add(Pair(i, j))}
            else -> null
        }}}

        gardenPlots = gardenPlots.map { Pair(it.first - start.first, it.second - start.second) }.toMutableSet()
        start = Pair(0, 0)

//        File("largeInput21.txt").printWriter().use { out ->
//            for (i in 1 .. 15) {
//                var newRow = IntRange(1, 15).map { input[i] }.joinToString ( separator="" )
//                out.println(newRow)
//            }
//        }


        var currentSteps = mutableSetOf<Pair<Int, Int>>()
        var newSteps = mutableSetOf<Pair<Int, Int>>()

        var width = input.first().length
        var height = input.size

        currentSteps.add(start)

        var steps = 131*2 + 65  // fit 2 deg polynomial to the results...


        fun Int.findRef(size: Int = 11): Int {
            if (this > 0) {
                return (this + size / 2) % size - (size / 2)
            }
            else if (this < 0) {
//                kotlin.io.println("${this} ${abs(this - size / 2)}")
                return size - abs(this - size / 2) % size - (size / 2 + 1)
            }
            else return 0
        }

        var finalPos = mutableListOf<Pair<Int, Int>>()
        var preFinalPos = mutableListOf<Pair<Int, Int>>()

        for (i in 1 .. steps) {
            for (pos in currentSteps){
                for (dir in listOf(Pair(1, 0), Pair(-1, 0), Pair(0, 1), Pair(0, -1))) {
                    var newPos = Pair(pos.first + dir.first, pos.second + dir.second)
                    var refPos = Pair(newPos.first.findRef(width), newPos.second.findRef(width))
                    if (refPos in gardenPlots) {
                        newSteps.add(newPos)
                        if (i == steps) {
                            finalPos.add(newPos)
                        }
                        if (i == 131*1 + 65) {
                            preFinalPos.add(newPos)
                        }
                    }
                }
            }
//            println(newSteps)
            currentSteps = newSteps.map { it.copy() }.toMutableSet()
            newSteps = mutableSetOf<Pair<Int, Int>>()
        }

//        File("largeOutput21_12.txt").printWriter().use { out ->
//            for (i in finalPos.minOf { it.first } .. finalPos.maxOf { it.first }) {
//                var newRow = IntRange(finalPos.minOf { it.second }, finalPos.maxOf { it.second })
//                    .map { if (Pair(i, it) in finalPos) 'O' else if (Pair(i, it) in preFinalPos) 'X' else '.' }.joinToString ( separator="" )
//                out.println(newRow)
//            }
//        }

        println("size = ${currentSteps.size}")

        return 0
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day21_test")
//    check(part1(testInput) == 136)
//    part1(testInput, steps = 6).println()
//    part2(testInput).println()

    val input = readInput("Day21")
    // test if implementation meets criteria from the description, like:
//    part1(input).println()
    part2(input).println()
}
