import java.io.File
import java.security.KeyStore.TrustedCertificateEntry
import java.util.ArrayDeque
import java.util.InputMismatchException
import kotlin.math.abs
//import kotlin.math.mod
fun main() {

    class Hailstone(var px: Long, var py: Long, var pz: Long, var vx: Long, var vy: Long, var vz: Long)

    fun Hailstone.state(): String {
        return "${this.px} ${this.py} ${this.pz} @ ${this.vx} ${this.vy} ${this.vz}"
    }

    fun Hailstone.x(t: Long): Long {
        return this.px + this.vx * t
    }

    fun Hailstone.y(t: Long): Long {
        return this.py + this.vy * t
    }

    fun part1(input: List<String>, range: LongRange = 7.toLong() .. 27.toLong()) : Int {
        var paths = mutableListOf<Hailstone>()

        for (line in input) {
            paths.add(Hailstone(line.split(" @ ")[0].split(", ")[0].trim().toLong(),
                line.split(" @ ")[0].split(", ")[1].trim().toLong(),
                line.split(" @ ")[0].split(", ")[2].trim().toLong(),
                line.split(" @ ")[1].split(", ")[0].trim().toLong(),
                line.split(" @ ")[1].split(", ")[1].trim().toLong(),
                line.split(" @ ")[1].split(", ")[2].trim().toLong()))
        }

//        paths.map { println(it.state()) }

        var ans = 0
        for (i in paths.indices) {
            for (j in i+1  until  paths.size){
                // check if solution exists
                var delta = paths[i].vx*(paths[j].vy) - (paths[j].vx)*(paths[i].vy)

                if (delta != 0.toLong()) {
                    var t2 = (-paths[i].vx*(paths[j].py-paths[i].py) + paths[i].vy*(paths[j].px - paths[i].px)) / delta
                    var t1 = ((paths[j].px - paths[i].px) + paths[j].vx * t2) / paths[i].vx

                    if ((t2 > 0)&&(t1 > 0)) {
                        var x1 = paths[i].px + paths[i].vx * t1
                        var y1 = paths[i].py + paths[i].vy * t1
                        var x2 = paths[j].px + paths[j].vx * t2
                        var y2 = paths[j].py + paths[j].vy * t2

                        if ((x1 > range.first)&&(x1 < range.endInclusive)&&(y1 > range.first)&&(y1 < range.endInclusive)) {
                            println(paths[i].state())
                            println(paths[j].state())
                            ans += 1
                        }
                    }
                }

//                if ((tx == ty)) {
//                    if ((paths[i].x(tx) >= range.first)&&(paths[i].x(tx) <= range.endInclusive)&&
//                        (paths[i].y(tx) >= range.first)&&(paths[i].y(tx) <= range.endInclusive)) {
//                        ans += 1
//                        println(paths[i].state())
//                        println(paths[j].state())
//                        println("################")
                }}
        println(ans)
        return  0
    }


    fun part2(input: List<String>): Int {

        return 0
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day24_test")
//    check(part1(testInput) == 136)
//    part1(testInput, steps = 6).println()
    part1(testInput).println()

    val input = readInput("Day24")
    // test if implementation meets criteria from the description, like:
    part1(input, range = 200000000000000 .. 400000000000000).println()
//    part2(input).println()
}
