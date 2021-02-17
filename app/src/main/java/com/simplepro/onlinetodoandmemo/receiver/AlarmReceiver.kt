package com.simplepro.onlinetodoandmemo.receiver

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import com.google.firebase.auth.FirebaseAuth
import com.simplepro.onlinetodoandmemo.R
import com.simplepro.onlinetodoandmemo.activity.MainActivity
import java.text.SimpleDateFormat
import java.util.*

class AlarmReceiver : BroadcastReceiver() {

    lateinit var notificationManager : NotificationManager
    lateinit var notificationChannel : NotificationChannel
    lateinit var builder : Notification.Builder
    private val channelId = "com.simplepro.secondtodoandmemo"
    private val description = "Test notification"

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("TAG", "receive")
        if(FirebaseAuth.getInstance().currentUser != null)
        {

            if(intent!!.hasExtra("todoText"))
            {
                Log.d("TAG", "hasExtra")
                val todoText = intent!!.getStringExtra("todoText")
                val contentView = RemoteViews(context!!.packageName, R.layout.todo_notification_layout)
                val now = System.currentTimeMillis()
                val mDate = Date(now)
                val simpleDate = SimpleDateFormat("hh:mm")
                val getTime = simpleDate.format(mDate)

                contentView.setTextViewText(R.id.notificationTodoTitleTextView, todoText)
                contentView.setTextViewText(R.id.timeTextViewNotification, getTime)
                notificationManager = context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

                val intent = Intent(context, MainActivity::class.java)
                    .setAction(Intent.ACTION_MAIN)
                    .addCategory(Intent.CATEGORY_LAUNCHER)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                val pendingIntent = PendingIntent.getActivities(context, 0, arrayOf(intent), PendingIntent.FLAG_UPDATE_CURRENT)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    notificationChannel = NotificationChannel(channelId, description, NotificationManager.IMPORTANCE_HIGH)
                    notificationChannel.enableLights(true)
                    notificationChannel.enableVibration(true)
                    notificationManager.createNotificationChannel(notificationChannel)

                    builder = Notification.Builder(context, channelId)
                        .setContent(contentView)
                        .setContentIntent(pendingIntent)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher))
                        .setContentIntent(pendingIntent)
                } else {
                    builder = Notification.Builder(context)
                        .setContent(contentView)
                        .setContentIntent(pendingIntent)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher))
                        .setContentIntent(pendingIntent)
                }
                notificationManager.notify(1234, builder.build())
            }
        }
    }
}