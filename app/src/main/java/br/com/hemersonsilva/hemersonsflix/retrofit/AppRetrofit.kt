package br.com.hemersonsilva.hemersonsflix.retrofit

import br.com.hemersonsilva.hemersonsflix.retrofit.service.MovieService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val API_BASE_URL = "https://api.themoviedb.org/3/"

class AppRetrofit {
    private val client by lazy {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
    }
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }
    val movieService: MovieService by lazy {
        retrofit.create(MovieService::class.java)
    }
}