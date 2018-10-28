package rekotlin

class State<R>(var myData: R) {
    fun setState(data: R) { this.myData = data }

    override fun toString(): String {
        return "State(myData=$myData)"
    }
}