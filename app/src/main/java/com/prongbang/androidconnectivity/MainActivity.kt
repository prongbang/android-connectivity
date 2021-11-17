package com.prongbang.androidconnectivity

import android.os.Build
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import com.prongbang.androidconnectivity.databinding.ActivityMainBinding
import com.prongbang.connectivity.ConnectivityProvider
import com.prongbang.connectivity.ConnectivityStateListener
import com.prongbang.connectivity.NetworkState
import com.prongbang.connectivity.hasInternet
import com.tapadoo.alerter.Alerter
import com.tapadoo.alerter.OnHideAlertListener

class MainActivity : AppCompatActivity(), ConnectivityStateListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private var displayedAlert = false

    private val connectivityProvider by lazy {
        ConnectivityProvider.create(this, lifecycle, this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        // Network state synchronously
        val hasInternet = connectivityProvider.getNetworkState().hasInternet()
        networkAlert(hasInternet)
    }

    private fun networkAlert(hasInternet: Boolean) {
        if (displayedAlert.not() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            displayedAlert = true
            Alerter.create(this)
                .setTitle("Connectivity")
                .setText(if (hasInternet) "Internet Connected" else "No Internet Connect")
                .setBackgroundColorInt(getColor(if (hasInternet) android.R.color.holo_green_light else android.R.color.holo_red_light))
                .setOnHideListener { displayedAlert = false }
                .show()
        }
    }

    override fun onNetworkStateChange(state: NetworkState) {
        val hasInternet = state.hasInternet()
        networkAlert(hasInternet)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}