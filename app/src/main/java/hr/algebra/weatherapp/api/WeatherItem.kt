package hr.algebra.weatherapp.api

import com.google.gson.annotations.SerializedName

data class WeatherItem(
    @SerializedName("name") val cityName: String,
    @SerializedName("main") val main: MainInfo,
    @SerializedName("weather") val weather: List<WeatherDetail>,
    @SerializedName("wind") val wind: WindInfo
) {
    data class MainInfo(
        @SerializedName("temp") val temperature: Double,
        @SerializedName("feels_like") val feelsLike: Double,
        @SerializedName("humidity") val humidity: Int,
        @SerializedName("pressure") val pressure: Int
    )

    data class WeatherDetail(
        @SerializedName("description") val description: String,
        @SerializedName("icon") val icon: String,
        @SerializedName("main") val main: String
    )

    data class WindInfo(
        @SerializedName("speed") val speed: Double
    )
}