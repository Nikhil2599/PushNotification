package com.example.fcmpushnotification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

const val channelId = "Notification_channel"
const val channelName = "com.example.fcmpushnotification"
class MyFirebaseMessagingService : FirebaseMessagingService() {

    //generate the notification
    //attach the notification with custom layout created
    //show the notification

    override fun onMessageReceived(remotemessage: RemoteMessage) {
        if(remotemessage.getNotification() != null)
            generateNotification(remotemessage.notification!!.title!!, remotemessage.notification!!.body!!)

    }

    fun getRemoteView(title: String, message: String): RemoteViews{
        val remoteView = RemoteViews("com.example.fcmpushnotification", R.layout.notification)
        remoteView.setTextViewText(R.id.title,title)
        remoteView.setTextViewText(R.id.message,message)
        remoteView.setImageViewResource(R.id.app_logo,R.drawable.img)

        return remoteView
    }

    fun generateNotification(title: String, message: String){
        val intent = Intent(this,MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingintent = PendingIntent.getActivity(this,0,intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE)

        //chanell id and chanell name
        var builder: NotificationCompat.Builder = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.img)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(1000,1000,1000,1000))
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingintent)

        builder = builder.setContent(getRemoteView(title, message))

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val notificationChannel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        notificationManager.notify(0,builder.build())
    }

}