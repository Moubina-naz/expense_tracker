package com.example.expensetracker.data.api
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
object RetrofitInstance {
    private const val BASE_URL = "http://10.91.168.163:8000/" // Emulator to localhost
    //192.168.29.175  10.91.168.163

    // Store token in memory (you should also save to Secure Storage)
    private var authToken: String? = null

    fun setAuthToken(token: String?) {
        authToken = token
    }
    fun getAuthToken(): String? {
        return authToken
    }

    private fun getOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val authInterceptor = Interceptor { chain ->
            val originalRequest = chain.request()
            val requestBuilder = originalRequest.newBuilder()

            // Add auth token if available
            authToken?.let { token ->
                requestBuilder.header("Authorization", "Bearer $token")
            }

            // Add common headers
            requestBuilder.header("Content-Type", "application/json")
            requestBuilder.header("Accept", "application/json")

            chain.proceed(requestBuilder.build())
        }

        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(getOkHttpClient())
            .build()
            .create(ApiService::class.java)
    }
}