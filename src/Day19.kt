import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.io.path.readText
import kotlin.math.abs
fun main() {

    fun part1(name: String): Int {
        var input = Path("input/$name.txt").readText()
        var rulesInput = input.split("\n\n").first()
        var partsInput = input.split("\n\n").last()


        var rules = mutableMapOf<String, MutableList<String>>()

        for (line in rulesInput.split("\n")) {
            var key = line.takeWhile { it != '{' }
            rules[key] = mutableListOf()
            for (rl in line.split('{').last().split(',').dropLast(1)){
                rules.getValue(key).add(rl)
                }
            rules.getValue(key).add(line.split('{').last().split(',').last().dropLast(1))
        }

        println(rules)

        fun Map<Char, String>.compare(rule: MutableList<String>): String {
            for (rl in rule.dropLast(1)) {
                var key = rl.first()
                var comp = rl.toCharArray()[1]
                var number = rl.filter { it.isDigit() }.toInt()
                var dest = rl.takeLastWhile { it != ':' }
                if ((comp == '<')&&(this.getValue(key).toInt() < number)) return dest
                if ((comp == '>')&&(this.getValue(key).toInt() > number)) return dest
            }
            return rule.last()
        }

        var ans = 0.toLong()
        for (line in partsInput.split('\n')) {
            var part = line.trim { it in listOf('{', '}') }.split(',')
                .associate { it.first() to it.takeLastWhile { it != '=' } }
            println(part)
            var start = "in"
            while (start !in listOf("R", "A")) {
                start = part.compare(rules.getValue(start))
                println("goes to $start")
            }
            if (start == "A") {println(part.values.map { it.toInt() }.sum()); ans += part.values.sumOf { it.toLong() }
            }
        }

        println(ans)

        return 0
    }


    class State(var startWorkflow: String, var ruleId: Int, var parts: MutableMap<Char, IntRange>)

    fun State.print() {
        kotlin.io.println("current workflow $startWorkflow ruleId $ruleId parts $parts")
    }

    fun part2(name: String): Long {
        var input = Path("input/$name.txt").readText()
        var rulesInput = input.split("\n\n").first()
        var partsInput = input.split("\n\n").last()

        var rules = mutableMapOf<String, MutableList<List<String>>>()

        for (line in rulesInput.split("\n")) {
            var key = line.takeWhile { it != '{' }
            rules[key] = mutableListOf()
            for (rl in line.split('{').last().split(',').dropLast(1)){
                rules.getValue(key).add(listOf(rl.take(1),  // x, m, a, s
                    rl.toCharArray()[1].toString(),  // comparator
                    rl.filter { it.isDigit() },  // number
                    rl.takeLastWhile { it != ':' } // destination
                    ))
            }
            rules.getValue(key).add(listOf(line.split('{').last().split(',').last().dropLast(1)))
        }

        var accepted = mutableListOf<Map<Char, IntRange>>()
        var queue = ArrayDeque<State>()
        var initalParts = mutableMapOf('x' to IntRange(1, 4000),
            'm' to IntRange(1, 4000),
            'a' to IntRange(1, 4000),
            's' to IntRange(1, 4000))

        queue.add(State("in", 0, initalParts))

        fun State.compare(): State {
            kotlin.io.println("before applying rule")
            this.print()
            if (this.ruleId == rules.getValue(this.startWorkflow).size - 1) {
                kotlin.io.println("last one")
                return State(rules.getValue(this.startWorkflow)[this.ruleId][0], 0, this.parts)
            }
            var workRule = rules.getValue(this.startWorkflow)[this.ruleId]
            var ruleK = workRule[0].first()
            var compNumber = workRule[2].toInt()
            var nextState = this.parts.toMutableMap()
            if (workRule[1] == ">") {
                nextState[ruleK] = IntRange(this.parts.getValue(ruleK).start, compNumber)
                this.parts[ruleK] = IntRange(compNumber+1, this.parts.getValue(ruleK).last)
            }
            if (workRule[1] == "<") {
                nextState[ruleK] = IntRange(compNumber, this.parts.getValue(ruleK).last)
                this.parts[ruleK] = IntRange(this.parts.getValue(ruleK).start, compNumber-1)
            }
            queue.add(State(this.startWorkflow, this.ruleId + 1, nextState))
            println("after applying the rule")
            this.print()
            kotlin.io.println("to queue:")
            State(this.startWorkflow, this.ruleId + 1, nextState).print()
            return State(workRule[3], 0, this.parts)
        }

        while (queue.isNotEmpty()) {
            var currentState = queue.removeFirst()
            while (true) {
//                currentState.print()
                if (currentState.startWorkflow == "R") break
                if (currentState.startWorkflow == "A") {accepted.add(currentState.parts); break}
                currentState = currentState.compare()
            }
        }

        println(accepted)
        println(accepted.map { it.map {c -> (c.value.last - c.value.first + 1).toLong() }.reduce{acc, elem -> (acc*elem)} }.sum())
        return 0
    }


    // test if implementation meets criteria from the description, like:
    val testInput = "Day19_test"
//    check(part1(testInput) == 136)
//    part1(testInput).println()
    part2(testInput).println()

    val input = "Day19"
    // test if implementation meets criteria from the description, like:
//    part1(input).println()
    part2(input).println()
}
