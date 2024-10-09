package com.example.tmobilecc.di

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
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
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
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
	fun provideOkHttpClient(@ApplicationContext context: Context): OkHttpClient {
		// Define cache size and location
		val cacheSize = 10 * 1024 * 1024 // 10 MB
		val cache = Cache(context.cacheDir, cacheSize.toLong())

		return OkHttpClient.Builder()
			.cache(cache) // Enable caching
			.addInterceptor { chain ->
				var request = chain.request()
				request = if (hasNetwork(context)) {
					// When online, make network request and cache for 60 seconds
					request.newBuilder().header("Cache-Control", "public, max-age=" + 60).build()
				} else {
					// When offline, serve from cache up to 7 days old
					request.newBuilder()
						.header("Cache-Control", "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7)
						.build()
				}
				chain.proceed(request)
			}
			.build()
	}

	@Provides
	@Singleton
	fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit {
		return Retrofit.Builder()
			.client(okHttpClient) // Use the OkHttpClient with caching
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

	// Helper function to check network availability
	private fun hasNetwork(context: Context): Boolean {
		val connectivityManager =
			context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
		val network = connectivityManager.activeNetwork ?: return false
		val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
		return when {
			activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
			activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
			activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
			else -> false
		}
	}
}
