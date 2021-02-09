package me.ssiddh.lunchables

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import me.ssiddh.lunchables.databinding.MainActivityBinding
import me.ssiddh.lunchables.ui.main.MapsFragment
import me.ssiddh.lunchables.utils.viewBinding

class MainActivity : AppCompatActivity() {

    private val binding by viewBinding(MainActivityBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setActionBar(binding.myToolbar)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MapsFragment.newInstance())
                .commitNow()
        }
    }
}