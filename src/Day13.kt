import java.nio.file.Path
import java.util.Queue
import kotlin.math.abs
import kotlin.io.path.Path
import kotlin.io.path.readText

fun main() {

    fun part1(fname: String): Int {
        var input = Path("input/$fname.txt").readText()
        
        var patterns = input.split("\n\n")

        var ans = 0
        
        for (ptrn in patterns) {
            var crds = ptrn.split("\n").mapIndexed { i, s -> s.mapIndexed { j, c -> if (c == '#') Pair(i, j) else null }.filterNotNull().toList() }.toList().flatten()
            var width = ptrn.split("\n")[0].length
            var height = ptrn.split("\n").size

            var flag = 0

            for (j in 1 until height) {
                var crdsUp = crds.filter { (it.first < j) }.map { Pair(j - 1 - it.first, it.second) }.filter { it.first < minOf(j, height - j) }
                var crdsDown = crds.filter { (it.first >= j) }.map { Pair(it.first-j, it.second) }.filter { it.first < minOf(j, height - j) }
                if (crdsUp.toSet() == crdsDown.toSet()) {ans += 100 * j; flag = 1}
            }

            if (flag == 0) {
                for (j in 1 until width) {
                    var crdsUp = crds.filter { (it.second < j) }.map { Pair(it.first, j - 1 - it.second) }.filter { it.second < minOf(j, width - j) }
                    var crdsDown = crds.filter { (it.second >= j) }.map { Pair(it.first, it.second - j) }.filter { it.second < minOf(j, width - j) }
                    if (crdsUp.toSet() == crdsDown.toSet()) { ans += j}
                }
            }
        }
        return ans
    }


    fun part2(fname: String): Int {
        var input = Path("input/$fname.txt").readText()

        var patterns = input.split("\n\n")

        var ans = 0

        for (ptrn in patterns) {
            var crds = ptrn.split("\n").mapIndexed { i, s -> s.mapIndexed { j, c -> if (c == '#') Pair(i, j) else null }.filterNotNull().toList() }.toList().flatten()
            var width = ptrn.split("\n")[0].length
            var height = ptrn.split("\n").size

            var flag = 0

            for (j in 1 until height) {
                var crdsUp = crds.filter { (it.first < j) }.map { Pair(j - 1 - it.first, it.second) }.filter { it.first < minOf(j, height - j) }.toSet()
                var crdsDown = crds.filter { (it.first >= j) }.map { Pair(it.first-j, it.second) }.filter { it.first < minOf(j, height - j) }.toSet()
                if (((crdsUp - crdsDown) union (crdsDown - crdsUp)).size == 1) {ans += 100 * j; flag = 1}
            }

            if (flag == 0) {
                for (j in 1 until width) {
                    var crdsUp = crds.filter { (it.second < j) }.map { Pair(it.first, j - 1 - it.second) }.filter { it.second < minOf(j, width - j) }.toSet()
                    var crdsDown = crds.filter { (it.second >= j) }.map { Pair(it.first, it.second - j) }.filter { it.second < minOf(j, width - j) }.toSet()
                    if (((crdsUp - crdsDown) union (crdsDown - crdsUp)).size == 1) {ans += j}
                }
            }

        }

        return ans
    }




    // test if implementation meets criteria from the description, like:
//    val testInput = readInput("Day13_test")
    check(part1("Day13_test") == 405)
    check(part2("Day13_test") == 400)
//    part1(testInput)


//    part1("Day13").println()
    part2("Day13").println()
}
