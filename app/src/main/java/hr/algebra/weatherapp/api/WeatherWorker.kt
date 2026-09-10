package hr.algebra.weatherapp.api

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

//worker se brine da ode u backgorund da fetcher odradi svoje
class WeatherWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {
    override fun doWork(): Result {
        WeatherFetcher(context).fetchMultipleCities(
            listOf("Zagreb", "Split")
        )
        return Result.success()
    }
}