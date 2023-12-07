import kotlin.math.pow
fun main() {

    class HandOfCards(var hand: String, var bid: Int, var type: Int)

    fun parseType(hand: String): Int {
        var occurences = hand.toCharArray().toList().groupingBy { it }.eachCount()
        if (occurences.size == 1) return 7
        if (occurences.values.sorted() == listOf(1, 4)) return 6
        if (occurences.values.sorted() == listOf(2, 3)) return 5
        if (occurences.values.sorted() == listOf(1, 1, 3)) return 4
        if (occurences.values.sorted() == listOf(1, 2, 2)) return 3
        if (occurences.values.sorted() == listOf(1, 1, 1, 2)) return 2
        if (occurences.size == 5) return 1
        return 0
    }

    fun parseTypeModified(hand: String): Int {
        var occurences = hand.toCharArray().toList().groupingBy { it }.eachCount()
        var modOccur = mutableListOf<Int>()
        if (('J' in occurences.keys)&&(occurences.size > 1)) {
            modOccur = occurences.filterKeys { it != 'J' }.values.sorted().toMutableList()
            modOccur[modOccur.size-1] = modOccur[modOccur.size-1] + occurences.getValue('J')
        }
        else {modOccur = occurences.values.sorted().toMutableList()}
        if (modOccur.size == 1) return 7
        if (modOccur == mutableListOf(1, 4)) return 6
        if (modOccur == mutableListOf(2, 3)) return 5
        if (modOccur == mutableListOf(1, 1, 3)) return 4
        if (modOccur == mutableListOf(1, 2, 2)) return 3
        if (modOccur == mutableListOf(1, 1, 1, 2)) return 2
        if (modOccur.size == 5) return 1
        return 0
    }

    fun HandOfCards.printHand() {
        println("hand = ${this.hand} bid = ${bid.toString()} type = ${type.toString()}")
    }

    class CustomCardComparator: Comparator<HandOfCards> {
        private val cardsRank = mapOf<Char, Int>('A' to 12, 'K' to 11, 'Q' to 10, 'J' to 9, 'T' to 8, '9' to 7,
            '8' to 6, '7' to 5, '6' to 4, '5' to 3, '4' to 2, '3' to 1, '2' to 0)

        override fun compare(o1: HandOfCards, o2: HandOfCards): Int {
            if (o1.type != o2.type) return o1.type - o2.type
            else {
                for (i in 0 until o1.hand.length) {
                    if (o1.hand[i] != o2.hand[i]) return cardsRank.getValue(o1.hand[i]) - cardsRank.getValue(o2.hand[i])
                }
            }
            return 0
        }
    }


    class CustomCardComparatorModified: Comparator<HandOfCards> {
        private val cardsRank = mapOf<Char, Int>('A' to 12, 'K' to 11, 'Q' to 10, 'T' to 8, '9' to 7,
            '8' to 6, '7' to 5, '6' to 4, '5' to 3, '4' to 2, '3' to 1, '2' to 0, 'J' to -1)

        override fun compare(o1: HandOfCards, o2: HandOfCards): Int {
            if (o1.type != o2.type) return o1.type - o2.type
            else {
                for (i in 0 until o1.hand.length) {
                    if (o1.hand[i] != o2.hand[i]) return cardsRank.getValue(o1.hand[i]) - cardsRank.getValue(o2.hand[i])
                }
            }
            return 0
        }
    }


    fun part1(input: List<String>): Long {
        var hands = input.map { HandOfCards(hand = it.split(' ')[0],
                                            bid = it.split(' ')[1].toInt(),
                                            type = parseType(it.split(' ')[0]))}


//        hands.sortedWith(compareHands())
        hands.sortedWith(CustomCardComparator()).mapIndexed { i, it -> it.printHand() }

        var sortHands = hands.sortedWith(CustomCardComparator()).mapIndexed {i, elem ->  elem.bid.toLong()*(i+1).toLong()}
        println(sortHands)

        return hands.sortedWith(CustomCardComparator()).mapIndexed {i, elem ->  elem.bid.toLong()*(i+1).toLong()}.reduce { acc, l -> acc + l }
    }


    fun part2(input: List<String>): Long {
        var hands = input.map { HandOfCards(hand = it.split(' ')[0],
            bid = it.split(' ')[1].toInt(),
            type = parseTypeModified(it.split(' ')[0]))}

//        hands.sortedWith(compareHands())
        hands.sortedWith(CustomCardComparatorModified()).mapIndexed { i, it -> it.printHand() }

        var sortHands = hands.sortedWith(CustomCardComparatorModified()).mapIndexed {i, elem ->  elem.bid.toLong()*(i+1).toLong()}
        println(sortHands)

        return hands.sortedWith(CustomCardComparatorModified()).mapIndexed {i, elem ->  elem.bid.toLong()*(i+1).toLong()}.reduce { acc, l -> acc + l }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    check(part2(testInput) == 6839.toLong())

    val input = readInput("Day07")
    // test if implementation meets criteria from the description, like:
//    part1(input).println()
    part2(input).println()
}
