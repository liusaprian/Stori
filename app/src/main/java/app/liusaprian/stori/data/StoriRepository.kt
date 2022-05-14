package app.liusaprian.stori.data

import app.liusaprian.stori.network.ApiConfig
import app.liusaprian.stori.network.response.FileUploadResponse
import app.liusaprian.stori.network.response.Story
import app.liusaprian.stori.network.response.StoryResponse
import app.liusaprian.stori.utils.reduceFileImage
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.File

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

        const val STORIES = "stories"
    }

    suspend fun loginUser(email: String, password: String) : Boolean {
        return withContext(Dispatchers.IO) {
            val jsonObject = JSONObject()
            jsonObject.put(EMAIL, email)
            jsonObject.put(PASSWORD, password)
            val requestBody = jsonObject.toString().toRequestBody("application/json".toMediaTypeOrNull())
            try {
                val auth = service.login(requestBody)
                if(!auth.error) {
                    session.createLoginSession()
                    with(session) {
                        saveToPreference(EMAIL, email)
                        saveToPreference(PASSWORD, password)
                        saveToPreference(NAME, auth.loginResult?.name!!)
                        saveToPreference(USER_ID, auth.loginResult.userId)
                        saveToPreference(TOKEN, auth.loginResult.token)
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
        jsonObject.put(NAME, name)
        jsonObject.put(EMAIL, email)
        jsonObject.put(PASSWORD, password)
        val requestBody = jsonObject.toString().toRequestBody("application/json".toMediaTypeOrNull())
        return try {
            service.register(requestBody)
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun getStories() : StoryResponse {
        val headerMap = mutableMapOf<String, String>()
        headerMap["Authorization"] = "Bearer ${getFromPreference(TOKEN)}"
        return service.getStories(headerMap)
    }

    suspend fun addStory(photo: File, description: String) : FileUploadResponse? {
        val headerMap = mutableMapOf<String, String>()
        headerMap["Authorization"] = "Bearer ${getFromPreference(TOKEN)}"

        val descriptionRequestBody = description.toRequestBody("text/plain".toMediaType())
        val file = reduceFileImage(photo)
        val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "photo",
            file.name,
            requestImageFile
        )
        return try {
            service.addStory(headerMap, imageMultipart, descriptionRequestBody)
        } catch(e: Exception) {
            null
        }
    }

    var isLogin = session.isLogin

    fun logoutUser() = session.logout()

    fun getFromPreference(key: String) = session.getFromPreference(key)

    fun saveToPreference(key: String, value: String) = session.saveToPreference(key, value)

    fun saveStoryList(list: List<Story>) {
        val gson = Gson()
        val json: String = gson.toJson(list)
        saveToPreference(STORIES, json)
    }

    fun getStoryList(): ArrayList<Story> {
        val gson = Gson()
        val json = getFromPreference(STORIES)
        val type = object : TypeToken<ArrayList<Story>>() {}.type
        return gson.fromJson(json, type)
    }
}