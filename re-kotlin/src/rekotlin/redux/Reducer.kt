package rekotlin.redux

interface Reducer<in T, R> {
    fun handle(state: State<R>, action: Action<T, R>): State<R>
}