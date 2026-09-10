package hr.algebra.weatherapp.framework

import android.Manifest
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Handler
import android.os.Looper
import androidx.preference.PreferenceManager
import android.view.View
import android.view.animation.AnimationUtils
import androidx.core.app.ActivityCompat
import hr.algebra.weatherapp.HostActivity
import hr.algebra.weatherapp.R
import androidx.core.content.edit
import androidx.core.content.getSystemService
import hr.algebra.weatherapp.WEATHER_PROVIDER_CONTENT_URI
import hr.algebra.weatherapp.WeatherReceiver
import hr.algebra.weatherapp.model.LocationItem

fun View.applyAnimation(animationId: Int)
        = startAnimation(AnimationUtils.loadAnimation(context,animationId))

inline fun <reified T : Activity> Context.startActivity(){
    startActivity(Intent(this,T::class.java).apply{
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    })
}
inline fun  <reified T : BroadcastReceiver> Context.sendBroadcast(){
    sendBroadcast(Intent(this, T::class.java))

}
fun Context.getBooleanProperty(key: String) =
    PreferenceManager.getDefaultSharedPreferences(this)
        .getBoolean(key,false)


fun Context.setBooleanProperty(key: String,value: Boolean = true) {
    PreferenceManager.getDefaultSharedPreferences(this)
        .edit {
            putBoolean(key, value)
        }
}

fun Context.isOnline(): Boolean {
    val connectivityManager = getSystemService<ConnectivityManager>()
    connectivityManager?.activeNetwork?.let{
            network -> connectivityManager.getNetworkCapabilities(network)?.let{
            capabilities -> return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
            || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
    }
    }
    return false
}

fun delayedCall(delay: Long, work: Runnable){
    Handler(Looper.getMainLooper()).postDelayed(
        work,
        delay
    )
}

fun Context.fetchItems() : MutableList<LocationItem> {
    val items = mutableListOf<LocationItem>()



    contentResolver.query(
        WEATHER_PROVIDER_CONTENT_URI,
        null,
        null,
        null,
        null
    ).use { rs ->
        while(rs?.moveToNext() == true){
            items.add(LocationItem(
                rs.getLong(rs.getColumnIndexOrThrow(LocationItem::_id.name)),
                rs.getString(rs.getColumnIndexOrThrow(LocationItem::cityName.name)),
                rs.getDouble(rs.getColumnIndexOrThrow(LocationItem::temperature.name)),
                rs.getDouble(rs.getColumnIndexOrThrow(LocationItem::feelsLike.name)),
                rs.getInt(rs.getColumnIndexOrThrow(LocationItem::humidity.name)),
                rs.getInt(rs.getColumnIndexOrThrow(LocationItem::pressure.name)),
                rs.getDouble(rs.getColumnIndexOrThrow(LocationItem::windSpeed.name)),
                rs.getString(rs.getColumnIndexOrThrow(LocationItem::description.name)),
                rs.getInt(rs.getColumnIndexOrThrow(LocationItem::isFavorite.name)) == 1
            ))
        }
    }
    return items
}

inline fun <reified T : Activity> Context.startActivity(key : String, value : Int){
    startActivity(Intent(this,T::class.java).apply{
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        putExtra(key,value)
    })
}

fun Context.getLastKnownLocation(): Location? {
    val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

    if (ActivityCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        return null
    }

    val providers = locationManager.getProviders(true)
    for (provider in providers) {
        val location = locationManager.getLastKnownLocation(provider)
        if (location != null) return location
    }
    return null
}