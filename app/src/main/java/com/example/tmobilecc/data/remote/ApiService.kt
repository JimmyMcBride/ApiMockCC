package com.example.tmobilecc.data.remote

import com.example.tmobilecc.domain.models.HomePageResponse
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
	@GET("test/home")
	suspend fun getHomePageFeeds(): Response<HomePageResponse>
}
