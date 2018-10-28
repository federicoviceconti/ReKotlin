package test

import rekotlin.*

enum class CountType {
    INCREMENT, DECREMENT
}

fun main(args: Array<String>) {
    TestClass().main()
}

class TestClass: Store.StoreChangeListener<Int> {
    override fun dataChanged(prevData: State<Int>, currentData: State<Int>) {
        println("CurrentData=(prev: ${prevData.myData}, current: ${currentData.myData}) -- ${this.javaClass.simpleName}")
    }

    fun main() {
        val initState = State(0)
        val store = Store.getInstance<CountType, Int>(initState)
        store.subscribe(this)
        val otherStore = Store.getInstance<CountType, Int>(initState)
        otherStore.subscribe(this)
        val provider = ReduxProvider(store)

        val reducer = CountReducer()
        store.combineReducers(arrayListOf(reducer))

        store.dispatch(Action(CountType.DECREMENT, data = 1))
        println("Data: ${provider.store.data}")

        otherStore.dispatch(Action(CountType.DECREMENT, data = 1))
        println("Data: ${provider.store.data}")

        store.dispatch(Action(CountType.INCREMENT, data = 1))
        println("Data: ${provider.store.data}")

        otherStore.dispatch(Action(CountType.INCREMENT, data = 1))
        println("Data: ${provider.store.data}")
    }
}

class CountReducer: Reducer<CountType, Int> {
    override fun handle(state: State<Int>, action: Action<CountType, Int>): State<Int> {
        val prevData = state.myData

        fun increment(value: Int): State<Int> {
            val currentData = prevData?.plus(value)
            return State(currentData)
        }

        when (action.type) {
            CountType.INCREMENT -> return increment(1)
            CountType.DECREMENT -> return increment(-1)
            else -> return state
        }
    }
}