package hr.algebra.android_api_manager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import hr.algebra.android_api_manager.databinding.ActivitySplashScreenBinding
import hr.algebra.android_api_manager.framework.*

private const val DELAY = 4000L
const val DATA_IMPORTED = "hr.algebra.android_api_manager.data_imported"

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        startAnimations()
        redirect()
    }

    private fun startAnimations() {
        binding.ivSplash.startAnimation(R.anim.rotate)
        binding.tvSplash.startAnimation(R.anim.blink)
    }

    private fun redirect() {
        if (getBooleanPreference(DATA_IMPORTED)) {
            callDelayed(DELAY) { startActivity<HostActivity>() }
        } else {
            if (isOnline()) {
                Intent(this, APIManagerService::class.java).apply {
                    APIManagerService.enqueue(
                        this@SplashScreenActivity,
                        this
                    )
                }
            } else {
                binding.tvSplash.text = getString(R.string.no_internet)
                callDelayed(DELAY) { finish() }
            }
        }
    }
}