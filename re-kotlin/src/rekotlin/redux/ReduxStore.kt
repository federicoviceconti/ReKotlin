package rekotlin.redux

interface ReduxStore<T, R> {
    fun dispatch(actionToDispatch: Action<T, R>)
    fun combineReducers(reducers: List<Reducer<T, R>>)
}