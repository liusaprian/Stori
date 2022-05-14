package app.liusaprian.stori.ui.detail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.viewbinding.library.activity.viewBinding
import app.liusaprian.stori.R
import app.liusaprian.stori.databinding.ActivityDetailBinding
import app.liusaprian.stori.network.response.Story
import app.liusaprian.stori.ui.locaton.LocationActivity
import com.bumptech.glide.Glide

class DetailActivity : AppCompatActivity() {

    private val binding: ActivityDetailBinding by viewBinding()
    private lateinit var data: Story

    companion object {
        const val STORY_DETAIL = "story_detail"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        intent.getParcelableExtra<Story>(STORY_DETAIL)?.let {
            data = it
        }

        with(binding) {
            Glide.with(this@DetailActivity)
                .load(data.photoUrl)
                .into(photoDetailIv)
            nameDetailTv.text = data.name
            descDetailTv.text = data.description
            locationBtn.contentDescription = getString(R.string.location_content_description, data.name)
            locationBtn.setOnClickListener {
                val toLocation = Intent(this@DetailActivity, LocationActivity::class.java)
                toLocation.putExtra(STORY_DETAIL, data)
                startActivity(toLocation)
            }
        }
    }
}