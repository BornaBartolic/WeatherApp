package hr.algebra.weatherapp.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

const val API_URL = "https://api.openweathermap.org/"
const val API_KEY = "b3860da545ed65d5e7eb7dbe20a3fce4"

interface WeatherAPI {
    @GET("data/2.5/weather")
    fun fetchWeather(
        @Query("q") city: String,
        @Query("appid") apiKey: String = API_KEY,
        @Query("units") units: String = "metric"
    ): Call<WeatherItem>

    @GET("data/2.5/weather")
    fun fetchWeatherByLocation(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String = API_KEY,
        @Query("units") units: String = "metric"
    ): Call<WeatherItem>
}