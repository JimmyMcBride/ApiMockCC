package com.example.tmobilecc.data.repository

import android.util.Log
import com.example.tmobilecc.data.remote.ApiService
import com.example.tmobilecc.domain.models.Page
import com.example.tmobilecc.domain.repository.ApiaryMockRepository
import com.example.tmobilecc.domain.utils.NetworkState

class ApiaryMockRepositoryImpl(
	private val apiService: ApiService,
) : ApiaryMockRepository {
	override suspend fun getHomePageFeeds(): NetworkState<Page> {
		return try {
			val res = apiService.getHomePageFeeds()
			if (res.isSuccessful && res.body() != null) {
				Log.d("ApiaryMockRepositoryImpl", "getHomePageFeeds: ${res.body()}")
				NetworkState.Success(data = res.body()!!.page)
			} else
				NetworkState.Error(message = "Something went wrong.")
		} catch (e: Exception) {
			NetworkState.Error(message = e.message.toString())
		}
	}
}
