package app.liusaprian.stori.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.viewbinding.library.activity.viewBinding
import androidx.recyclerview.widget.LinearLayoutManager
import app.liusaprian.stori.data.SessionManager
import app.liusaprian.stori.data.StoriRepository
import app.liusaprian.stori.databinding.ActivityHomeBinding
import app.liusaprian.stori.ui.auth.LoginActivity
import app.liusaprian.stori.viewmodel.AuthViewModel
import app.liusaprian.stori.viewmodel.StoryViewModel

class HomeActivity : AppCompatActivity() {

    companion object {
        const val JUST_LOGGED_OUT = "just_logged_out"
    }

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

        val storyAdapter = StoryAdapter()

        storyViewModel.stories.observe(this) { storyAdapter.setData(it) }

        with(binding) {
            logoutBtn.setOnClickListener {
                authViewModel.logout()
                authViewModel.saveToPreference(JUST_LOGGED_OUT, "true")
                val toLogin = Intent(this@HomeActivity, LoginActivity::class.java)
                toLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(toLogin)
                finish()
            }
            storyRv.adapter = storyAdapter
            storyRv.layoutManager = LinearLayoutManager(this@HomeActivity)
        }
    }
}