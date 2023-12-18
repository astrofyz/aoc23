import kotlin.math.abs
fun main() {

    fun part1(input: List<String>): Int {
        var start = Pair(0, 0)
        var border = mutableSetOf<Pair<Int, Int>>(start)

        var end = start

        for (line in input) {
            var dir = line.split(' ')[0]
            var steps = line.split(' ')[1].toInt()

            when (dir) {
                "R" -> end = Pair(start.first, start.second + steps)
                "L" -> end = Pair(start.first, start.second - steps)
                "U" -> end = Pair(start.first - steps, start.second)
                "D" -> end = Pair(start.first + steps, start.second)
            }

            border.addAll(IntRange(minOf(start.first, end.first), maxOf(start.first, end.first))
                .map { i -> IntRange(minOf(start.second, end.second), maxOf(start.second, end.second))
                    .map { j -> Pair(i, j) }.toList() }.toList().flatten())

            start = end
        }

        var minUp = border.minOf { it.first }
        var minLeft = border.minOf { it.second }

        border = border.map { Pair(it.first - minUp, it.second - minLeft) }.toMutableSet()

        var maxDown = border.maxOf { it.first }
        var maxRight = border.maxOf { it.second }

//        println(border.sortedWith(compareBy ( {it.first}, {it.second} )))

        val queue = ArrayDeque(listOf(Pair(-1, -1)))
        val outside = mutableSetOf<Pair<Int, Int>>()


        fun Pair<Int, Int>.neighbours(): List<Pair<Int, Int>> {
            return listOf(
                Pair(this.first-1, this.second),
                Pair(this.first+1, this.second),
                Pair(this.first, this.second-1),
                Pair(this.first, this.second+1)
            )
        }

        while (queue.isNotEmpty()){
            for (air in queue.removeFirst().neighbours().filter{(it !in border)}){
                if ((air.first in -1 .. maxDown + 1) &&
                    (air.second in -1 .. maxRight + 1) &&
                    (air !in outside)){
                    queue.add(air)
                    outside.add(air)
                }
            }
        }
//        println(outside.sortedWith(compareBy ( {it.first}, {it.second} )))
        println((maxDown + 3)*(maxRight + 3) - outside.size)

        return 0
    }


    fun part2(input: List<String>): Long {
        var start = Pair(0.toLong(), 0.toLong())
        var corners = mutableSetOf<Pair<Long, Long>>(start)

        var end = start

        var perimeter = 0.toLong()

        for (line in input) {
//            var dir = line.split(' ')[0]
//            var steps = line.split(' ')[1].toInt()
//
//            when (dir) {
//                "R" -> end = Pair(start.first, start.second + steps)
//                "L" -> end = Pair(start.first, start.second - steps)
//                "U" -> end = Pair(start.first - steps, start.second)
//                "D" -> end = Pair(start.first + steps, start.second)
//            }

            var dir = line.split(' ')[2].takeLast(2).first()
            var steps = line.split(' ')[2].drop(2).dropLast(2).toLong(radix = 16)

            perimeter += steps


            when (dir) {
                '0' -> end = Pair(start.first, start.second + steps)
                '2' -> end = Pair(start.first, start.second - steps)
                '3' -> end = Pair(start.first - steps, start.second)
                '1' -> end = Pair(start.first + steps, start.second)
            }

            corners.add(end)

            start = end
        }

        var minUp = corners.minOf { it.first }.toLong()
        var minLeft = corners.minOf { it.second }.toLong()

        corners = corners.map { Pair(it.first - minUp, it.second-minLeft) }.toMutableSet()

        var area = corners.windowed(2).map { it[0].first*it[1].second - it[1].first*it[0].second }.sum()/2
//        println(corners)
//        println(area)
//        println(perimeter)
//        println(abs(area) + perimeter/2 + 1)
        return abs(area) + perimeter/2 + 1
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day18_test")
//    check(part1(testInput) == 136)
//    part1(testInput).println()
    part2(testInput).println()

    val input = readInput("Day18")
    // test if implementation meets criteria from the description, like:
//    part1(input).println()
    part2(input).println()
}
