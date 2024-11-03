package com.assist.mycorutine

import android.util.Log
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlin.random.Random
import kotlin.system.measureTimeMillis
import kotlin.time.measureTime

class MySuspend {
    val TAG : String = "TAG"
    suspend fun getRandom1(): Int {
        delay(1000L)
        return Random.nextInt(0, 500)
    }

    suspend fun getRandom2(): Int {
        delay(1000L)
        return Random.nextInt(0, 500)
    }

    suspend fun myAsync() {
        val runTime = measureTimeMillis {
            runBlocking {
                //launch는 Join cancle 등을 수행할 수 있다.
                //async는 결과를 받아야할 때 사용한다.
                /**
                 * async는 await()과 세트이며, 벨류 1과 2가 코루틴이 시작하면
                 * 동시에 작동한다.
                 *
                 * 이후 await()으로 데이터가 필요한 부분에 선언해주면 데이터가 날아왔을 때
                 * 올바르게 작동한다.
                 *
                 * 만약에 똑같이 async를 하지만 늦게 시작하고싶다면?
                 * async(start = CoroutineStart.LAZY) {TODO} 와 함께 start로 진행한다.
                 *
                 * 코루틴은 자식 코루틴에서 Error가 발생하면 부모 코루틴까지 종료되어 코루틴이 원하는
                 * 방향으로 작동하지 않을수 있다.
                 *
                 * 이는 코루틴이 계층적 구조이기 때문이다.
                 * 그래서 만약 코루틴이 위와 같은 경우로 종료된다.
                 * try {} catch로 관리한다.
                 *
                 * 또는 try {} finally {}로 관리한다.
                 * **/
                val value1 = async { getRandom1() }
                val value2 = async { getRandom2() }
                val value3 = async(start = CoroutineStart.LAZY) { getRandom2() }

                /**
                 * 위와 같은 코드에서 value3는 바로 예약 큐에 들어가지 않는다. value3.start()와 같이 명시적으로 큐에 넣어줘야한다.
                 * */

                Log.d(
                    TAG,
                    "${value1.await()} + ${value2.await()} + = + ${value1.await() + value2.await()}"
                )
            }
        }

        Log.d(TAG, "it's complete$runTime")
    }
}