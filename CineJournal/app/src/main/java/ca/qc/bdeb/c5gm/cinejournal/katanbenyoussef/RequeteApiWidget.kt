package ca.qc.bdeb.c5gm.cinejournal.katanbenyoussef

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class RequeteApiWidget(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {
    override fun doWork(): Result {
        // Perform your API call here


        // Return success or failure
        return Result.success()
    }
}