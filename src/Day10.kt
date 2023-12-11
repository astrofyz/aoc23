import java.util.Queue
fun main() {

//    class Tile(var crd: Pair<Int, Int>, var pipe: Char)

    fun processMap(input: List<String>): Pair<MutableMap<Pair<Int, Int>, MutableSet<Pair<Int, Int>>>, Pair<Int, Int>> {
        var startPoint = Pair(0, 0)
        var mapTiles = mutableMapOf<Pair<Int, Int>, MutableSet<Pair<Int, Int>>>()
        for ((i, line) in input.withIndex()) {
            for ((j, elem) in line.withIndex()) {
                when (elem) {
                    '|' -> mapTiles[Pair(i, j)] = mutableSetOf(Pair(i - 1, j), Pair(i + 1, j))
                    '-' -> mapTiles[Pair(i, j)] = mutableSetOf(Pair(i, j - 1), Pair(i, j + 1))
                    'L' -> mapTiles[Pair(i, j)] = mutableSetOf(Pair(i - 1, j), Pair(i, j + 1))
                    'J' -> mapTiles[Pair(i, j)] = mutableSetOf(Pair(i - 1, j), Pair(i, j - 1))
                    '7' -> mapTiles[Pair(i, j)] = mutableSetOf(Pair(i + 1, j), Pair(i, j - 1))
                    'F' -> mapTiles[Pair(i, j)] = mutableSetOf(Pair(i + 1, j), Pair(i, j + 1))
                    'S' -> {startPoint = Pair(i, j); mapTiles[startPoint] = mutableSetOf()}
                    else -> continue
                }
            }
        }
        for (i in startPoint.first-1 .. startPoint.first + 1) {
            for (j in startPoint.second - 1 .. startPoint.second + 1) {
                if (Pair(i, j) == startPoint) continue
                else if (startPoint in mapTiles.getOrDefault(Pair(i, j), mutableSetOf())) {
                    mapTiles.getValue(startPoint).add(Pair(i, j))
                }
                else continue
            }
        }
        return Pair(mapTiles, startPoint)
    }


    fun findPath(mapTiles: MutableMap<Pair<Int, Int>, MutableSet<Pair<Int, Int>>>, start: Pair<Int, Int>): MutableList<Pair<Int, Int>> {
        var startPoint = start
        var visitedTiles = mutableListOf<Pair<Int, Int>>()
        var nextTile = Pair(0, 0)
        var countLength = 0

        while (true) {
            for (neighbor in mapTiles.getValue(startPoint)) {
                if (neighbor in visitedTiles) {nextTile = neighbor; continue}
                else {
                    nextTile = neighbor
                    visitedTiles.add(startPoint)
                    countLength += 1
                    break
                }
            }
            if (nextTile in visitedTiles) {visitedTiles.add(startPoint); break}
            startPoint = nextTile
        }
        return visitedTiles
    }

    fun part1(input: List<String>): Int {
        var (mapTiles, startPoint) = processMap(input)
        var visitedTiles = findPath(mapTiles, startPoint)
        return visitedTiles.size / 2
    }


    fun part2(input: List<String>): Int {
        var (mapTiles, startPoint) = processMap(input)
        var loopTiles = findPath(mapTiles, startPoint)

        loopTiles.add(loopTiles[0])


        var loopTilesDoubles = loopTiles.windowed(2).map { listOf(Pair(it.first().first*2, it.first().second*2),
            Pair((it.first().first + it.last().first), (it.first().second + it.last().second)))  }.toMutableList().flatten()


        var height = input.size * 2
        var width = input.first().length * 2

        val queue = ArrayDeque(listOf(Pair(-1, -1)))
        val outside = mutableSetOf<Pair<Int, Int>>()
        val maybeInside = mutableSetOf<Pair<Int, Int>>()


        fun Pair<Int, Int>.neighbours(): List<Pair<Int, Int>> {
            return listOf(
                Pair(this.first-1, this.second),
                Pair(this.first+1, this.second),
                Pair(this.first, this.second-1),
                Pair(this.first, this.second+1)
            )
        }

        while (queue.isNotEmpty()){
            for (air in queue.removeFirst().neighbours().filter{(it !in loopTilesDoubles)}){
                if ((air.first in -1 .. height) &&
                    (air.second in -1 .. width) &&
                    (air !in outside)){
                    queue.add(air)
                    outside.add(air)
                }
            }
        }

        for (i in 1 .. height - 2) {
            for (j in 1 .. width - 2) {
                if ((Pair(i, j) !in outside) && (Pair(i, j) !in loopTilesDoubles)) maybeInside.add(Pair(i, j))
            }
        }

        return maybeInside.map{Pair(it.first/2, it.second/2)}.toSet().filter { it !in loopTiles }.size

    }


    fun part2Old(input: List<String>): Int {
        var (mapTiles, startPoint) = processMap(input)
        var loopTiles = findPath(mapTiles, startPoint)

        var startFlood = Pair(-1, -1)

        var height = input.size
        var width = input.first().length

        val queue = ArrayDeque(listOf(Pair(-1, -1)))
        val outside = mutableSetOf<Pair<Int, Int>>()
        val maybeInside = mutableSetOf<Pair<Int, Int>>()
        val inside = mutableSetOf<Pair<Int, Int>>()


        fun Pair<Int, Int>.neighbours(): List<Pair<Int, Int>> {
            return listOf(
                Pair(this.first-1, this.second),
                Pair(this.first+1, this.second),
                Pair(this.first, this.second-1),
                Pair(this.first, this.second+1)
            )
        }

        while (queue.isNotEmpty()){
            for (air in queue.removeFirst().neighbours().filter{(it !in loopTiles)}){
                if ((air.first in -1 .. height) &&
                    (air.second in -1 .. width) &&
                    (air !in outside)){
                    queue.add(air)
                    outside.add(air)
                }
            }
        }

        for (i in 1 .. height - 2) {
            for (j in 1 .. width - 2) {
                if (!((Pair(i, j) in outside) || (Pair(i, j) in loopTiles))) maybeInside.add(Pair(i, j))
            }
        }

        // trying to compute how many times ray crosses the walls of the loop. didn't work for some reason:(
        for (elem in maybeInside) {
            var flag = 0
            for ((i, brd) in loopTiles.withIndex().filter { (it.value.second < elem.second) && (it.value.first == elem.first) }) {
                when {
                    loopTiles[(i-1).mod(loopTiles.size)].first < brd.first -> {flag += 1}
                    loopTiles[(i-1).mod(loopTiles.size)].first > brd.first -> {flag -= 1}
                    loopTiles[(i+1).mod(loopTiles.size)].first < brd.first -> {flag -= 1}
                    loopTiles[(i+1).mod(loopTiles.size)].first > brd.first -> {flag += 1}
                }
            }
            if (flag != 0) inside.add(elem)
        }

//        println(inside)
        return inside.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
//    check(part2(testInput) == 0)

    val input = readInput("Day10")
    // test if implementation meets criteria from the description, like:
//    part1(input).println()
    part1(input).println()
    part2(input).println()
}
