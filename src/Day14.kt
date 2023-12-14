import java.util.Queue
import kotlin.math.abs
fun main() {
    fun part1(input: List<String>): Int {
        var rocks = mutableListOf<Pair<Int, Int>>()
        var stops = mutableListOf<Pair<Int, Int>>()
        var height = input.size

        fun List<Int>.maxOrDefault(defaultVal: Int): Int {
            if (this.isEmpty()) return defaultVal
            return this.max() + 1
        }

        input.mapIndexed { i, row -> row.mapIndexed { j, c ->
            when (c) {
                '#' -> stops.add(Pair(i, j))
                'O' -> {
                    rocks.add(Pair(
                    (rocks.filter { it.second == j }.map{it.first}.toList()+
                            stops.filter { it.second == j }.map{it.first}.toList()).maxOrDefault(0), j))}
                else -> println(j)
            }
        }}
        return rocks.sumOf { (height - it.first) }
    }


    fun part2(input: List<String>): Int {
        var rocks = mutableListOf<Pair<Int, Int>>()
        var stops = mutableListOf<Pair<Int, Int>>()
        var height = input.size
        var width = input.first().length

        fun List<Int>.maxOrDefault(defaultVal: Int): Int {
            if (this.isEmpty()) return defaultVal
            return this.max() + 1
        }

        fun List<Int>.minOrDefault(defaultVal: Int): Int {
            if (this.isEmpty()) return defaultVal
            return this.min() - 1
        }

        input.mapIndexed { i, row -> row.mapIndexed { j, c ->
            when (c) {
                '#' -> stops.add(Pair(i, j))
                'O' -> rocks.add(Pair(i, j))
                else -> null
            }
        }.filterNotNull()}


        fun MutableList<Pair<Int, Int>>.cycle(stops: MutableList<Pair<Int, Int>>): MutableList<Pair<Int, Int>> {
            var inputRocks = this
            var modifiedRocks = mutableListOf<Pair<Int, Int>>()

            //north
            for (elem in inputRocks.sortedWith(compareBy ({it.first}, {it.second}))) {
                modifiedRocks.add(
                    Pair(
                        (modifiedRocks.filter { (it.first < elem.first)&&(it.second == elem.second) }.map{it.first} +
                        stops.filter { (it.first < elem.first) && (it.second == elem.second) }.map{it.first}).maxOrDefault(0),
                        elem.second
                )
                )
            }

//            kotlin.io.println("north ${modifiedRocks.sortedWith(compareBy({it.first}, {it.second}))}")

            inputRocks = modifiedRocks.toMutableList()
            modifiedRocks = mutableListOf<Pair<Int, Int>>()


            //west
            for (elem in inputRocks.sortedWith(compareBy ({it.second}, {it.first}))) {
                modifiedRocks.add(
                    Pair(elem.first,
                        (modifiedRocks.filter { (it.second < elem.second)&&(it.first == elem.first) }.map{it.second} +
                                stops.filter { (it.second < elem.second) && (it.first == elem.first) }.map{it.second})
                            .maxOrDefault(0)
                    )
                )
            }

//            kotlin.io.println("east ${modifiedRocks.sortedWith(compareBy({it.first}, {it.second}))}")

            inputRocks = modifiedRocks.toMutableList()
            modifiedRocks = mutableListOf<Pair<Int, Int>>()

            //south
            for (elem in inputRocks.sortedWith(compareBy ({-it.first}, {it.second}))) {
                modifiedRocks.add(
                    Pair(
                        (modifiedRocks.filter { (it.first > elem.first)&&(it.second == elem.second) }.map{it.first} +
                                stops.filter { (it.first > elem.first) && (it.second == elem.second) }.map{it.first})
                            .minOrDefault(height-1),
                        elem.second
                    )
                )
            }

//            kotlin.io.println("south ${modifiedRocks.sortedWith(compareBy({it.first}, {it.second}))}")

            inputRocks = modifiedRocks.toMutableList()
            modifiedRocks = mutableListOf<Pair<Int, Int>>()


            //east
            for (elem in inputRocks.sortedWith(compareBy ({-it.second}, {it.first}))) {
                modifiedRocks.add(
                    Pair(elem.first,
                        (modifiedRocks.filter { (it.second > elem.second)&&(it.first == elem.first) }.map{it.second} +
                                stops.filter { (it.second > elem.second) && (it.first == elem.first) }.map{it.second})
                            .minOrDefault(width-1)
                    )
                )
            }

//            kotlin.io.println("west ${modifiedRocks.sortedWith(compareBy({it.first}, {it.second}))}")


            return modifiedRocks
        }

        var cycledRocks = mutableListOf<MutableSet<Pair<Int, Int>>>()
        var loads = mutableListOf<Int>()
        var repeatFrom = 0
        var repeatTo = 0

        for (i in 1 until 1000000000 ) {
            rocks = rocks.cycle(stops)
            if (rocks.toMutableSet() in cycledRocks) {
                repeatTo = i
                cycledRocks.mapIndexed{index, c -> if (c==rocks.toMutableSet()) repeatFrom = index }
                break
            }
            cycledRocks.add(rocks.toMutableSet())
            loads.add(rocks.sumOf { (height - it.first) })
        }
        var idx = ((1000000000-(repeatFrom+1)) % (repeatTo - repeatFrom - 1)) + repeatFrom


        return loads[idx]

    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day14_test")
//    check(part1(testInput) == 136)
    part2(testInput)

    val input = readInput("Day14")
    // test if implementation meets criteria from the description, like:
    part1(input).println()
    part2(input).println()
}
