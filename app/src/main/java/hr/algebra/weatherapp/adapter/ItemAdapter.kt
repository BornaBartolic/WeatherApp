package hr.algebra.weatherapp.adapter

import android.content.ContentUris
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import hr.algebra.weatherapp.ITEM_POS
import hr.algebra.weatherapp.ItemPagerActivity
import hr.algebra.weatherapp.R
import hr.algebra.weatherapp.WEATHER_PROVIDER_CONTENT_URI
import hr.algebra.weatherapp.framework.startActivity
import hr.algebra.weatherapp.model.LocationItem


class ItemAdapter(
    private val context: Context,
    private val items: MutableList<LocationItem>
) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvCityName = itemView.findViewById<TextView>(R.id.tvCityName)
        private val tvTemperature = itemView.findViewById<TextView>(R.id.tvTemperature)
        private val tvDescription = itemView.findViewById<TextView>(R.id.tvDescription)
        private val tvHumidity = itemView.findViewById<TextView>(R.id.tvHumidity)
        private val tvWind = itemView.findViewById<TextView>(R.id.tvWind)

        fun bind(locationItem: LocationItem) {
            tvCityName.text = locationItem.cityName
            tvTemperature.text = "${locationItem.temperature}°C"
            tvDescription.text = locationItem.description
            tvHumidity.text = "Vlažnost: ${locationItem.humidity}%"
            tvWind.text = "Vjetar: ${locationItem.windSpeed} m/s"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
        holder.itemView.setOnClickListener {
            context.startActivity<ItemPagerActivity>(
                ITEM_POS,
                position)
        }
        holder.itemView.setOnLongClickListener {
            deleteItem(position)
            true
        }
    }

    private fun deleteItem(position: Int) {
        val item = items[position]
        context.contentResolver.delete(
            ContentUris.withAppendedId(WEATHER_PROVIDER_CONTENT_URI, item._id!!),
            null,
            null
        )
        items.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun getItemCount(): Int = items.count()
}