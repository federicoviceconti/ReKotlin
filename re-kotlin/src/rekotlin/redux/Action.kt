package rekotlin

class Action<out T, out R>(val type: T, val data: R)