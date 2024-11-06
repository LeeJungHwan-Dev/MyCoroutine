package com.assist.mycorutine

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class Massive {
    /**
     * 아래 코드는 총 10만번의 코루틴 action() 함수를 실행 해주는 함수이다.
     * 하지만 여러 코루틴이 action()에 동시에 접근할 경우 데이터가 오염되는 일이 발생할 수 있다.
     * **/
    suspend fun massiveRun(action: suspend () -> Unit) {
        val n = 100
        val k = 1000

        coroutineScope {
            repeat(n) {
                launch {
                    repeat(k) {
                        action()
                    }
                }
            }
        }
    }
}