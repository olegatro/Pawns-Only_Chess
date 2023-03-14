fun main() {
    var max: Int = 0

    do {
        val number: Int = readln().toInt()

        if (number > max) {
            max = number
        }
    } while (number != 0)

    println(max)
}