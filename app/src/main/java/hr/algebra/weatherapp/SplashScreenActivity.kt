package hr.algebra.weatherapp

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import hr.algebra.weatherapp.api.WeatherWorker
import hr.algebra.weatherapp.databinding.ActivitySplashScreenBinding
import hr.algebra.weatherapp.framework.applyAnimation
import hr.algebra.weatherapp.framework.createNotificationChannel
import hr.algebra.weatherapp.framework.delayedCall
import hr.algebra.weatherapp.framework.getBooleanProperty
import hr.algebra.weatherapp.framework.isOnline
import hr.algebra.weatherapp.framework.startActivity



private const val DELAY = 3000L
const val DATA_IMPORTED = "hr.algebra.weather.data_imported"
class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { }

    private val locationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        createNotificationChannel()
        requestNotificationPermission()
        requestLocationPermission()

        hideSystemBars()
        startAnimations()
        redirect()
    }

    private fun requestLocationPermission() {
        locationPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    private fun startAnimations() {
        binding.tvSplash.applyAnimation(R.anim.blink)
        binding.icWeather.applyAnimation(R.anim.blink)

    }

    private fun redirect() {

        if (getBooleanProperty(DATA_IMPORTED))
        {
            delayedCall(DELAY) {startActivity<HostActivity>()}
        }else {
            if (isOnline()){
                WorkManager.getInstance(this).apply {
                    enqueueUniqueWork(
                        DATA_IMPORTED,
                        ExistingWorkPolicy.KEEP,
                        OneTimeWorkRequest.from(WeatherWorker::class.java)
                    )
                }
            }else{
                //exit, ako nema interneta cekaj 3 sec onda izadi
                binding.tvSplash.text = getString(R.string.no_internet)
                delayedCall(DELAY) { finish() }
            }

        }
    }

    private fun hideSystemBars() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, window.decorView).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

}