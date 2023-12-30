import java.io.File
import java.security.KeyStore.TrustedCertificateEntry
import java.util.ArrayDeque
import java.util.Arrays
import java.util.InputMismatchException
import kotlin.math.abs
//import kotlin.math.mod
fun main() {

    class Brick (var x1: Int, var x2: Int, var y1: Int, var y2: Int, var z1: Int, var z2: Int)

    fun Brick.state(): String {
        return "${this.x1},${this.y1},${this.z1}~${this.x2},${this.y2},${this.z2}"
    }

    fun parseCoords(input: String): Brick {
        return Brick(x1 = input.split('~')[0].split(',')[0].toInt(),
            x2 = input.split('~')[1].split(',')[0].toInt(),
            y1 = input.split('~')[0].split(',')[1].toInt(),
            y2 = input.split('~')[1].split(',')[1].toInt(),
            z1 = input.split('~')[0].split(',')[2].toInt(),
            z2 = input.split('~')[1].split(',')[2].toInt())
    }

    fun part1(input: List<String>) : Int {
        var bricks = mutableListOf<Brick>()
        var fallenBricks = mutableListOf<Brick>()


        for (line in input) {
            bricks.add(parseCoords(line))
        }

        fun Brick.crossBricks(brick: Brick): Boolean {
            return (IntRange(this.x1,this.x2).intersect(IntRange(brick.x1,brick.x2)).size)*
                    (IntRange(this.y1,this.y2).intersect(IntRange(brick.y1,brick.y2)).size) > 0
        }

        fallenBricks.addAll(bricks.filter { (it.z1 == 1) })

        var brickMap = mutableMapOf<Brick, MutableMap<String, MutableList<Brick>>>()

        loop@for (brick in bricks.filter { it.z1 > 1 }.sortedBy { it.z1 }) {
            for (lowerbrick in fallenBricks.sortedByDescending { it.z2 }.filter { it.z2 < brick.z1 }) {
                if (lowerbrick.crossBricks(brick)) {
                    fallenBricks.add(
                        Brick(
                            x1 = brick.x1, x2 = brick.x2, y1 = brick.y1, y2 = brick.y2,
                            z1 = lowerbrick.z2 + 1, z2 = lowerbrick.z2 + 1 + brick.z2 - brick.z1
                        )
                    )
                    continue@loop
                }
            }
            fallenBricks.add(
                Brick(
                    x1 = brick.x1, x2 = brick.x2, y1 = brick.y1, y2 = brick.y2,
                    z1 = 1, z2 = 1 + brick.z2 - brick.z1
                )
            )
        }

        fallenBricks.map { brickMap[it] = mutableMapOf(); brickMap.getValue(it)["support"] = mutableListOf(); brickMap.getValue(it)["supportBy"] = mutableListOf(); }

        for (brick in fallenBricks.sortedBy { it.z1 }) {
            for (lowerbrick in fallenBricks.filter { it.z2 == brick.z1 - 1 }) {
                if (lowerbrick.crossBricks(brick)) {
                    brickMap.getValue(brick).getValue("supportBy").add(lowerbrick)
                    brickMap.getValue(lowerbrick).getValue("support").add(brick)
                }
            }
        }

//        brickMap.map { println() }


        println(fallenBricks.size - brickMap.filterValues { it.getValue("supportBy").size == 1 }.map { it.value.getValue("supportBy") }.flatten().toSet().size)

        var fallenNumber = mutableMapOf<Brick, Int>()
//        for (brick in fallenBricks.sortedByDescending { it.z2 }) {
//            println(brick.state())
//            var potentialFallen = brickMap.getValue(brick).getValue("support")
//            println("potential fallen: ${potentialFallen.map { it.state() }}")
//            for (potFall in potentialFallen) {
//                if (brickMap.getValue(potFall).getValue("supportBy").size == 1 ) {
//                    println("fall ${potFall.state()}")
//                    fallenNumber[brick] = fallenNumber.getOrDefault(brick, 0) + 1 + fallenNumber.getOrDefault(potFall, 0)
//                    println(fallenNumber[brick])
//                }
//            }
//        }

//        fallenNumber.map { println("${it.key.state()} : ${it.value}") }


        var ans = 0

        for (brick in fallenBricks) {
            var fallenHeroes = mutableSetOf<Brick>()
            fallenHeroes.add(brick)

            fun Brick.countDamage(): Int {
                for (brick in brickMap.getValue(this).getValue("support").filter { brickMap.getValue(it).getValue("supportBy").subtract(fallenHeroes).size == 0 }) {
                    fallenHeroes.add(brick)
                    brick.countDamage()
                }
                return fallenHeroes.size - 1
            }

//            println(brick.state())
            ans += brick.countDamage()  //.also { println(it) }

        }

        println("total damage ${ans}")
        return  0
    }


    fun part2(input: List<String>): Int {

        return 0
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day22_test")
//    check(part1(testInput) == 136)
//    part1(testInput, steps = 6).println()
//    part1(testInput).println()

    val input = readInput("Day22")
    // test if implementation meets criteria from the description, like:
    part1(input).println()
//    part2(input).println()
}
