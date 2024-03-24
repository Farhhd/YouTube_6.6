package com.example.youtube_66.di

import com.example.youtube_66.data.YouTubeApiService
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.BuildConfig
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {

    single {
        dispatcherIO()
    }
    factory {
        provideYouTubeApiService(get())
    }
}

fun getService(): YouTubeApiService {
    val loggingInterceptor = HttpLoggingInterceptor().setLevel(
        if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
        else HttpLoggingInterceptor.Level.NONE
    )
    val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor { chain ->
            val url = chain.request().url
                .newBuilder()
                .addQueryParameter("key", "AIzaSyB9DdiQjgrMjOAn-kdaDM7sep-xKL2zPdo")
                .build()
            chain.proceed(chain.request().newBuilder().url(url).build())
        }.build()
    val retrofit = Retrofit.Builder()
        .baseUrl("https://www.googleapis.com/youtube/v3/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()
    return retrofit.create(YouTubeApiService::class.java)
}


fun provideYouTubeApiService(retrofit: Retrofit): YouTubeApiService {
    return retrofit.create(YouTubeApiService::class.java)
}

fun dispatcherIO() = Dispatchers.IO
