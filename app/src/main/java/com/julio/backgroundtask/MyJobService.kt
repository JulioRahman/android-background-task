package com.julio.backgroundtask

import android.app.job.JobParameters
import android.app.job.JobService

class MyJobService : JobService() {

    override fun onStartJob(p0: JobParameters?): Boolean {
        // lakukan background task atau memanggil service
        MyIntentService.startActionJobService(this, "hello", "world")
        return true
    }

    override fun onStopJob(p0: JobParameters?): Boolean {
        return true
    }
}
