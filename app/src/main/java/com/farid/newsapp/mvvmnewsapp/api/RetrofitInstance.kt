package com.farid.newsapp.mvvmnewsapp.api

import com.farid.newsapp.util.Constants.Companion.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {

    companion object{
        // lazy means that we initialize this once (only one time)
        private val retrofit by lazy {
            // it is used to loge the responses of retrofit which is useful for debugging
            // to let us see which requests we are making what the responses are
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()

            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }

        // actual api object that will be used in athe whole project
        val api by lazy {
            retrofit.create(NewsAPI::class.java)
        }
    }
}