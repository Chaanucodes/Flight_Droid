package com.example.flightdroid.notifs

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.flightdroid.R
import com.example.flightdroid.signup.SignUpActivity
import java.lang.Exception


const val CHANNEL_ID_OLD = "com_ID_FLIGHT"
const val CHANNEL_ID = "com_ID_A_FLIGHT"
const val notificationId = 1
const val REQ_CODE = 2

class NotifWork(appContext : Context, params : WorkerParameters) : CoroutineWorker(appContext, params){

    companion object {
        const val WORK_NAME = "com.example.amotion.work.ReminderNote_WORK"
    }

    override suspend fun doWork(): Result {

        try {
//            val random = Random(4).nextInt()
            createNoti()
        }catch (e : Exception){
            return Result.retry()
        }

        return Result.success()
    }

    private fun createNoti() {

        val contentIntent = Intent(applicationContext, SignUpActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            REQ_CODE,
            contentIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val largeImg = BitmapFactory.decodeResource(
            applicationContext.resources,
            R.drawable.ic_main_drw
        )

        val builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_drw)
            .setLargeIcon(largeImg)
            .setContentTitle("Write something")
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setContentText("Click to open app")
//            .setStyle(
//                NotificationCompat.BigTextStyle()
//                .bigText(
//                    "Click to open app"))
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .also {
                it.color =  applicationContext.resources.getColor(R.color.colorPrimary)
                it.setColorized(true)
            }

        with(NotificationManagerCompat.from(applicationContext)){
            notify(notificationId, builder.build())
        }

    }





}