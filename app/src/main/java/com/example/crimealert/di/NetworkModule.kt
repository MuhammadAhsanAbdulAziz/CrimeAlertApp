package com.example.crimealert.di

import com.example.crimealert.api.AuthInterceptor
import com.example.crimealert.api.ComplaintApi
import com.example.crimealert.api.FeedbackApi
import com.example.crimealert.api.UserApi
import com.example.crimealert.utils.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {
    @Singleton
    @Provides
    fun provideRetrofitBuilder(): Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(authInterceptor).build()
    }

    @Singleton
    @Provides
    fun provideUserApi(retrofitBuilder: Retrofit.Builder): UserApi {
        return retrofitBuilder
            .build()
            .create(UserApi::class.java)
    }

    @Singleton
    @Provides
    fun provideFeedbackApi(
        retrofitBuilder: Retrofit.Builder,
        okHttpClient: OkHttpClient
    ): FeedbackApi {
        return retrofitBuilder
            .client(okHttpClient)
            .build()
            .create(FeedbackApi::class.java)
    }

    @Singleton
    @Provides
    fun provideComplaintApi(
        retrofitBuilder: Retrofit.Builder,
        okHttpClient: OkHttpClient
    ): ComplaintApi {
        return retrofitBuilder
            .client(okHttpClient)
            .build()
            .create(ComplaintApi::class.java)
    }

}