package me.ssiddh.lunchables

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.livinglifetechway.quickpermissions_kotlin.runWithPermissions
import com.livinglifetechway.quickpermissions_kotlin.util.QuickPermissionsOptions
import kotlinx.coroutines.launch
import me.ssiddh.lunchables.databinding.MainActivityBinding
import me.ssiddh.lunchables.ui.main.ListResultFragment
import me.ssiddh.lunchables.ui.main.MainViewModel
import me.ssiddh.lunchables.ui.main.MapsFragment
import me.ssiddh.lunchables.utils.SearchResultStates
import me.ssiddh.lunchables.utils.UIStates
import me.ssiddh.lunchables.utils.awaitLastLocation
import me.ssiddh.lunchables.utils.viewBinding
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val binding by viewBinding(MainActivityBinding::inflate)

    private val viewModel: MainViewModel by viewModel()

    private val fusedLocationProviderClient: FusedLocationProviderClient by inject()

    private val quickPermissionOptions = QuickPermissionsOptions(
        handleRationale = true,
        handlePermanentlyDenied = true,
        permanentDeniedMethod = {
            viewModel.permissionsDenied()
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setActionBar(binding.myToolbar)

        viewModel.uiState.observe(
            this,
            Observer { uiState ->
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
                        MaterialAlertDialogBuilder(this)
                            .setTitle(resources.getString(R.string.no_permissions))
                            .show()
                    }
                }
            }
        )

        viewModel.searchResultState.observe(
            this,
            Observer { searchResultState ->
                handleSearchResultState(searchResultState)
            }
        )

        binding.buttonSwitchResult.setOnClickListener {
            viewModel.switchResultView()
        }

        binding.textInputLayout.setEndIconOnClickListener {
            val searchText = binding.textInputLayout.editText?.text?.toString() ?: ""
            if (searchText.isBlank()) {
                binding.textInputLayout.isErrorEnabled = true
                binding.textInputLayout.error = "Looks like you forgot to enter a query"
            } else {
                lifecycleScope.launch {
                    val lastLocation = fusedLocationProviderClient.awaitLastLocation()
                    viewModel.getNearByRestaurants(lastLocation, searchText)
                }
            }
        }

        binding.textInputLayout.editText?.addTextChangedListener {
            binding.textInputLayout.apply {
                if (isErrorEnabled) {
                    binding.textInputLayout.error = null
                    binding.textInputLayout.isErrorEnabled = false
                }
            }
        }

        requestPermissions()
    }

    @SuppressLint("MissingPermission")
    private fun requestPermissions() =
        runWithPermissions(
            Manifest.permission.ACCESS_FINE_LOCATION,
            options = quickPermissionOptions
        ) {
            lifecycleScope.launch {
                val lastLocation = fusedLocationProviderClient.awaitLastLocation()
                Log.d(
                    "SSLOG",
                    "Last location ${lastLocation.latitude} ${lastLocation.longitude}"
                )
                viewModel.getNearByRestaurants(lastLocation)
            }
        }

    private fun updateButtonState(uiState: UIStates) {
        binding.buttonSwitchResult.apply {
            when (uiState) {
                UIStates.NoLocationUIState -> visibility = View.GONE
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

    private fun handleSearchResultState(searchResultState: SearchResultStates) {
        when (searchResultState) {
            SearchResultStates.Loading -> binding.textInputLayout.isEnabled = false
            is SearchResultStates.FoundPlaceResults -> {
                Log.d("SSLOG", "Results Found")
                binding.textInputLayout.isEnabled = true
            }
            else -> {
                binding.textInputLayout.isEnabled = true
                val snackBarMessage: Int =
                    when (searchResultState) {
                        is SearchResultStates.NoResultsButCache -> R.string.no_result_cache
                        is SearchResultStates.ErrorWhileSearching -> R.string.error_searching
                        else -> R.string.unknown_error
                    }
                val snackbar = Snackbar.make(
                    binding.mainCoordinator,
                    snackBarMessage,
                    Snackbar.LENGTH_INDEFINITE
                )
                snackbar.setAction(R.string.ok) {
                    snackbar.dismiss()
                }
                snackbar.show()
            }
        }
    }
}
