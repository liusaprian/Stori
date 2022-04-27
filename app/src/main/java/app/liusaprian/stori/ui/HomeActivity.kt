package app.liusaprian.stori.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.viewbinding.library.activity.viewBinding
import app.liusaprian.stori.data.SessionManager
import app.liusaprian.stori.data.StoriRepository
import app.liusaprian.stori.databinding.ActivityHomeBinding
import app.liusaprian.stori.viewmodel.AuthViewModel

class HomeActivity : AppCompatActivity() {

    private val authViewModel by lazy {
        AuthViewModel(
            StoriRepository.getInstance(
                SessionManager(this.applicationContext)
            )
        )
    }
    private val binding: ActivityHomeBinding by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        with(binding) {
            logoutBtn.setOnClickListener {
                authViewModel.logout()
                val toLogin = Intent(this@HomeActivity, LoginActivity::class.java)
                toLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(toLogin)
                finish()
            }
        }
    }
}