package hr.algebra.weatherapp.model

data class LocationItem(
    var _id: Long? = null,
    val cityName: String,
    val temperature: Double = 0.0,
    val feelsLike: Double = 0.0,
    val humidity: Int = 0,
    val pressure: Int = 0,
    val windSpeed: Double = 0.0,
    val description: String = "",
    var isFavorite: Boolean = false
)