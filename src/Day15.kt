import java.util.Queue
import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.io.path.readText
import kotlin.math.abs
fun main() {

    fun hashCode(input: String): Int {
        var code = 0
        input.forEach { code+=it.code; code *= 17; code %= 256 }
        return code
    }

    fun part1(name: String): Int {
        var input = Path("input/$name.txt").readText()
        return input.split(",").map { hashCode(it) }.sum()
    }

    class Lens(var label: String, var focalLength: Int)


    fun part2(name: String): Int {
        var input = Path("input/$name.txt").readText()
        var boxes = IntRange(0, 255).map { mutableListOf<Lens>() }.toMutableList()
        for (operation in input.split(',')) {
            if ('-' in operation) {
                var workLabel = operation.takeWhile { it != '-' }
                var boxNumber = hashCode(workLabel)
                var idx = boxes[boxNumber].indexOfFirst { it.label == workLabel }
                if (idx > -1) {
                    boxes[boxNumber].removeAt(idx)
                }
                else continue
            }
            else if ('=' in operation) {
                var workLabel = operation.takeWhile { it != '=' }
                var boxNumber = hashCode(workLabel)
                var focalL = operation.takeLast(1).toInt()
                var idx = boxes[boxNumber].indexOfFirst { it.label == workLabel }
                if (idx > -1) {
                    boxes[boxNumber][idx] = Lens(workLabel, focalL)
                }
                else {
                    boxes[boxNumber].add(Lens(workLabel, focalL))
                }
            }
        }
        return boxes.mapIndexed { index, box -> if (!box.isEmpty()) {
            box.mapIndexed { indexL, lens -> (1 + index) * (indexL + 1) * lens.focalLength }.sum()
        } else 0 }.sum()
     }


    // test if implementation meets criteria from the description, like:
    val testInput = "Day15_test"
//    check(part1(testInput) == 136)
    part2(testInput).println()

    val input = "Day15"
    // test if implementation meets criteria from the description, like:
    part1(input).println()
    part2(input).println()
}
