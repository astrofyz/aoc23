fun main() {
    class Tile(var crd: List<Int>, var countDir: Int, var heat: Int, var dir: List<Int>)

    fun Tile.state(): List<Int> {
        return listOf<Int>(this.crd[0], this.crd[1], this.countDir, this.dir[0], this.dir[1])
    }

    fun part1(input: List<String>): Int {
        var height = input.size
        var width = input.first().length

        var start = listOf(0, 0)

        var allTiles = mutableMapOf<List<Int>, Int>()

        for (i in 0 until height){
            for (j in 0 until width) {
                for (dir in listOf(listOf(0, 1), listOf(0, -1), listOf(1, 0), listOf(-1, 0))) {
                    for (countDir in listOf(0, 1, 2)) {
                        allTiles[Tile(crd=listOf(i, j), countDir=countDir, heat = 1000001, dir=dir).state()] = 1000001
                        if ((i == 0)&&(j == 0)) {
                            allTiles[Tile(crd=listOf(i, j), countDir=countDir, heat = 1000001, dir=dir).state()] = input[0][0].toString().toInt()
                        }
                    }
                }
            }
        }

//        allTiles = allTiles.filterKeys { !((it[0] == 0)&&(it[1] == 0)) }.toMutableMap()
//
//        allTiles[Tile(crd = listOf(0, 0), countDir = 0, heat = input[0][0].toString().toInt(), dir = listOf(0, 1)).state()] = input[0][0].toString().toInt()
//        allTiles[Tile(crd = listOf(0, 0), countDir = 0, heat = input[0][0].toString().toInt(), dir = listOf(1, 0)).state()] = input[0][0].toString().toInt()



        var completedTiles = mutableListOf<List<Int>>()

        fun Tile.neighbours(): MutableList<Tile> {
            var nghb = mutableListOf<Tile>()
            for (dir in listOf(listOf(0, 1), listOf(0, -1), listOf(1, 0), listOf(-1, 0))) {
                // check that I'm not doing more than 3 steps
                if ((dir == this.dir)&&(this.countDir == 2)) continue
                //check that I'm not going back
                if (dir[0]*this.dir[0] + dir[1]*this.dir[1] == -1) continue
                // check that new Tile is within the field
                if ((this.crd[0]+dir[0] < height)&&(this.crd[0]+dir[0] >= 0)&&(this.crd[1]+dir[1] < width)&&(this.crd[1]+dir[1] >= 0)) {
                    nghb.add(Tile(crd = listOf(this.crd[0]+dir[0], this.crd[1]+dir[1]),
                        heat=this.heat + input[this.crd[0]+dir[0]][ this.crd[1]+dir[1]].toString().toInt(),
                        dir = dir,
                        countDir = when {
                            dir == this.dir -> this.countDir + 1
                            else -> 0
                        }))
                    }
                }
//            nghb.map { kotlin.io.println("${it.key} previous ${it.value.previous} countDir ${it.value.countDir} heat ${it.value.heat} dir ${it.value.dir}") }
            return nghb
        }

        var queueTile = allTiles.filterValues { it < 1000000 }.toMutableMap()

        while (queueTile.isNotEmpty()) {
//            if (allTiles.filterKeys { it !in completedTiles }.filterValues { it < 1000000 }.isEmpty()) break
//            println(allTiles.filterValues { it < 1000000 }.size)
//            println(queueTile.size)

//            var currentState = allTiles.filterKeys { it !in completedTiles }.filterValues { it < 1000000 }.toList().minBy { it.second }.first // state of min heat
            var currentState = queueTile.toList().minBy { it.second }.first // state of min heat
            var currentTile = Tile(
                crd = listOf(currentState[0], currentState[1]), countDir = currentState[2],
                dir = listOf(currentState[3], currentState[4]), heat = allTiles.getValue(currentState)
            )

            for (nextTile in currentTile.neighbours()) {
//                println(nextTile.state())
                if (nextTile.state() !in completedTiles) {
//                    println("add to allTiles")
                    allTiles[nextTile.state()] = minOf(nextTile.heat, allTiles.getValue(nextTile.state()))
                    queueTile[nextTile.state()] = minOf(nextTile.heat, allTiles.getValue(nextTile.state()))
                }
            }
            completedTiles.add(currentState)
            queueTile.remove(currentState)
        }

        println(allTiles.filter { (it.key[0] == height - 1)&&(it.key[1] == width - 1) })

        return 0
    }


    fun part2(input: List<String>): Int {
        var height = input.size
        var width = input.first().length

        var start = listOf(0, 0)

        var allTiles = mutableMapOf<List<Int>, Int>()

        for (i in 0 until height){
            for (j in 0 until width) {
                for (dir in listOf(listOf(0, 1), listOf(0, -1), listOf(1, 0), listOf(-1, 0))) {
                    for (countDir in 0 .. 9) {
                        allTiles[Tile(crd=listOf(i, j), countDir=countDir, heat = 1000001, dir=dir).state()] = 1000001
                        if ((i == 0)&&(j == 0)) {
                            allTiles[Tile(crd=listOf(i, j), countDir=countDir, heat = 1000001, dir=dir).state()] = input[0][0].toString().toInt()
                        }
                    }
                }
            }
        }


        var completedTiles = mutableListOf<List<Int>>()

        fun Tile.neighbours(): MutableList<Tile> {
            var nghb = mutableListOf<Tile>()
            for (dir in listOf(listOf(0, 1), listOf(0, -1), listOf(1, 0), listOf(-1, 0))) {
                // check that I'm not doing more than 9 steps
                if ((dir == this.dir)&&(this.countDir == 9)) continue
                // check that I'm not turning earlier than after 4 steps
                if ((dir != this.dir)&&(this.countDir < 3)) continue
                //check that I'm not going back
                if (dir[0]*this.dir[0] + dir[1]*this.dir[1] == -1) continue
                // check that new Tile is within the field
                if ((this.crd[0]+dir[0] < height)&&(this.crd[0]+dir[0] >= 0)&&(this.crd[1]+dir[1] < width)&&(this.crd[1]+dir[1] >= 0)) {
                    nghb.add(Tile(crd = listOf(this.crd[0]+dir[0], this.crd[1]+dir[1]),
                        heat=this.heat + input[this.crd[0]+dir[0]][ this.crd[1]+dir[1]].toString().toInt(),
                        dir = dir,
                        countDir = when {
                            dir == this.dir -> this.countDir + 1
                            else -> 0
                        }))
                }
            }
//            nghb.map { kotlin.io.println("${it.key} previous ${it.value.previous} countDir ${it.value.countDir} heat ${it.value.heat} dir ${it.value.dir}") }
            return nghb
        }

        var queueTile = allTiles.filterValues { it < 1000000 }.toMutableMap()

        while (queueTile.isNotEmpty()) {
//            if (allTiles.filterKeys { it !in completedTiles }.filterValues { it < 1000000 }.isEmpty()) break
//            println(allTiles.filterValues { it < 1000000 }.size)
//            println(queueTile.size)

//            var currentState = allTiles.filterKeys { it !in completedTiles }.filterValues { it < 1000000 }.toList().minBy { it.second }.first // state of min heat
            var currentState = queueTile.toList().minBy { it.second }.first // state of min heat
            var currentTile = Tile(
                crd = listOf(currentState[0], currentState[1]), countDir = currentState[2],
                dir = listOf(currentState[3], currentState[4]), heat = allTiles.getValue(currentState)
            )

            for (nextTile in currentTile.neighbours()) {
//                println(nextTile.state())
                if (nextTile.state() !in completedTiles) {
//                    println("add to allTiles")
                    allTiles[nextTile.state()] = minOf(nextTile.heat, allTiles.getValue(nextTile.state()))
                    queueTile[nextTile.state()] = minOf(nextTile.heat, allTiles.getValue(nextTile.state()))
                }
            }
            completedTiles.add(currentState)
            queueTile.remove(currentState)
        }

        println(allTiles.filter { (it.key[0] == height - 1)&&(it.key[1] == width - 1)&&(it.key[2] >= 3)&&(it.key[2]<9) }.filterValues { it < 1000000 }.toList().sortedBy { it.second })

        return 0
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day17_test")
//    check(part1(testInput) == 136)
    part1(testInput).println()
    part2(testInput).println()

    val input = readInput("Day17")
    // test if implementation meets criteria from the description, like:
//    part1(input).println()
//    part2(input).println()
}
