fun getUpperAlphabetList(): MutableList<String> {
    val list: MutableList<String> = mutableListOf()
    for (symbol in 'A'..'Z') list.add(symbol.toString())

    return list
}

fun getLowerAlphabetList(): MutableList<String> {
    val list: MutableList<String> = mutableListOf()
    for (symbol in 'a'..'z') list.add(symbol.toString())

    return list
}

fun getDigitAlphabetList(): MutableList<String> {
    val list: MutableList<String> = mutableListOf()
    for (symbol in '0'..'9') list.add(symbol.toString())

    return list
}

fun getAlphabetList(upperCount: Int, lowerCount: Int, digitCount: Int): MutableList<MutableList<String>> {
    val alphabetList: MutableList<MutableList<String>> = mutableListOf()

    if (upperCount > 0) alphabetList.add(getUpperAlphabetList())
    if (lowerCount > 0) alphabetList.add(getLowerAlphabetList())
    if (digitCount > 0) alphabetList.add(getDigitAlphabetList())

    if (upperCount == 0 && lowerCount == 0 && digitCount == 0) {
        alphabetList.add(getUpperAlphabetList())
        alphabetList.add(getLowerAlphabetList())
        alphabetList.add(getDigitAlphabetList())
    }

    return alphabetList
}

const val UPPER_RULE_INDEX = 0
const val LOWER_RULE_INDEX = 1
const val DIGIT_RULE_INDEX = 2
const val LENGTH_RULE_INDEX = 3

fun main() {
    val rules = readln().split(" ")

    var upperCount: Int = rules[UPPER_RULE_INDEX].toInt()
    var lowerCount: Int = rules[LOWER_RULE_INDEX].toInt()
    var digitCount: Int = rules[DIGIT_RULE_INDEX].toInt()
    val passwordLength: Int = rules[LENGTH_RULE_INDEX].toInt()

    val passwordList: MutableList<String> = mutableListOf()

    while (passwordList.size < passwordLength) {
        val alphabetList = getAlphabetList(upperCount, lowerCount, digitCount)

        val symbol = alphabetList.random().random()

        if (passwordList.size == 0 || symbol != passwordList.last()) {
            passwordList.add(symbol)

            if (symbol in getUpperAlphabetList() && upperCount > 0) upperCount--
            if (symbol in getLowerAlphabetList() && lowerCount > 0) lowerCount--
            if (symbol in getDigitAlphabetList() && digitCount > 0) digitCount--
        }

        alphabetList.clear()
    }

    println(passwordList.joinToString(""))
}
