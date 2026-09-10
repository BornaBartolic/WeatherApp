package hr.algebra.weatherapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import hr.algebra.weatherapp.adapter.ItemPagerAdapter
import hr.algebra.weatherapp.databinding.ActivityItemPagerBinding
import hr.algebra.weatherapp.framework.fetchItems
import hr.algebra.weatherapp.model.LocationItem

const val ITEM_POS = "hr.algebra.weatherapp.item_position"

class ItemPagerActivity : AppCompatActivity() {


    private lateinit var binding: ActivityItemPagerBinding
    private lateinit var  items: MutableList<LocationItem>
    private var position = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityItemPagerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()

        }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return super.onSupportNavigateUp()
    }
    private fun init() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        position = intent.getIntExtra(ITEM_POS, position)
        items = fetchItems()

        binding.viewPager2.adapter = ItemPagerAdapter(this, items)
        binding.viewPager2.setCurrentItem(position, false)
    }
}
