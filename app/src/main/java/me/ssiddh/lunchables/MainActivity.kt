package me.ssiddh.lunchables

import android.Manifest
import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.livinglifetechway.quickpermissions_kotlin.runWithPermissions
import com.livinglifetechway.quickpermissions_kotlin.util.QuickPermissionsOptions
import kotlinx.coroutines.launch
import me.ssiddh.lunchables.databinding.MainActivityBinding
import me.ssiddh.lunchables.ui.main.ListResultFragment
import me.ssiddh.lunchables.ui.main.MainViewModel
import me.ssiddh.lunchables.ui.main.MapsFragment
import me.ssiddh.lunchables.utils.UIStates
import me.ssiddh.lunchables.utils.awaitLastLocation
import me.ssiddh.lunchables.utils.viewBinding
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val binding by viewBinding(MainActivityBinding::inflate)

    private val viewModel: MainViewModel by viewModel()

    private val fusedLocationProviderClient: FusedLocationProviderClient by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setActionBar(binding.myToolbar)

        viewModel.uiState.observe(this, Observer { uiState ->
            updateButtonState(uiState)
            when (uiState) {
                UIStates.ListUIState -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, ListResultFragment.newInstance())
                        .commit()
                }
                UIStates.MapUIState -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, MapsFragment.newInstance())
                        .commit()
                }
                UIStates.NoLocationUIState -> {

                }
                UIStates.ErrorLoadingUIState -> {

                }
            }
        })

        binding.buttonSwitchResult.setOnClickListener {
            viewModel.switchResultView()
        }

        binding.textInputLayout.setEndIconOnClickListener {
            Toast.makeText(this, "Search Clicked", Toast.LENGTH_LONG).show()
        }

        requestPermissions()

    }

    @SuppressLint("MissingPermission")
    private fun requestPermissions() =
        runWithPermissions(Manifest.permission.ACCESS_FINE_LOCATION) {
            lifecycleScope.launch {
                val lastLocation = fusedLocationProviderClient.awaitLastLocation()
                Log.d(
                    "SSLOG",
                    "Last location ${lastLocation.latitude} ${lastLocation.longitude}"
                )
            }
        }

    private fun updateButtonState(uiState: UIStates) {
        binding.buttonSwitchResult.apply {
            when (uiState) {
                UIStates.NoLocationUIState, UIStates.ErrorLoadingUIState -> visibility = View.GONE
                else -> {
                    if (visibility == View.GONE) {
                        visibility = View.VISIBLE
                    }
                }
            }
            text = resources.getString(uiState.stringResId)
            icon = AppCompatResources.getDrawable(
                this@MainActivity,
                uiState.iconResId
            )
        }
    }
}