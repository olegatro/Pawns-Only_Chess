fun main() {
    val report = readLine()!!
    var isMatch: Boolean = false

    for (i in 0..9) {
        isMatch = "$i wrong answers?".toRegex().matches(report)

        if (isMatch) break
    }

    println(isMatch)
}