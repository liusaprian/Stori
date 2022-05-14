package app.liusaprian.stori.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.viewbinding.library.activity.viewBinding
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import app.liusaprian.stori.R
import app.liusaprian.stori.data.SessionManager
import app.liusaprian.stori.data.StoriRepository
import app.liusaprian.stori.databinding.ActivityHomeBinding
import app.liusaprian.stori.network.response.Story
import app.liusaprian.stori.ui.add.AddStoryActivity
import app.liusaprian.stori.ui.auth.LoginActivity
import app.liusaprian.stori.viewmodel.AuthViewModel
import app.liusaprian.stori.viewmodel.StoryViewModel

class HomeActivity : AppCompatActivity() {

    companion object {
        const val JUST_LOGGED_OUT = "just_logged_out"
        const val NEW_STORY_RESULT = 100
    }

    private lateinit var storyAdapter: StoryAdapter
    private val authViewModel by lazy {
        AuthViewModel(
            StoriRepository.getInstance(
                SessionManager(this.applicationContext)
            )
        )
    }

    private val storyViewModel by lazy {
        StoryViewModel(
            StoriRepository.getInstance(
                SessionManager(this)
            )
        )
    }
    private val binding: ActivityHomeBinding by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        storyViewModel.getStories()

        storyAdapter = StoryAdapter()

        with(binding) {
            logoutBtn.setOnClickListener {
                authViewModel.logout()
                authViewModel.saveToPreference(JUST_LOGGED_OUT, "true")
                val toLogin = Intent(this@HomeActivity, LoginActivity::class.java)
                toLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(toLogin)
                finish()
            }
            addStoryBtn.setOnClickListener {
                val toAddStory = Intent(this@HomeActivity, AddStoryActivity::class.java)
                launcherIntentNewStory.launch(toAddStory)
            }
            storyViewModel.stories.observe(this@HomeActivity) {
                progressBar.visibility = View.GONE
                if(!it.error) {
                    if(it.listStory.isNotEmpty()) {
                        storyViewModel.saveStories(it.listStory)
                        storyAdapter.setData(it.listStory)
                        storyRv.adapter = storyAdapter
                        storyRv.visibility = View.VISIBLE
                        errorMessage.visibility = View.GONE
                        storyRv.layoutManager = LinearLayoutManager(this@HomeActivity)
                    } else {
                        errorMessage.visibility = View.VISIBLE
                        errorMessage.text = getString(R.string.empty_story)
                    }
                } else {
                    errorMessage.visibility = View.VISIBLE
                    errorMessage.text = it.message
                }
            }
        }
    }

    private val launcherIntentNewStory = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result -> if (result.resultCode == NEW_STORY_RESULT) refresh() }

    private fun refresh() {
        finish()
        startActivity(intent)
        overridePendingTransition(0, 0)
    }
}