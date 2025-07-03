package com.example.lmsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.lmsapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root) // Use ViewBinding

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // Setup AppBarConfiguration: Pass top-level destinations.
        // These destinations will not show an "Up" button in the ActionBar.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.loginRegisterFragment, // If user reaches here, it's a top-level for them
                R.id.courseListFragment     // Main content screen
                // Add other top-level destinations as needed
            )
        )

        // Set up the ActionBar with NavController
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        // Handle "Up" button navigation
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}
