package rekotlin

class Store<T, R> private constructor(var data: State<R>) : ReduxStore<T, R>{
    private lateinit var reducers: List<Reducer<T, R>>
    private lateinit var listener: StoreChangeListener<R>

    override fun dispatch(actionToDispatch: Action<T, R>) {
        for (reducer in reducers) {
            val prevData = State(data.myData)
            data = reducer.handle(data, actionToDispatch)
            listener.dataChanged(prevData, data)
        }
    }

    override fun combineReducers(reducers: List<Reducer<T, R>>) {
        this.reducers = reducers
    }

    companion object {
        private var INSTANCE: Any? = null

        fun <T, R> getInstance(initValue: State<R>): Store<T, R> {
            if(INSTANCE == null) {
                INSTANCE = Store<T, R>(initValue)
            }
            return Store.INSTANCE as Store<T, R>
        }
    }

    fun subscribe(listener: StoreChangeListener<R>) {
        this.listener = listener
    }

    interface StoreChangeListener<R> {
        fun dataChanged(prevData: State<R>, currentData: State<R>)
    }
}