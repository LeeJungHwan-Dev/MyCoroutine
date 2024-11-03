package com.assist.mycorutine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Switch
import android.widget.TextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Delay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.async
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
    lateinit var mySuspend : MySuspend
    lateinit var dispatcherExample: DispatcherExample
    lateinit var myJobExample: JobExample

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mySuspend = MySuspend()
        dispatcherExample = DispatcherExample()
        myJobExample = JobExample()

        testMyJob()
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
}