package app.liusaprian.stori.network

import app.liusaprian.stori.network.response.AuthResponse
import app.liusaprian.stori.network.response.FileUploadResponse
import app.liusaprian.stori.network.response.StoryResponse
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ApiService {
    @POST("login")
    suspend fun login(
        @Body requestBody: RequestBody
    ) : AuthResponse

    @POST("register")
    suspend fun register(
        @Body requestBody: RequestBody
    ) : AuthResponse

    @GET("stories")
    suspend fun getStories(
        @HeaderMap headers: Map<String, String>,
        @Query("location") location: Int = 1
    ) : StoryResponse

    @Multipart
    @POST("stories")
    suspend fun addStory(
        @HeaderMap headers: Map<String, String>,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): FileUploadResponse
}

class ApiConfig {
    fun getApiService(): ApiService {
        val loggingInterceptor = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://story-api.dicoding.dev/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(ApiService::class.java)
    }
}
