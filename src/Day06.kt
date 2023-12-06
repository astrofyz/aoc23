import kotlin.math.floor
import kotlin.math.sqrt
fun main() {

    fun part1(): Int {
//        var t = listOf<Float>(7F, 15F, 30F)
//        var s = listOf<Float>(9.0F, 40.0F, 200.0F)
        var t = listOf<Float>(44F, 80F, 65F, 72F)
        var s = listOf<Float>(208F, 1581F, 1050F, 1102F)
        var ans = 1

        for (i in t.indices) {
            var upperRoot = floor((t[i] + sqrt(t[i] * t[i] - 4*s[i])) / 2.0F).toInt()
            if ((t[i].toInt() - upperRoot) * upperRoot == s[i].toInt()) {
                upperRoot -= 1
            }
            ans *= (t[i].toInt() - (t[i].toInt() - upperRoot)*2 + 1)
        }
        return ans
    }

    fun part2(): Long {
//        var t = 71530F
//        var s = 940200F
        var t = 44806572.toDouble()
        var s = 208158110501102.toDouble()

        var upperRoot = floor((t + sqrt(t * t - 4*s)) / 2.0F).toLong()
        if ((t.toLong() - upperRoot) * upperRoot == s.toLong()) {
            upperRoot -= 1
        }

        return (t.toInt() - (t.toLong() - upperRoot)*2 + 1)
    }

    // test if implementation meets criteria from the description, like:
    part1().println()
    part2().println()
}
