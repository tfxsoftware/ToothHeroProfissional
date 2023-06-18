package com.tfxsoftware.toothhero


import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build


import android.util.Log
import androidx.core.app.NotificationCompat

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage



class ToothHeroFireBaseMessagingService : FirebaseMessagingService() {

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        remoteMessage.notification?.let {
            Log.d(TAG, "Message Notification Body: ${it.body}")
            it.body?.let { body ->
                remoteMessage.data.let { data ->
                    val title = it.title // Get the title of the notification

                    if (title == "Atendimento") {
                        // Call a different method for handling status updates
                        sendAtendimentoNotification(body)
                    } else {
                        val emergencia = Emergencia(
                            data["eid"],
                            data["nome"],
                            data["telefone"],
                            data["fotoambos"],
                            data["fotocrianca"],
                            data["fotodoc"],
                            data["datahora"],
                            data["status"],
                            data["latitude"]!!.toDouble(),
                            data["longitude"]!!.toDouble()
                        )
                        Log.d(TAG, emergencia.toString())
                        sendEmergenciaNotification(body, emergencia)
                    }
                }
            }
        }
    }




    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // FCM registration token to your app server.
        sendRegistrationToServer(token)
    }
    // [END on_new_token]


    private fun sendRegistrationToServer(token: String?) {
        // TODO: Implement this method to send token to your app server.
        Log.d(TAG, "sendRegistrationTokenToServer($token)")
    }


    private fun sendAtendimentoNotification(messageBody: String){
        val intent = Intent(this, DentistaActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            action = "DentistaActivity"}

        val pendingIntent = PendingIntent.getActivity(this, 99, intent, PendingIntent.FLAG_IMMUTABLE)

        val channelId = getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Atendimento")
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.

        val channel = NotificationChannel(
            channelId,
            getString(R.string.default_notification_channel_name),
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)

        notificationManager.notify(99, notificationBuilder.build())
    }

    private fun sendEmergenciaNotification(messageBody: String, emergencia: Emergencia) {

        val intent = Intent(this, EmergenciaActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            action = "EmergenciaActivity"
            putExtra("eid", emergencia.eid.toString())
            putExtra("nome", emergencia.nome.toString())
            putExtra("telefone", emergencia.telefone.toString())
            putExtra("datahora", emergencia.datahora.toString())
            putExtra("fotoambos", emergencia.fotoambos.toString())
            putExtra("fotocrianca", emergencia.fotocrianca.toString())
            putExtra("fotodoc", emergencia.fotodoc.toString())
            putExtra("status", emergencia.status.toString())
            putExtra("latitude", emergencia.latitude!!)
            putExtra("longitude", emergencia.longitude!!)
        }


        val pendingIntent = PendingIntent.getActivity(this, 99, intent, PendingIntent.FLAG_IMMUTABLE)

        val channelId = getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(getString(R.string.notification_title))
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.

            val channel = NotificationChannel(
                channelId,
                getString(R.string.default_notification_channel_name),
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)

        notificationManager.notify(99, notificationBuilder.build())
    }

    companion object {

        private const val TAG = "ToothHeroFirebaseMsgService"
    }
}