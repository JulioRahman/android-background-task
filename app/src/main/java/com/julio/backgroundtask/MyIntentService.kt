package com.julio.backgroundtask

import android.app.IntentService
import android.content.Context
import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager

private const val ACTION_FROM_ACTIVITY = "com.julio.backgroundtask.action.ACT"
private const val ACTION_FROM_JOB_SERVICE = "com.julio.backgroundtask.action.JS"

private const val EXTRA_WORD1 = "com.julio.backgroundtask.extra.WORD1"
private const val EXTRA_WORD2 = "com.julio.backgroundtask.extra.WORD2"

/**
 * An [IntentService] subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * helper methods.
 */
class MyIntentService : IntentService("MyIntentService") {

    override fun onHandleIntent(intent: Intent?) {
        when (intent?.action) {
            ACTION_FROM_ACTIVITY -> {
                val param1 = intent.getStringExtra(EXTRA_WORD1)
                val param2 = intent.getStringExtra(EXTRA_WORD2)
                handleActionFromActivity(param1, param2)
            }
            ACTION_FROM_JOB_SERVICE -> {
                val param1 = intent.getStringExtra(EXTRA_WORD1)
                val param2 = intent.getStringExtra(EXTRA_WORD2)
                handleActionFromJobService(param1, param2)
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private fun handleActionFromActivity(param1: String, param2: String) {
        // Dikarenakan IntentService ini berjalan di worker thread, untuk memunculkan Toast atau mengubah UI,
        // harus dijalankan di main thread.
        sendBroadcastMessage(
            String.format(
                "%s %s, from handleActionFromActivity",
                param1,
                param2
            )
        )
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private fun handleActionFromJobService(param1: String, param2: String) {
        // Dikarenakan IntentService ini berjalan di worker thread, untuk memunculkan Toast atau mengubah UI,
        // harus dijalankan di main thread.
        sendBroadcastMessage(
            String.format(
                "%s %s, from handleActionFromJobService",
                param1,
                param2
            )
        )
    }

    private fun sendBroadcastMessage(message: String) {
        // Kirim message ke main thread menggunakan LocalBroadcastManager.
        val localBroadcastManager: LocalBroadcastManager = LocalBroadcastManager.getInstance(this)
        val localIntent = Intent(MainActivity.BROADCAST_ACTION)
        localIntent.putExtra("message", message)
        localBroadcastManager.sendBroadcast(localIntent)
    }

    companion object {
        /**
         * Starts this service to perform action Foo with the given parameters. If
         * the service is already performing a task this action will be queued.
         *
         * @see IntentService
         */
        @JvmStatic
        fun startActionFromActivity(context: Context, param1: String, param2: String) {
            val intent = Intent(context, MyIntentService::class.java).apply {
                action = ACTION_FROM_ACTIVITY
                putExtra(EXTRA_WORD1, param1)
                putExtra(EXTRA_WORD2, param2)
            }
            context.startService(intent)
        }

        /**
         * Starts this service to perform action Baz with the given parameters. If
         * the service is already performing a task this action will be queued.
         *
         * @see IntentService
         */
        @JvmStatic
        fun startActionJobService(context: Context, param1: String, param2: String) {
            val intent = Intent(context, MyIntentService::class.java).apply {
                action = ACTION_FROM_JOB_SERVICE
                putExtra(EXTRA_WORD1, param1)
                putExtra(EXTRA_WORD2, param2)
            }
            context.startService(intent)
        }
    }
}
