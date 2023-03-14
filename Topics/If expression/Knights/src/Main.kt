import kotlin.math.abs

fun main() {
    val (x1, y1) = readln().split(" ")
    val (x2, y2) = readln().split(" ")

    if ((abs(y2.toInt() - y1.toInt()) == 1 && abs(x2.toInt() - x1.toInt()) == 2) || (abs(y2.toInt() - y1.toInt()) == 2 && abs(x2.toInt() - x1.toInt()) == 1)) {
        println("YES")
    } else {
        println("NO")
    }
}