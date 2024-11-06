package com.example.quickybulkemail.di

import com.example.quickybulkemail.data.remote.SendGridService
import com.example.quickybulkemail.data.repository.EmailRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object HiltModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.sendgrid.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun provideApiService(retrofit: Retrofit): SendGridService {
        return retrofit.create(SendGridService::class.java)
    }

    @Provides
    fun provideEmailRepository(apiService: SendGridService): EmailRepository {
        return EmailRepository(apiService)
    }


}