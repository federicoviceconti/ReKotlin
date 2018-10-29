package rekotlin.redux

class Action<out T, out R>(val type: T, val data: R)