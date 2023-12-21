import kotlin.math.abs
fun main() {
    // high = 1, low = 0
    // on = 1, off = 0
    // inputs -- map of input module and received signal

    class Module(var type: String, var output: List<String>, var inSignal: Int, var outSignal: Int,
                 var state: Int = 0, var inputs: MutableMap<String, String> = mutableMapOf(), var name: String)

    fun Module.currentState(): String {
        return "${this.type} ${this.output} ${this.inSignal} ${this.outSignal} ${this.state} ${this.inputs}"
    }


    fun part1(input: List<String>): Long {

        var modules = mutableMapOf<String, Module>()
        var listConjunction = mutableListOf<String>()

        for (line in input) {
            var (nm, output) = line.split(" -> ")
            if (nm == "broadcaster") {
                modules[nm] = Module(type = "broadcaster", output = output.split(", "), inSignal = 0, outSignal = 0, name = nm)
            }
            if (nm.startsWith('%')) {
                modules[nm.drop(1)] = Module(type = "flipflop", state = 0, output = output.split(", "),
                    inSignal = 0, outSignal = 0, name = nm.drop(1))
            }
            if (nm.startsWith('&')) {
                modules[nm.drop(1)] = Module(type = "conjunction", output = output.split(", "),
                    outSignal = 0, inSignal = 0, inputs = mutableMapOf(), name=nm.drop(1))
                listConjunction.add(nm.drop(1))
            }
        }

        for (conj in listConjunction) {
            modules.getValue(conj).inputs?.plusAssign(modules.filter { it.value.output.contains(conj) }.keys.associateWith { "0" })
        }

//        modules.map { println("${it.key} ${it.value.currentState()}") }

        var startSignal = listOf("button", "0", "broadcaster")

        var queueSignal = ArrayDeque<List<String>>()
        queueSignal.add(startSignal)

        var countLows = 0
        var countHighs = 0

        fun Module.process(signal: List<String>) {
            if (this.type == "broadcaster") {
                    this.output.map { queueSignal.add(listOf(this.name, signal[1].also { if (signal[1] == "1") countHighs += 1 else countLows += 1 }, it))}
            }
            if (this.type == "flipflop") {
                if (signal[1] == "0") {
                    this.output.map { queueSignal.add(listOf(this.name, if (this.state == 0) "1".also { countHighs+=1 } else "0".also { countLows+=1 }, it)) }
                    this.state = if (this.state == 0) 1 else 0
                }
            }
            if (this.type == "conjunction") {
                this.inputs[signal[0]] = signal[1]
                this.output.map { queueSignal.add(listOf(this.name, if (this.inputs.values.all { c -> c == "1" }) "0".also { countLows+=1 } else "1".also { countHighs+=1 }, it)) }
            }
        }

        var countPush = 0
        while (countPush < 1000) {
            countLows += 1
            while (queueSignal.isNotEmpty()) {
                startSignal = queueSignal.removeFirst()
                if (startSignal[2] in modules.keys) {
                    modules.getValue(startSignal[2]).process(signal = startSignal)
                }
            }
            startSignal = listOf("button", "0", "broadcaster")
            countPush += 1
            queueSignal.add(startSignal)
        }

        return countLows.toLong()*countHighs.toLong()
    }


    fun part2(input: List<String>): Int {
        var modules = mutableMapOf<String, Module>()
        var listConjunction = mutableListOf<String>()

        for (line in input) {
            var (nm, output) = line.split(" -> ")
            if (nm == "broadcaster") {
                modules[nm] = Module(type = "broadcaster", output = output.split(", "), inSignal = 0, outSignal = 0, name = nm)
            }
            if (nm.startsWith('%')) {
                modules[nm.drop(1)] = Module(type = "flipflop", state = 0, output = output.split(", "),
                    inSignal = 0, outSignal = 0, name = nm.drop(1))
            }
            if (nm.startsWith('&')) {
                modules[nm.drop(1)] = Module(type = "conjunction", output = output.split(", "),
                    outSignal = 0, inSignal = 0, inputs = mutableMapOf(), name=nm.drop(1))
                listConjunction.add(nm.drop(1))
            }
        }

        for (conj in listConjunction) {
            modules.getValue(conj).inputs?.plusAssign(modules.filter { it.value.output.contains(conj) }.keys.associateWith { "0" })
        }

//        modules.map { println("${it.key} ${it.value.currentState()}") }

        var startSignal = listOf("button", "0", "broadcaster")

        var queueSignal = ArrayDeque<List<String>>()
        queueSignal.add(startSignal)

        var countLows = 0
        var countHighs = 0

        var rxflag = 0

        fun Module.process(signal: List<String>, countPush: Int) {
            if (this.type == "broadcaster") {
                this.output.map { queueSignal.add(listOf(this.name, signal[1].also { if (signal[1] == "1") countHighs += 1 else countLows += 1 }, it))
                    .also { if ((queueSignal.last()[1] == "0")&&(queueSignal.last()[2] == "rx")) rxflag = 1 }}
            }
            if (this.type == "flipflop") {
                if (signal[1] == "0") {
                    this.output.map { queueSignal.add(listOf(this.name, if (this.state == 0) "1".also { countHighs+=1 } else "0".also { countLows+=1 }, it))
                        .also { if ((queueSignal.last()[1] == "0")&&(queueSignal.last()[2] == "rx")) rxflag = 1 }}
                    this.state = if (this.state == 0) 1 else 0
                }
            }
            if (this.type == "conjunction") {
                this.inputs[signal[0]] = signal[1]
                this.output.map { queueSignal.add(listOf(this.name, if (this.inputs.values.all { c -> c == "1" }) "0".also { countLows+=1 } else "1".also { countHighs+=1 }, it))
                    .also { if ((queueSignal.last()[1] == "0")&&(queueSignal.last()[0] in listOf("rx", "sl", "rt", "fv", "gk"))) kotlin.io.println("$countPush, ${queueSignal.last()}") }}

            }
        }

//        var queuePrinting = ArrayDeque<String>()
//        queuePrinting.addAll(modules.getValue("broadcaster").output)
//        while (queuePrinting.isNotEmpty()){
//            var elem = queuePrinting.removeFirst()
////            if (elem == "rx") break
//
//            println("$elem ${modules.getValue(elem).currentState()}")
//            if ("rx" in modules.getValue(elem).output) break
//            queuePrinting.addAll(modules.getValue(elem).output)
//        }

//        modules.map { println("${it.key} ${it.value.currentState()}") }

        var countPush = 0
        while (countPush < 20000) {
            countLows += 1
            while (queueSignal.isNotEmpty()) {
                startSignal = queueSignal.removeFirst()
//                println(startSignal)
//                println(rxflag)
                if (startSignal[2] in modules.keys) {
                    modules.getValue(startSignal[2]).process(signal = startSignal, countPush)
//                    modules.map { if (it.key == "hj") println("${it.key} ${it.value.currentState()}") }

                }
            }
//            println("\n\n")
//            modules.map { if (it.key == "hj") println("${it.key} ${it.value.currentState()}") }
            startSignal = listOf("button", "0", "broadcaster")
            countPush += 1
            queueSignal.add(startSignal)
        }

        return countPush
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day20_test")
//    check(part1(testInput) == 136)
//    part1(testInput).println()
//    part2(testInput).println()

    val input = readInput("Day20")
    // test if implementation meets criteria from the description, like:
//    part1(input).println()
    part2(input).println()
}
