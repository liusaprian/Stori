package app.liusaprian.stori.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.liusaprian.stori.data.StoriRepository
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: StoriRepository) : ViewModel() {

    private val _isRegister = MutableLiveData<Boolean>()
    val isRegister: LiveData<Boolean> = _isRegister

    private val _isLogin = MutableLiveData(repository.isLogin)
    val isLogin: LiveData<Boolean> = _isLogin

    fun login(email: String, password: String) = viewModelScope.launch {
        _isLogin.postValue(repository.loginUser(email, password))
    }

    fun register(name: String, email: String, password: String) = viewModelScope.launch {
        _isRegister.postValue(repository.registerUser(name, email, password))
    }

    fun logout() = viewModelScope.launch {
        repository.logoutUser()

    }

    fun getPreference(key: String) = repository.getFromPreference(key)

    fun saveToPreference(key: String, value: String) = repository.saveToPreference(key, value)
}