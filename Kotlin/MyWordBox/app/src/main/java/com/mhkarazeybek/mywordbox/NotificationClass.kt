package com.mhkarazeybek.mywordbox

import android.annotation.SuppressLint
import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import java.util.*

class NotificationClass: BroadcastReceiver() {
    var ingArray = ArrayList<String>()
    var trArray = ArrayList<String>()
    var size=10
    var notificationText="What did you do today for you?"
    val rnd= Random()
    var index=0
    private val CHANNEL_ID="com.mhkarazeybek.main2notification"
    var count=0
    var control=0
    var secound=0
    var minute=0

    @SuppressLint("NewApi")
    @Override
    override fun onReceive(context: Context?, intent: Intent?) {
        val hour=Calendar.getInstance()[11]
        secound=Calendar.getInstance()[13]
        val resultSecound=60-secound
        val gintent=intent
        ingArray=gintent!!.getStringArrayListExtra("IngArray")
        trArray=gintent!!.getStringArrayListExtra("TrArray")
        index=gintent!!.getIntExtra("index",0)
        size=ingArray.size

        minute=Calendar.getInstance()[12]


        val notificationIntent=Intent(context,Main2Activity::class.java)
        notificationIntent.putExtra("ing", ingArray[index])
        notificationIntent.putExtra("tr", trArray[index])
        notificationIntent.putExtra("info", "old")

        val stactBuilder= TaskStackBuilder.create(context)
        stactBuilder.addParentStack(MainActivity::class.java)
        stactBuilder.addNextIntent(notificationIntent)


        val pendingIntent=stactBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)

        val builder= Notification.Builder(context)

        if (ingArray.size!=0 && trArray.size!=0){
                if (((hour==8) ||(hour==10)||(hour==12)||(hour==14)||(hour==16)||(hour==18)||(hour==20)||(hour==22))&&(minute==0)){
                    notificationText=ingArray[index]+": "+trArray[index]
                }
                else if(((hour==9) ||(hour==11)||(hour==13)||(hour==15)||(hour==17)||(hour==19)||(hour==21)||(hour==23))&&(minute==0)){
                    notificationText="Do you know  '"+ingArray[index]+"' ?"
                }
                else{
                    notificationText="How is it going?"
                }
        }

        else{
            notificationText="What did you today for you?"
        }

        val argb=Color.BLUE

        val soundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notification=builder.setContentTitle(notificationText)
                .setContentText("Regularly add and test words")
                .setSmallIcon(R.drawable.notify)
                .setSound(soundUri)
                .setAutoCancel(true)
                .setColor(1000)
                .setShowWhen(true)
                .setLights(argb,0,1000)
                .setContentIntent(pendingIntent).build()

        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            builder.setChannelId(CHANNEL_ID)
        }

        val notificationManager=context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            val channel= NotificationChannel(CHANNEL_ID,"Notification", NotificationManager.IMPORTANCE_DEFAULT)
            channel.enableVibration(true)
            channel.lightColor= Color.BLUE

            notificationManager.createNotificationChannel(channel)
        }
        if ((hour<=23 && hour>7)&&(minute==0 && resultSecound<=2)){
            index=rnd.nextInt(size-0)
            notificationManager.notify(0,notification)
            control=1
        }
        val alarmManager: AlarmManager =context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val notificationIntent2=Intent(context,NotificationClass::class.java)
        notificationIntent2.putExtra("IngArray",ingArray)
        notificationIntent2.putExtra("TrArray",trArray)
        notificationIntent2.putExtra("index",index)

        val broadcast= PendingIntent.getBroadcast(context ,100,notificationIntent2, PendingIntent.FLAG_UPDATE_CURRENT)

        val calendar=Calendar.getInstance()

        calendar.clear()
        calendar.add(Calendar.MINUTE,60)
        calendar.add(Calendar.SECOND,0)
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY,broadcast)
    }
}