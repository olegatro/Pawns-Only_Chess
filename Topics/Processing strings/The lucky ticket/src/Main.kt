fun main() {
    val ticket = readln()
    val ticketFirstHalf = ticket.substring(0, ticket.length / 2)
    val ticketLastHalf = ticket.substring(ticket.length / 2)
    var ticketFirstSum: Int = 0
    var ticketLastSum: Int = 0

    for (letter in ticketFirstHalf) {
        ticketFirstSum += letter.code
    }

    for (letter in ticketLastHalf) {
        ticketLastSum += letter.code
    }

    if (ticketFirstSum == ticketLastSum) {
        println("Lucky")
    } else {
        println("Regular")
    }
}