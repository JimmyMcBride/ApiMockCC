package com.example.tmobilecc.presentation.screens.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tmobilecc.domain.models.Page
import com.example.tmobilecc.domain.repository.ApiaryMockRepository
import com.example.tmobilecc.domain.utils.NetworkState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
	private val repository: ApiaryMockRepository,
) : ViewModel() {
	var homePageFeeds = mutableStateOf<NetworkState<Page>>(NetworkState.Idle)
		private set

	fun loadHomePageFeeds() {
		homePageFeeds.value = NetworkState.Loading
		viewModelScope.launch {
			homePageFeeds.value = repository.getHomePageFeeds()
		}
	}
}
