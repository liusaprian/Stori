package app.liusaprian.stori.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.viewbinding.library.activity.viewBinding
import android.widget.Toast
import app.liusaprian.stori.data.SessionManager
import app.liusaprian.stori.data.StoriRepository
import app.liusaprian.stori.databinding.ActivityLoginBinding
import app.liusaprian.stori.viewmodel.AuthViewModel

class LoginActivity : AppCompatActivity() {

    private val binding: ActivityLoginBinding by viewBinding()
    private val authViewModel by lazy {
        AuthViewModel(
            StoriRepository.getInstance(
                SessionManager(this.applicationContext)
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        authViewModel.isLogin.observe(this) {
            if (it) goingHome()
            else {
                if(authViewModel.getPreference(HomeActivity.JUST_LOGGED_OUT) != "true") {
                    Toast.makeText(this, "Invalid credential", Toast.LENGTH_SHORT).show()
                }
            }
        }
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        with(binding) {
            registerTv.setOnClickListener {
                val toRegister = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(toRegister)
            }
            loginBtn.setOnClickListener {
                if (emailEt.error == null && passEt.error == null && emailEt.text!!.isNotEmpty() && passEt.text!!.isNotEmpty()) {
                    authViewModel.saveToPreference(HomeActivity.JUST_LOGGED_OUT, "false")
                    authViewModel.login(emailEt.text.toString(), passEt.text.toString())
                } else Toast.makeText(
                        this@LoginActivity,
                        "Fields must not be empty or have errors",
                        Toast.LENGTH_SHORT
                    ).show()
            }
        }
    }

    private fun goingHome() {
        val toHome = Intent(this, HomeActivity::class.java)
        toHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(toHome)
        finish()
    }
}