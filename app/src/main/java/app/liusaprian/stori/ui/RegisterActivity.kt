package app.liusaprian.stori.ui

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

        with(binding) {
            registerBtn.setOnClickListener {
                if(nameEt.text.isNotEmpty() && emailEt.text!!.isNotEmpty() && passEt.text!!.isNotEmpty() && emailEt.error == null && passEt.error == null) {
                    authViewModel.register(nameEt.text.toString(), emailEt.text.toString(), passEt.text.toString())
                    authViewModel.isRegister.observe(this@RegisterActivity) {
                        Toast.makeText(this@RegisterActivity, it, Toast.LENGTH_SHORT).show()
                        if(it == "User Created") {
                            finish()
                        }
                    }
                } else Toast.makeText(this@RegisterActivity, "Fields must not be empty or have errors", Toast.LENGTH_SHORT).show()
            }
        }

        binding.loginTv.setOnClickListener {
            finish()
        }
    }
}