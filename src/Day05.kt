import kotlin.math.pow
fun main() {

    fun part1(input: List<String>): Long {
        var seeds = input[0].drop(7).split(" ").map { it.toLong() }
        var destination = seeds.toMutableList()
        var instruction = mutableListOf<MutableMap<Int, Int>>()
        for (line in input.drop(1).filter { it.isNotBlank() }) {
            if (line.first().isDigit()) {
                var (dest, src, lng) = line.split(' ').map { it.toLong() }
                seeds.mapIndexed{ i, elem -> if (elem in src until src+lng) destination[i] = dest + elem - src}
            }
            else {seeds = destination.toMutableList()}
        }
        return destination.min()
    }


    fun intersectRanges(seedsRange: MutableList<Pair<Long, Long>>,
                        instructions: MutableMap<Pair<Long, Long>, Pair<Long, Long>>): MutableList<Pair<Long, Long>> {
        var seedId = 0
        var instrId = 0

        var destRange = mutableListOf<Pair<Long, Long>>()

        while (seedId < seedsRange.size) {
            if (seedsRange[seedId].second < instructions.keys.toList()[instrId].first) {
                destRange.add(seedsRange[seedId])
                seedId += 1
            }
            else if (seedsRange[seedId].first > instructions.keys.toList()[instrId].second) {
                if (instrId == instructions.size-1) {
                    destRange.add(Pair(seedsRange[seedId].first, seedsRange[seedId].second))
                    seedId += 1
                }
                else instrId += 1
            }
            else if ((seedsRange[seedId].first < instructions.keys.toList()[instrId].first)
                &&(seedsRange[seedId].second > instructions.keys.toList()[instrId].second)) {
                destRange.add(Pair(seedsRange[seedId].first, instructions.keys.toList()[instrId].first-1))
                seedsRange[seedId] = Pair(instructions.keys.toList()[instrId].first, seedsRange[seedId].second)
            }
            else if ((seedsRange[seedId].first >= instructions.keys.toList()[instrId].first)
                &&(seedsRange[seedId].second <= instructions.keys.toList()[instrId].second)) {
                var destCur = instructions.getValue(instructions.keys.toList()[instrId]).first
                var srcCur = instructions.keys.toList()[instrId].first
                destRange.add(Pair(destCur + seedsRange[seedId].first - srcCur, destCur + seedsRange[seedId].second - srcCur))
                seedId += 1
            }
            else if ((seedsRange[seedId].first >= instructions.keys.toList()[instrId].first)
                &&(seedsRange[seedId].second > instructions.keys.toList()[instrId].second)) {
                var curSeedRange = Pair(seedsRange[seedId].first, instructions.keys.toList()[instrId].second)
                var destCur = instructions.getValue(instructions.keys.toList()[instrId]).first
                var srcCur = instructions.keys.toList()[instrId].first
                destRange.add(Pair(destCur + curSeedRange.first - srcCur, destCur + curSeedRange.second - srcCur))
                seedsRange[seedId] = Pair(instructions.keys.toList()[instrId].second + 1, seedsRange[seedId].second)
                if (instrId < instructions.size-1) instrId += 1
            }
            else if ((seedsRange[seedId].first < instructions.keys.toList()[instrId].first)
                &&(seedsRange[seedId].second <= instructions.keys.toList()[instrId].second)) {
                var curSeedRange = Pair(instructions.keys.toList()[instrId].first, seedsRange[seedId].second)
                var destCur = instructions.getValue(instructions.keys.toList()[instrId]).first
                var srcCur = instructions.keys.toList()[instrId].first
                destRange.add(Pair(destCur + curSeedRange.first - srcCur, destCur + curSeedRange.second - srcCur))
                destRange.add(Pair(seedsRange[seedId].first, instructions.keys.toList()[instrId].first-1))
                seedId += 1
            }
        }

        return destRange.sortedBy { it.first }.map { it.copy() }.toMutableList()
    }

    fun part2(input: List<String>): Long {
        var seedsEdges = input[0].drop(7).split(" ").map { it.toLong() }
        var seedsRange = seedsEdges.chunked(2).map { Pair(it[0], it[0]+it[1]-1) }.sortedBy { it.first }.toMutableList() //.flatten() //

        var destRange = mutableListOf<Pair<Long, Long>>()

        var instructions = mutableMapOf<Pair<Long, Long>, Pair<Long, Long>>()

        for (line in input.drop(3).filter { it.isNotBlank() }) {
            if (line.first().isDigit()) {
                var (dest, src, lng) = line.split(' ').map { it.toLong() }
                instructions[Pair(src, src+lng-1)] = Pair(dest, dest+lng-1.toLong())
            }
            else {
                instructions = instructions.toSortedMap(compareBy { it.first })

                seedsRange = intersectRanges(seedsRange, instructions)
                instructions = mutableMapOf<Pair<Long, Long>, Pair<Long, Long>>()
            }
        }
        instructions = instructions.toSortedMap(compareBy { it.first })
        seedsRange = intersectRanges(seedsRange, instructions)

        return seedsRange[0].first
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part2(testInput) == 46.toLong())

    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}
