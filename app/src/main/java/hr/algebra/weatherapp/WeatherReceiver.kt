package hr.algebra.weatherapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import hr.algebra.weatherapp.framework.setBooleanProperty
import hr.algebra.weatherapp.framework.startActivity

class WeatherReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {


        context.setBooleanProperty(DATA_IMPORTED)
        context.startActivity<HostActivity>()

    }
}