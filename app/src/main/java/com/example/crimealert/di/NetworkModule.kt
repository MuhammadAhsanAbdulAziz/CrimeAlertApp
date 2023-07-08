package com.example.crimealert.di

import com.example.crimealert.api.UserApi
import com.example.crimealert.utils.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideRetrofitBuilder() : Retrofit.Builder {
        return  Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
    }
//
//    @Singleton
//    @Provides
//    fun provideOkHttpClient(authInterceptor: AuthInterceptor) : OkHttpClient{
//        return OkHttpClient.Builder().addInterceptor(authInterceptor).build()
//    }

    @Singleton
    @Provides
    fun provideUserApi(retrofitBuilder: Retrofit.Builder) : UserApi {
        return retrofitBuilder.build().create(UserApi::class.java)
    }

//    @Singleton
//    @Provides
//    fun provideNoteApi(retrofitBuilder: Retrofit.Builder,okHttpClient: OkHttpClient) : NoteApi{
//        return retrofitBuilder
//            .client(okHttpClient)
//            .build()
//            .create(NoteApi::class.java)
//    }
}