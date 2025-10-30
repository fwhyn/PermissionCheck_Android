package com.fwhyn.app.permissioncheck.core.percheck

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.fwhyn.app.permissioncheck.core.percheck.databinding.ActivityPerCheckBinding
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar

class PerCheckActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityPerCheckBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPerCheckBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appBarPerCheck.toolbar)

        binding.appBarPerCheck.fab?.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .setAnchorView(R.id.fab).show()
        }

        val navHostFragment =
            (supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_per_check) as NavHostFragment?)!!
        val navController = navHostFragment.navController

        binding.navView?.let {
            appBarConfiguration = AppBarConfiguration(
                setOf(
                    R.id.nav_transform, R.id.nav_reflow, R.id.nav_slideshow, R.id.nav_settings
                ),
                binding.drawerLayout
            )
            setupActionBarWithNavController(navController, appBarConfiguration)
            it.setupWithNavController(navController)
        }

        binding.appBarPerCheck.contentPerCheck.bottomNavView?.let {
            appBarConfiguration = AppBarConfiguration(
                setOf(
                    R.id.nav_transform, R.id.nav_reflow, R.id.nav_slideshow
                )
            )
            setupActionBarWithNavController(navController, appBarConfiguration)
            it.setupWithNavController(navController)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val result = super.onCreateOptionsMenu(menu)
        // Using findViewById because NavigationView exists in different layout files
        // between w600dp and w1240dp
        val navView: NavigationView? = findViewById(R.id.nav_view)
        if (navView == null) {
            // The navigation drawer already has the items including the items in the overflow menu
            // We only inflate the overflow menu if the navigation drawer isn't visible
            menuInflater.inflate(R.menu.overflow, menu)
        }
        return result
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_settings -> {
                val navController = findNavController(R.id.nav_host_fragment_content_per_check)
                navController.navigate(R.id.nav_settings)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_per_check)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}