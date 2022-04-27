package app.liusaprian.stori.data

import app.liusaprian.stori.network.ApiConfig
import app.liusaprian.stori.network.response.Story
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class StoriRepository(private val session: SessionManager) {
    private val service = ApiConfig().getApiService()

    companion object {
        @Volatile
        private var instance: StoriRepository? = null

        fun getInstance(session: SessionManager): StoriRepository =
            instance ?: synchronized(this) {
                instance
                    ?: StoriRepository(session)
            }

        const val TOKEN = "token"
        const val USER_ID = "user_id"
        const val NAME = "name"
        const val EMAIL = "email"
        const val PASSWORD = "password"
    }

    suspend fun loginUser(email: String, password: String) : Boolean {
        return withContext(Dispatchers.IO) {
            val jsonObject = JSONObject()
            jsonObject.put("email", email)
            jsonObject.put("password", password)
            val requestBody = jsonObject.toString().toRequestBody("application/json".toMediaTypeOrNull())
            try {
                val auth = service.login(requestBody)
                if(!auth.error!!) {
                    session.createLoginSession()
                    with(session) {
                        saveToPreference(EMAIL, email)
                        saveToPreference(PASSWORD, password)
                        saveToPreference(NAME, auth.loginResult?.name!!)
                        saveToPreference(USER_ID, auth.loginResult.userId!!)
                        saveToPreference(TOKEN, auth.loginResult.token!!)
                    }
                }
                true
            } catch (e: Exception) {
                false
            }
        }
    }

    suspend fun registerUser(name: String, email: String, password: String) : Boolean {
        val jsonObject = JSONObject()
        jsonObject.put("name", name)
        jsonObject.put("email", email)
        jsonObject.put("password", password)
        val requestBody = jsonObject.toString().toRequestBody("application/json".toMediaTypeOrNull())
        return try {
            service.register(requestBody)
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun getStories() : List<Story> {
        val headerMap = mutableMapOf<String, String>()
        headerMap["Authorization"] = "Bearer ${getFromPreference(TOKEN)}"
        return service.getStories(headerMap).listStory
    }

    var isLogin = session.isLogin

    fun logoutUser() = session.logout()

    fun getFromPreference(key: String) = session.getFromPreference(key)

    fun saveToPreference(key: String, value: String) = session.saveToPreference(key, value)
}