package hr.algebra.weatherapp.fragment

import androidx.appcompat.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import hr.algebra.weatherapp.R
import hr.algebra.weatherapp.adapter.ItemAdapter
import hr.algebra.weatherapp.api.WeatherFetcher
import hr.algebra.weatherapp.databinding.FragmentItemsBinding
import hr.algebra.weatherapp.model.LocationItem
import hr.algebra.weatherapp.framework.fetchItems
import hr.algebra.weatherapp.framework.getLastKnownLocation

class ItemsFragment : Fragment() {

    private lateinit var binding: FragmentItemsBinding
    private lateinit var items: MutableList<LocationItem>
    private lateinit var adapter: ItemAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentItemsBinding.inflate(inflater, container, false)
        items = requireContext().fetchItems()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ItemAdapter(requireContext(), items)
        binding.rvItems.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@ItemsFragment.adapter
        }

        binding.fabAddCity.setOnClickListener {
            showAddCityDialog()
        }

        binding.fabMyLocation.setOnClickListener {
            addMyLocation()
        }
    }

    private fun addMyLocation() {
        val location = requireContext().getLastKnownLocation()
        if (location != null) {
            WeatherFetcher(requireContext()).fetchByLocation(location.latitude, location.longitude)
        } else {
            AlertDialog.Builder(requireContext())
                .setTitle("Lokacija nedostupna")
                .setMessage("Nije moguće dohvatiti lokaciju. Provjeri jesi li omogućio GPS/lokaciju na uređaju.")
                .setPositiveButton("OK", null)
                .show()
        }
    }

    private fun showAddCityDialog() {
        val editText = EditText(requireContext())
        editText.hint = "Naziv grada"

        AlertDialog.Builder(requireContext())
            .setTitle("Dodaj grad")
            .setView(editText)
            .setPositiveButton("Dodaj") { _, _ ->
                val cityName = editText.text.toString().trim()

                if (cityName.isEmpty()) {
                    return@setPositiveButton
                }

                val alreadyExists = items.any {
                    it.cityName.equals(cityName, ignoreCase = true)
                }

                if (alreadyExists) {
                    AlertDialog.Builder(requireContext())
                        .setTitle("Grad već postoji")
                        .setMessage("$cityName je već na tvojoj listi lokacija.")
                        .setPositiveButton("OK", null)
                        .show()
                } else {
                    WeatherFetcher(requireContext()).fetchItems(cityName)
                }
            }
            .setNegativeButton("Odustani", null)
            .show()
    }

}