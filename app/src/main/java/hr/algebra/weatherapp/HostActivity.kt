package hr.algebra.weatherapp

import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import hr.algebra.weatherapp.api.WeatherFetcher
import hr.algebra.weatherapp.databinding.ActivityHostBinding
import hr.algebra.weatherapp.databinding.ActivitySplashScreenBinding

class HostActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHostBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleTransition()
        binding = ActivityHostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initHamburgerMenu()
        initNavigation()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.host_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                toggleDrawer()
                return true
            }
            R.id.exit -> {
                exitApp()
                return true
            }
            R.id.refresh -> {
                WeatherFetcher(this).refreshAllItems()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun exitApp() {
        AlertDialog.Builder(this).apply {
            setTitle((R.string.exit))
            setMessage("Really exit app?")
            setIcon(R.drawable.exit)
            setNegativeButton("Cancel", null)
            setPositiveButton("Exit") { _, _ -> finishAffinity() }
            show()
        }
    }

    private fun toggleDrawer() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            binding.drawerLayout.closeDrawers()
        }
        else
        {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
    }

    private fun initHamburgerMenu() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)
    }

    private fun initNavigation() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.navController) as NavHostFragment
        val navController = navHostFragment.navController

        NavigationUI.setupWithNavController(binding.navView, navController)
    }


    private fun handleTransition() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE){
            overrideActivityTransition(
                OVERRIDE_TRANSITION_OPEN,
                android.R.anim.fade_in,
                android.R.anim.fade_out
            )

        } else{
            @Suppress("DEPRECATION")
            overridePendingTransition(
                android.R.anim.fade_in,
                android.R.anim.fade_out
            )
        }
    }
}
