package hr.algebra.android_api_manager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Adapter
import hr.algebra.android_api_manager.databinding.ActivityApimanagerPagerBinding
import hr.algebra.android_api_manager.framework.fetchItems
import hr.algebra.android_api_manager.model.Item

const val ITEM_POSITION = "hr.algebra.android_api_manager.item_position"
class APIManagerPagerActivity : AppCompatActivity() {

    private lateinit var items: MutableList<Item>
    private lateinit var binding: ActivityApimanagerPagerBinding

    private var itemPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityApimanagerPagerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initPager()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun initPager() {
        items = fetchItems()
        itemPosition = intent.getIntExtra(ITEM_POSITION, 0)
        binding.viewPager.adapter = APIManagerPagerAdapter(this, items)
        binding.viewPager.currentItem = itemPosition
    }
}