import java.util.Queue
import kotlin.math.abs
fun main() {

    class TurningMirror (val crd: Pair<Int, Int>, val dir: Pair<Int, Int>)

    fun TurningMirror.print() {
        kotlin.io.print(" $crd $dir ;;; ")
    }


    fun part1(input: List<String>, startDir: Pair<Pair<Int, Int>, Pair<Int, Int>>): Int {
        var mirrorCrds = mutableListOf<Pair<Int, Int>>()
        var mirrorsSymbols = mutableListOf<Char>()

        input.mapIndexed {i, row -> row.mapIndexed { j, c -> if (c != '.') {
            mirrorCrds.add(Pair(i, j))
            mirrorsSymbols.add(c)
        }}}

        var width = input.first().length
        var height = input.size

        var mirrors = mirrorCrds.zip(mirrorsSymbols).toMap()

        val startQueue = ArrayDeque(listOf(TurningMirror(startDir.first, startDir.second)))
        var startSet = mutableSetOf<Pair<Pair<Int, Int>, Pair<Int, Int>>>(Pair(startDir.first, startDir.second))

        var energizedTiles = mutableSetOf<Pair<Int, Int>>()
        var visitedMirrors = mutableSetOf<Pair<Pair<Int, Int>, Pair<Int, Int>>>()

        var i = 0

        var matrixDir = mapOf('/' to listOf(listOf(0, -1), listOf(-1, 0)),
            '\\' to listOf(listOf(0, 1), listOf(1, 0))
        )

        fun Pair<Int,Int>.matmul(mtrx: List<List<Int>>): Pair<Int, Int> {
            return Pair(this.first*mtrx[0][0]+this.second*mtrx[1][0], this.first*mtrx[0][1]+this.second*mtrx[1][1])
        }

        while (startQueue.isNotEmpty()){ // queue.removeFirst()
//            startQueue.map { it.print() }
//            println(startSet.size)
            var tm = startQueue.removeFirst()
            var stPos = tm.crd
            var dir = tm.dir


            while ((stPos.first >= 0)&&(stPos.first < height)&&(stPos.second >= 0)&&(stPos.second < width)) {
                var filteredMirrors = mirrors.filter { if (dir.first == 0) (it.key.first == stPos.first)&&((it.key.second - stPos.second)*dir.second >= 0)
                else (it.key.second == stPos.second)&&((it.key.first - stPos.first)*dir.first >= 0)}

                if (filteredMirrors.isEmpty()) {
                    var metMirrorCrd = Pair(width, height)
                    when (dir) {
                        Pair(0, 1) -> metMirrorCrd = Pair(stPos.first, width-1)
                        Pair(0, -1) -> metMirrorCrd = Pair(stPos.first, 0)
                        Pair(1, 0) -> metMirrorCrd = Pair(height-1, stPos.second)
                        Pair(-1, 0) -> metMirrorCrd = Pair(0, stPos.second)
                    }
                    // add all energized tiles to the set of tiles
                    IntRange(minOf(stPos.first, metMirrorCrd.first), maxOf(stPos.first, metMirrorCrd.first))
                        .map { i -> IntRange(minOf(stPos.second, metMirrorCrd.second), maxOf(stPos.second, metMirrorCrd.second))
                            .map { j -> energizedTiles.add(Pair(i, j)) } }

                    break
                }

                var metMirrorCrd = filteredMirrors
                    .toSortedMap(compareBy { (it.first - stPos.first) * dir.first + (it.second - stPos.second) * dir.second }).firstKey()


                // add all energized tiles to the set of tiles
                IntRange(minOf(stPos.first, metMirrorCrd.first), maxOf(stPos.first, metMirrorCrd.first))
                    .map { i -> IntRange(minOf(stPos.second, metMirrorCrd.second), maxOf(stPos.second, metMirrorCrd.second))
                        .map { j -> energizedTiles.add(Pair(i, j)) } }


                // check what kind of mirror is encountered
                if (mirrors.getValue(metMirrorCrd) == '\\') {
                    stPos = metMirrorCrd
                    dir = dir.matmul(matrixDir.getValue(mirrors.getValue(metMirrorCrd)))
                }
                else if (mirrors.getValue(metMirrorCrd) == '/') {
                    stPos = metMirrorCrd
                    dir = dir.matmul(matrixDir.getValue(mirrors.getValue(metMirrorCrd)))
                }
                else if (((mirrors.getValue(metMirrorCrd) == '-')&&(dir.first == 0))||
                        ((mirrors.getValue(metMirrorCrd) == '|')&&(dir.second == 0))) {
                    stPos = metMirrorCrd
                }
                // split the beam
                else if ((mirrors.getValue(metMirrorCrd) == '-')&&(dir.second == 0)) {
                    stPos = metMirrorCrd
                    dir = Pair(0, 1)

                    if (Pair(Pair(metMirrorCrd.first, metMirrorCrd.second-1), Pair(0, -1)) !in startSet) {
                        startQueue.add(TurningMirror(Pair(metMirrorCrd.first, metMirrorCrd.second-1), Pair(0, -1)))
                        startSet.add(Pair(Pair(metMirrorCrd.first, metMirrorCrd.second-1), Pair(0, -1)))
                    }
                }
                // split the beam
                else if ((mirrors.getValue(metMirrorCrd) == '|')&&(dir.first == 0)) {
                    stPos = metMirrorCrd
                    dir = Pair(1, 0)

                    if (Pair(Pair(metMirrorCrd.first - 1, metMirrorCrd.second), Pair(-1, 0)) !in startSet) {
                        startQueue.add(TurningMirror(Pair(metMirrorCrd.first - 1, metMirrorCrd.second), Pair(-1, 0)))
                        startSet.add(Pair(Pair(metMirrorCrd.first - 1, metMirrorCrd.second), Pair(-1, 0)))
                    }
                }
                stPos = Pair(stPos.first + dir.first, stPos.second + dir.second)

                // check that light ray is not stuck in an infinite loop
                if (Pair(metMirrorCrd, dir) in visitedMirrors) break
                else visitedMirrors.add(Pair(metMirrorCrd, dir))

            }
        }

        return energizedTiles.toSet().size
    }


    fun part2(input: List<String>): Int {
        var ans = mutableListOf<Int>()

        var width = input.first().length
        var height = input.size

        for (i in input.indices) {
            ans.add(part1(input, Pair(Pair(i, 0), Pair(0, 1))))
            ans.add(part1(input, Pair(Pair(i, width-1), Pair(0, -1))))
        }

        for (j in 0 until height) {
            ans.add(part1(input, Pair(Pair(0, j), Pair(1, 0))))
            ans.add(part1(input, Pair(Pair(height-1, j), Pair(-1, 0))))
        }
        return ans.max()

    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day16_test")
//    check(part1(testInput) == 136)
    part1(testInput, Pair(Pair(0, 0), Pair(0, 1))).println()
    part2(testInput).println()

    val input = readInput("Day16")
    // test if implementation meets criteria from the description, like:
    part1(input, Pair(Pair(0, 0), Pair(0, 1))).println()
    part2(input).println()
}
