package com.assist.mycorutine

import android.util.Log
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Delay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class JobExample {
    private val TAG = "TAG"
    /**
     * 코루틴은 계층적 구조를 가지고 있다.
     * 아래와 같은 예시로 살펴보자.
     * **/

    @OptIn(ExperimentalStdlibApi::class)
    fun myJobExample() = runBlocking { // 증부모
        val job = launch { // 부모
            launch(Job()) { // 자식?
                /**
                 * 만약, 위와 같이 Job을 생성하면 더 이상 부모 자식 관계가 성립하지 않는다.
                 * 즉, 해당 Job이 Exception이 발생해 중단 되도, 다른 launch안의 코루틴은 영향 받지 않는다.
                 * **/

                delay(2000L)
                Log.d(TAG, "1!")
            }

            launch(Dispatchers.IO + CoroutineName("Test")) {
                Log.d(TAG, coroutineContext[CoroutineName].toString() + " / " + coroutineContext[CoroutineDispatcher].toString())
                delay(500L)
                Log.d(TAG,"2!")
            }
        }
        delay(800L)
        job.cancelAndJoin()
        //또한, Job()은 새로운 부모에서 작동하기 때문에 아무리 cancelAndJoin()을 해줘도
        //Log에서는 "1!"가 출력된다. 즉, 취소되지 않는다.
    }
}