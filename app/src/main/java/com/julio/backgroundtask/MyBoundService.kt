package com.julio.backgroundtask

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.widget.Toast

class MyBoundService : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Service berjalan di main thread, jadi bisa memanggil Toast disini
        Toast.makeText(this, "Hello world from Service", Toast.LENGTH_SHORT).show()
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder {
        throw UnsupportedOperationException("Not yet implemented")
    }
}
