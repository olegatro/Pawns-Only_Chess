fun main() {
    val input: String = readln()
    val query: List<String> = input.substringAfter("?").split("&")

    var password: String = ""

    for (param in query) {
        var (key, value) = param.split("=")

        if (value.isEmpty()) {
            value = "not found"
        }

        if (key == "pass") {
            password = value
        }

        println("$key : $value")
    }

    if (password.isNotEmpty()) {
        println("password : $password")
    }
}