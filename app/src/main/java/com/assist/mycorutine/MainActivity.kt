package com.assist.mycorutine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Delay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.withTimeoutOrNull

class MainActivity : AppCompatActivity() {
    val TAG : String = "TAG"
    lateinit var tv : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        runBlocking {
            doExceptionTest()
            dontFix()
            timeOut()
        }
    }

    suspend fun dontFix() = withContext(NonCancellable) {
        val job1 = launch {
            Log.d(TAG, "이 작업은 취소할 수 없습니다")
        }

        job1.cancel()
        /**
         * 해당 withContext(NonCancellable)은 작업을 취소할 수 없으며, 반드시 필요한
         * 코루틴에 사용하면 좋다.
         * **/
    }

    suspend fun timeOut() = runBlocking {
        withTimeout(500) {
            launch {
                delay(300)
                Log.d(TAG, "500 밀리 이상의 작업은 실행되지 않습니다.")
                //앱이 강제 종료된다.
            }
        }

        //withTimeoutOrNull() 해당 코드를 넣으면 TimeOut에 대한 예외 처리를 진행할 수 있다.
    }

    suspend fun doExceptionTest() = coroutineScope {
        val job1 = launch {
            try {
                delay(1000L)
                Log.d(TAG, "Hi")
            } finally {
                Log.d(TAG, "작업이 취소 되었어요!")
            }
        }
        delay(200L)
        job1.cancel()

        /**
         * 작업이 끝나거나 취소되면 호출 된다.
         * catch로 오류를 잡을 수 있다.
         * */
    }

    suspend fun doOneTwoThree() = coroutineScope {
        val job1 = launch {
            delay(1000L)
            Log.d(TAG, "3")
        }

        val job2 = launch {
            Log.d(TAG, "1")
        }

        val job3 = launch {
            delay(500L)
            Log.d(TAG, "2")
        }

        val job4 = launch(Dispatchers.Default) {
            for (i in 0 .. 5) {
                Log.d(TAG, i.toString() + "1")
                delay(400L)
                if (!this.isActive) {
                    break
                }
            }
        }

        delay(300L)
        job1.cancel() // job을 취소 할 수 있는 방법.
        job2.cancel()
        job3.cancel() // 800ms초 안에 job2/job3은 작동 하지만 1000L이 되기 전에 cancel되기 때문에 출력되지 않는다.
        job4.cancelAndJoin()
        Log.d(TAG, "4")

        //1 -> 2 -> 4 -> 5

        /**
         * 단, launch안에 Dispatchers.Default와 같이 다른 쓰레드에서 작업이 실행되는 코루틴은 cancel 할 수 없다.
         * 일반 launch 했을 때는 MainThread에서 작동한다.
         *
         * 위와 같은 코드를 코드를 cancel하려면 cancel()을 호출 하고 join()을 호출해 눈속임을 할 수 있다.
         * 즉, Log.d "4"는 join이 완전히 끝날 때 까지 기다리는 것이다.
         *
         * 하지만 위와 같은 건 너무 불편한 내용이다 이를 해결 하기 위해 cancel & join을 사용한다.
         * cancelAndJoin() <- 위와 같이 작성한다.
         *
         * 하지만 위 코드는 눈속임이다. 이를 해결 하기 위해 isActive를 사용해 cancel()을 호출했을 때
         * 코루틴을 종료 시킬 수 있다.
         * **/
    }
}