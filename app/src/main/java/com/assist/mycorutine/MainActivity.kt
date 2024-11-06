package com.assist.mycorutine

import android.health.connect.datatypes.units.Mass
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.util.concurrent.atomic.AtomicInteger

class MainActivity : AppCompatActivity() {
    val TAG : String = "TAG_TEST"
    lateinit var mySuspend : MySuspend
    lateinit var dispatcherExample: DispatcherExample
    lateinit var myJobExample: JobExample
    lateinit var massive: Massive
    var count = AtomicInteger()
    /**count가 0과 같은 단순 Integer 형이면 Thread safe 하지 않다.**/
    /**하지만 위 처럼 AtomicInteger()를 사용하면 보다 안전한 코드를 작성할 수 있다.**/
    val mutex = Mutex()
    var count2 = 0
    /**또는 위와 같은 뮤텍스를 사용하여 뮤텍스 블록으로 간편하게 처리할 수 있다.**/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mySuspend = MySuspend()
        dispatcherExample = DispatcherExample()
        myJobExample = JobExample()
        massive = Massive()

        runBlocking {
            testMassiveRun()
        }
    }

    private fun testSuspend() {
        runBlocking { mySuspend.myAsync() }
    }

    private fun testDispatcher(mode : Int) {
        when(mode) {
            1 -> dispatcherExample.runAsyncDispatcher()
            2 -> dispatcherExample.runLaunchDispatcher()
        }
    }

    private fun testMyJob() {
        myJobExample.myJobExample()
    }

    private suspend fun testMassiveRun() {
        withContext(Dispatchers.Default) {
            //withContext는 주로 IO 작업을 처리할 때 사용하며 순차처리만 가능하다.
            //즉 async{} await()을 사용하는 게 더 좋은 선택인 것 같다.
            massive.massiveRun {
                count.incrementAndGet()
                //count는 단순 Integer 형이면 여러 쓰레드에서 동시에 접근하는 문제가 발생할 수 있다.
                mutex.withLock {
                    count2++
                }
            }
        }

        Log.d(TAG, "$count / $count2")
    }

    /**
     * 코루틴에서 CoroutineScope.functionName() = actor<Class> {
     *      for (msg in channel) {
     *          when(msg){
     *              is _ -> Todo()
     *              is _ -> Todo()
     *          }
     *      }
     * }
     *
     * 위와 같은 자료형 구조를 사용하면 아래와 같이
     * val count = functgionName()
     * count.send(Message) 를 넣어주면 when 문에서 과정을 체크하여 처리할 수 있다.
     *
     * 이는 멀티쓰레드 환경에서 올바른 접근을 관리할 수 있는 방법이다.
     * **/
}