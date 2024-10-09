package com.example.tmobilecc.domain.repository

import com.example.tmobilecc.domain.models.Page
import com.example.tmobilecc.domain.utils.NetworkState

interface ApiaryMockRepository {
	suspend fun getHomePageFeeds(): NetworkState<Page>
}
