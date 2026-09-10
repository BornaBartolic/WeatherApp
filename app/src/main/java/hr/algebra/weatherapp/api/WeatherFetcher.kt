package hr.algebra.weatherapp.api

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.util.Log
import hr.algebra.weatherapp.WEATHER_PROVIDER_CONTENT_URI
import hr.algebra.weatherapp.WeatherReceiver
import hr.algebra.weatherapp.framework.fetchItems
import hr.algebra.weatherapp.framework.sendBroadcast
import hr.algebra.weatherapp.framework.sendWeatherNotification
import hr.algebra.weatherapp.model.LocationItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class WeatherFetcher(private val context: Context) {

    private val weatherApi: WeatherAPI
    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        weatherApi = retrofit.create<WeatherAPI>()
    }

    fun fetchItems(city: String = "Zagreb") {
        val request = weatherApi.fetchWeather(city)
        request.enqueue(object : Callback<WeatherItem> {
            override fun onResponse(call: Call<WeatherItem>, response: Response<WeatherItem>) {
                response.body()?.let { populateItem(it) }
            }
            override fun onFailure(call: Call<WeatherItem>, t: Throwable) {
                Log.e("ERROR", t.toString(), t)
            }
        })
    }

    fun fetchMultipleCities(cities: List<String>) {
        cities.forEach { city -> fetchItems(city) }
    }

    private fun populateItem(weatherItem: WeatherItem) {
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            val values = ContentValues().apply {
                put(LocationItem::cityName.name, weatherItem.cityName)
                put(LocationItem::temperature.name, weatherItem.main.temperature)
                put(LocationItem::feelsLike.name, weatherItem.main.feelsLike)
                put(LocationItem::humidity.name, weatherItem.main.humidity)
                put(LocationItem::pressure.name, weatherItem.main.pressure)
                put(LocationItem::windSpeed.name, weatherItem.wind.speed)
                put(LocationItem::description.name, weatherItem.weather.firstOrNull()?.description ?: "")
            }

            val existing = context.fetchItems().find {
                it.cityName.equals(weatherItem.cityName, ignoreCase = true)
            }

            if (existing != null) {
                context.contentResolver.update(
                    ContentUris.withAppendedId(WEATHER_PROVIDER_CONTENT_URI, existing._id!!),
                    values, null, null
                )
            } else {
                values.put(LocationItem::isFavorite.name, false)
                context.contentResolver.insert(WEATHER_PROVIDER_CONTENT_URI, values)
            }

            context.sendWeatherNotification(weatherItem.cityName)
            context.sendBroadcast<WeatherReceiver>()
        }
    }

    fun fetchByLocation(lat: Double, lon: Double) {
        val request = weatherApi.fetchWeatherByLocation(lat, lon)
        request.enqueue(object : Callback<WeatherItem> {
            override fun onResponse(call: Call<WeatherItem>, response: Response<WeatherItem>) {
                response.body()?.let { populateItem(it) }
            }
            override fun onFailure(call: Call<WeatherItem>, t: Throwable) {
                Log.e("ERROR", t.toString(), t)
            }
        })
    }

    fun refreshAllItems() {
        val cities = context.fetchItems().map { it.cityName }
        fetchMultipleCities(cities)
    }

}