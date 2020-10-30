package com.example.flightdroid

import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.flightdroid.notifs.NotifWork
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

object WorkUtil {

    private val applicationScope = CoroutineScope(Dispatchers.Default)

    fun launchWork(){

        applicationScope.launch {
            setUpRecurringWork()
        }

    }

    private fun setUpRecurringWork() {
        val myWorkRequest = PeriodicWorkRequestBuilder<NotifWork>(1, TimeUnit.DAYS)
            .build()

        WorkManager.getInstance().enqueueUniquePeriodicWork(
            NotifWork.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            myWorkRequest)
    }
}