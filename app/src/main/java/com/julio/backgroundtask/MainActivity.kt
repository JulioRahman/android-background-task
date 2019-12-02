package com.julio.backgroundtask

import android.app.ProgressDialog
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.*
import android.os.AsyncTask
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var dialog: ProgressDialog

    /**
     * broadcast receiver untuk menerima broadcast dari service
     */
    private val myBroadCastReceiver: BroadcastReceiver = MyBroadCastReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dialog = ProgressDialog(this)
        dialog.setCancelable(false)

        btnThread.setOnClickListener {
            runThread()
        }

        btnAsyncTask.setOnClickListener {
            runAsyncTask()
        }

        btnScheduler.setOnClickListener {
            runScheduler()
        }

        btnIntentService.setOnClickListener {
            runIntentService()
        }

        btnService.setOnClickListener {
            runService()
        }

        registerMyReceiver()
    }

    /**
     * This function is responsible to register an action to BroadCastReceiver
     */
    private fun registerMyReceiver() {
        LocalBroadcastManager.getInstance(this).registerReceiver(
            myBroadCastReceiver, IntentFilter(
                BROADCAST_ACTION
            )
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(myBroadCastReceiver)
    }

    private fun runThread() {
        // munculkan dialog sebelum thread dijalankan
        dialog.show()

        Thread(Runnable {
            // berjalan di worker thread
            // simulasi proses dengan menunggu 3 detik
            try {
                Thread.sleep(3000)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            // jalankan aksi di ui thread karena harus mengubah UI (dialog)
            runOnUiThread { dialog.dismiss() }
        }).start()
    }

    private fun runAsyncTask() {
        MyAsyncTask(dialog).execute()
    }

    private fun runScheduler() {
        val serviceComponent = ComponentName(this, MyJobService::class.java)
        val builder = JobInfo.Builder(0, serviceComponent)

        builder.setMinimumLatency(1000) // wait at least
        builder.setOverrideDeadline(3 * 1000.toLong()) // maximum delay
//        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED) // require unmetered network
//        builder.setRequiresDeviceIdle(true) // device should be idle
//        builder.setRequiresCharging(false) // we don't care if the device is charging or not

        val jobScheduler = getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
        jobScheduler.schedule(builder.build())
    }

    private fun runIntentService() {
        MyIntentService.startActionFromActivity(this, "hello", "world")
    }

    private fun runService() {
        val intent = Intent(this, MyBoundService::class.java)
        startService(intent)
    }

    class MyAsyncTask(private var dialog: ProgressDialog) : AsyncTask<Int, Void, Void>() {

        // fungsi ini berjalan di ui thread
        override fun onPreExecute() {
            super.onPreExecute()
            dialog.show()
        }

        override fun doInBackground(vararg p0: Int?): Void? {
            try {
                Thread.sleep(3000)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            return null
        }

        // fungsi ini berjalan di ui thread
        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            dialog.dismiss()
        }
    }

    class MyBroadCastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            // ambil nilai message dari intent extra
            val message = intent.getStringExtra("message")

            // jika message tidak kosong, tampilkan dalam Toast
            Toast.makeText(context, message ?: "", Toast.LENGTH_SHORT).show()
        }

    }

    companion object {
        const val BROADCAST_ACTION = "BROADCAST_ACTION"
    }
}
