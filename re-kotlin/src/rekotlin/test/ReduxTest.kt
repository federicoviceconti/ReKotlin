package rekotlin.test

import org.junit.Before
import org.junit.Test
import rekotlin.redux.*

enum class CountType {
    INCREMENT, DECREMENT
}

class ReduxTest {
    lateinit var test: TestClass
    lateinit var main: MainClass

    @Before
    fun setUp() {
        main = MainClass()
        test = TestClass()
    }

    @Test
    fun incrementing() {
        test.onClickIncrement()
        assert(test.counter == 1)
    }

    @Test
    fun decrementing() {
        test.onClickDecrement()
        assert(test.counter == 0)
    }
}

class MainClass {
    private val store: Store<CountType, Int>
    private val provider: ReduxProvider<CountType, Int>

    init {
        val initState = State(0)
        store = Store.getInstance(initState)
        store.combineReducers(arrayListOf(CountReducer()))
        provider = ReduxProvider(store)
    }
}

class TestClass: Store.StoreChangeListener<Int> {
    private val store: Store<CountType, Int> = Store.getInstance(rekotlin.redux.State(0))
    var counter = 0

    init {
        store.subscribe(this)
    }

    override fun dataChanged(prevData: State<Int>, currentData: State<Int>) {
        counter = currentData.myData
        println("CurrentData=(prev: ${prevData.myData}, current: ${currentData.myData}) -- ${this.javaClass.simpleName}")
    }

    fun onClickIncrement() {
        store.dispatch(Action(CountType.INCREMENT, 1))
    }

    fun onClickDecrement() {
        store.dispatch(Action(CountType.DECREMENT, -1))
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