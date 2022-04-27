package app.liusaprian.stori.ui.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.viewbinding.library.activity.viewBinding
import android.widget.Toast
import app.liusaprian.stori.data.SessionManager
import app.liusaprian.stori.data.StoriRepository
import app.liusaprian.stori.databinding.ActivityRegisterBinding
import app.liusaprian.stori.viewmodel.AuthViewModel

class RegisterActivity : AppCompatActivity() {

    private val binding: ActivityRegisterBinding by viewBinding()
    private val authViewModel by lazy {
        AuthViewModel(
            StoriRepository.getInstance(
                SessionManager(this.applicationContext)
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        authViewModel.isRegister.observe(this@RegisterActivity) {
            if(it) {
                Toast.makeText(this@RegisterActivity, "User Created", Toast.LENGTH_SHORT).show()
                finish()
            } else Toast.makeText(this@RegisterActivity, "Email is already taken", Toast.LENGTH_SHORT).show()
        }
        with(binding) {
            registerBtn.setOnClickListener {
                if(nameEt.text.isNotEmpty() && emailEt.text!!.isNotEmpty() && passEt.text!!.isNotEmpty() && emailEt.error == null && passEt.error == null) {
                    authViewModel.register(nameEt.text.toString(), emailEt.text.toString(), passEt.text.toString())
                } else Toast.makeText(this@RegisterActivity, "Fields must not be empty or have errors", Toast.LENGTH_SHORT).show()
            }
        }

        binding.loginTv.setOnClickListener {
            finish()
        }
    }
}