fun main() {
    val input: String = readln()
    val vowelList: List<String> = listOf("a", "e", "i", "o", "u", "y")
    val resultList: MutableList<String> = mutableListOf()

    for (symbol in input) {
        if (resultList.size < 2) {
            resultList.add(symbol.toString())
            continue
        }

        var hasTypeMatch: Boolean = false

        if (vowelList.indexOf(symbol.toString()) != -1
                && vowelList.indexOf(resultList[resultList.lastIndex]) != -1
                && vowelList.indexOf(resultList[resultList.lastIndex - 1]) != -1
        ) {
            hasTypeMatch = true
        }

        if (vowelList.indexOf(symbol.toString()) == -1
                && vowelList.indexOf(resultList[resultList.lastIndex]) == -1
                && vowelList.indexOf(resultList[resultList.lastIndex - 1]) == -1
        ) {
            hasTypeMatch = true
        }

        if (hasTypeMatch) {
            if (vowelList.indexOf(resultList[resultList.lastIndex]) != -1) {
                resultList.add("b")
            } else {
                resultList.add(vowelList.random())
            }
        }

        resultList.add(symbol.toString())
    }

    println(resultList.size - input.length)
}