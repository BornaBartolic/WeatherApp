package hr.algebra.weatherapp.adapter

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import hr.algebra.weatherapp.R
import hr.algebra.weatherapp.WEATHER_PROVIDER_CONTENT_URI
import hr.algebra.weatherapp.model.LocationItem

class ItemPagerAdapter(
    private val context: Context,
    private val items: MutableList<LocationItem>
) : RecyclerView.Adapter<ItemPagerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.item_pager, parent, false)
        )
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val item = items[position]
        holder.bind(item)
        holder.ivFavorite.setOnClickListener {
            updateItem(position)
        }
    }

    // "content://hr.algebra.weatherapp.provider/items
    // "content://hr.algebra.weatherapp.provider/items/22
    private fun updateItem(position: Int) {
        val item = items[position]
        item.isFavorite = !item.isFavorite
        context.contentResolver.update(
            ContentUris.withAppendedId(WEATHER_PROVIDER_CONTENT_URI, item._id!!),
            ContentValues().apply {
                put(LocationItem::isFavorite.name, item.isFavorite)
            },
            null,
            null
        )
        notifyItemChanged(position)
    }

    override fun getItemCount() = items.count()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvCityName = itemView.findViewById<TextView>(R.id.tvCityName)
        private val tvTemperature = itemView.findViewById<TextView>(R.id.tvTemperature)
        private val tvDescription = itemView.findViewById<TextView>(R.id.tvDescription)
        private val tvFeelsLike = itemView.findViewById<TextView>(R.id.tvFeelsLike)
        private val tvHumidity = itemView.findViewById<TextView>(R.id.tvHumidity)
        private val tvPressure = itemView.findViewById<TextView>(R.id.tvPressure)
        private val tvWind = itemView.findViewById<TextView>(R.id.tvWind)
        val ivFavorite = itemView.findViewById<ImageView>(R.id.ivFavorite)

        fun bind(item: LocationItem) {
            tvCityName.text = item.cityName
            tvTemperature.text = "${item.temperature}°C"
            tvDescription.text = item.description
            tvFeelsLike.text = "${item.feelsLike}°C"
            tvHumidity.text = "${item.humidity}%"
            tvPressure.text = "${item.pressure} hPa"
            tvWind.text = "${item.windSpeed} m/s"
            ivFavorite.setImageResource(
                if (item.isFavorite) R.drawable.green_flag else R.drawable.red_flag
            )
        }
    }
}