package com.example.tmobilecc.di

import com.example.tmobilecc.data.remote.ApiService
import com.example.tmobilecc.data.repository.ApiaryMockRepositoryImpl
import com.example.tmobilecc.domain.models.CardInfo
import com.example.tmobilecc.domain.repository.ApiaryMockRepository
import com.example.tmobilecc.domain.utils.CardDeserializer
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
	@Provides
	@Singleton
	fun provideGson(): Gson {
		return GsonBuilder()
			.registerTypeAdapter(
				CardInfo::class.java,
				CardDeserializer()
			) // Register custom card deserializer
			.create()
	}

	@Provides
	@Singleton
	fun provideRetrofit(gson: Gson): Retrofit {
		return Retrofit.Builder()
			.baseUrl("https://private-8ce77c-tmobiletest.apiary-mock.com/")
			.addConverterFactory(GsonConverterFactory.create(gson))
			.build()
	}

	@Provides
	@Singleton
	fun provideApiService(retrofit: Retrofit): ApiService {
		return retrofit.create(ApiService::class.java)
	}

	@Provides
	@Singleton
	fun provideApiaryRepository(apiService: ApiService): ApiaryMockRepository {
		return ApiaryMockRepositoryImpl(apiService)
	}
}
