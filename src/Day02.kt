fun main() {

    class gameSet(var red: Int, var blue: Int, var green: Int) {
        init {
            red = 0
            blue = 0
            green = 0
        }
    }

//    fun parseBlueprint(input: String): MutableMap<String, MutableMap<String, Int>> {
//        val ruleMap = mutableMapOf<String, MutableMap<String, Int>>()
//        for (rule in input.split(":")[1].split(" Each ").subList(1, 5)){
//            ruleMap[rule.split(" ")[0]] = mutableMapOf<String, Int>()
//            val elems = rule.split(" ")
//            for ((index, elem) in elems.withIndex()){
//                if (elem.toIntOrNull() != null){
//                    ruleMap[rule.split(" ")[0]]?.set(elems[index+1].replace(".", ""), elem.toInt())
//                }
//            }
//        }
//        return ruleMap
//    }

//    fun State.print(){
//        println("oreRobot: ${this.oreRobot} clayRobot: ${this.clayRobot} obsRobot: ${this.obsidianRobot} geodeRobot: ${this.geodeRobot}")
//        println("ore: ${this.ore} clay: ${this.clay} obsidian: ${this.obsidian} geode: ${this.geode}; t = ${this.t}")
//        println("---------------------")
//    }


    fun parseGame(input: String): Int {
        var patternGame = mapOf("red" to 12, "green" to 13, "blue" to 14)
        var gameId = input.split(": ")[0].drop(5).toInt()
        var gameSets = input.split(": ")[1].split("; ")
        for (gmSet in gameSets) {
            println(gmSet.split(", "))
            var curPattern = gmSet.split(", ").map { it.split(" ")[1] to it.split(" ")[0].toInt() }.toMap()
            for (color in curPattern.keys) {
                if ((curPattern[color]!! > patternGame[color]!!)) return 0
            }
        }
        return gameId
    }


    fun multGame(input: String): Int {
        var gameId = input.split(": ")[0].drop(5).toInt()
        var gameSets = input.split(": ")[1].split("; ")
        var smallestSet = mutableMapOf("red" to 0, "blue" to 0, "green" to 0)

        for (gmSet in gameSets) {
            println(gmSet.split(", "))
            var curPattern = gmSet.split(", ").map { it.split(" ")[1] to it.split(" ")[0].toInt() }.toMap()
            for (color in curPattern.keys) {
                if ((curPattern[color]!! > smallestSet[color]!!)) smallestSet[color] = curPattern[color]!!
            }
        }
        return smallestSet.values.reduce{acc, elem -> elem*acc}
    }

    fun part1(input: List<String>): Int {
        return input.map { parseGame(it) }.reduce{acc, elem -> acc + elem}
    }

    fun part2(input: List<String>): Int {
        return input.map { multGame(it) }.reduce{acc, elem -> acc + elem}
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part2(testInput) == 2286)

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}
