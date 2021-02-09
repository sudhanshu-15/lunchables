package me.ssiddh.lunchables

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import me.ssiddh.lunchables.databinding.MainActivityBinding
import me.ssiddh.lunchables.ui.main.ListResultFragment
import me.ssiddh.lunchables.ui.main.MainViewModel
import me.ssiddh.lunchables.ui.main.MapsFragment
import me.ssiddh.lunchables.utils.UIStates
import me.ssiddh.lunchables.utils.viewBinding

class MainActivity : AppCompatActivity() {

    private val binding by viewBinding(MainActivityBinding::inflate)

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setActionBar(binding.myToolbar)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

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