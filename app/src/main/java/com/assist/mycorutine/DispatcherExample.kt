package com.assist.mycorutine

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking

class DispatcherExample {
    val TAG = "TAG"

    /**
     * Coroutines은 다양한 Dispathcer를 가지고 있다.
     *
     * 아래 예시를 참고해보자.
     *
     * Default는 코어 수에 비례하는 스레들 풀에서 수행한다.
     * - 복잡한 작업을 주로 수행하며, 코어 수보다 많은 쓰레드를 만들면
     * 스위칭 리소스가 더 낭비 되기 때문에 같은 갯수로 작동한다. -
     *
     * IO는 코어 수 보다 훨씬 많은 스레드를 가지는 스레드 풀이다.
     * Unconfined는 어디에도 속하지 않고, 부모의 스레드에서 수행된다.
     * newSingleThreadContext는 항상 새로운 스레드를 만든다.
     *
     * 또한, 코루틴 디스패쳐는 async, withContext 등 다양한 부분에서 사용할 수 있다.
     * **/
    fun runLaunchDispatcher() = runBlocking<Unit> {
        launch(Dispatchers.Unconfined) { // 해당 코루틴은 어디에서 실행되는 지 예측할 수 없다.ㅍㅍ
            Log.d(TAG, "Unconfined / ${Thread.currentThread().name}")
        }

        launch(Dispatchers.IO) {//해당 코루틴은 DefaultDispatcher가 실행 시켰다.
            Log.d(TAG, "IO / ${Thread.currentThread().name}")
        }

        launch(newSingleThreadContext("Fast Campus")) {
            Log.d(TAG, "newSingleThread / ${Thread.currentThread().name}")
        }

        launch(Dispatchers.Default) {//해당 코루틴은 DefaultDispatcher가 실행 시켰다.
            Log.d(TAG, "Default / ${Thread.currentThread().name}")
        }
    }

    fun runAsyncDispatcher() = runBlocking<Unit> {
        async(Dispatchers.Unconfined) { // 해당 코루틴은 어디에서 실행되는 지 예측할 수 없다.
            Log.d(TAG, "Unconfined / ${Thread.currentThread().name}")
            delay(100L) // 위 로그는 main에서 출력되지만, 아래는 Default에서 작동한다.
            Log.d(TAG, "Unconfined / ${Thread.currentThread().name}")
        }

        async(Dispatchers.IO) {//해당 코루틴은 DefaultDispatcher가 실행 시켰다.
            Log.d(TAG, "IO / ${Thread.currentThread().name}")
        }

        async(newSingleThreadContext("Fast Campus")) {
            Log.d(TAG, "newSingleThread / ${Thread.currentThread().name}")
        }

        async(Dispatchers.Default) {//해당 코루틴은 DefaultDispatcher가 실행 시켰다.
            Log.d(TAG, "Default / ${Thread.currentThread().name}")
        }
    }
}