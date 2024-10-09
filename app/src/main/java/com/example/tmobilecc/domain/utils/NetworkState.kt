package com.example.tmobilecc.domain.utils

import androidx.compose.runtime.Composable

sealed class NetworkState<out T> {
	object Idle : NetworkState<Nothing>()
	object Loading : NetworkState<Nothing>()
	data class Success<T>(val data: T) : NetworkState<T>()

	// I could make this error handle network errors better by adding status code from the response
	data class Error(val message: String) : NetworkState<Nothing>()
}

@Composable
fun <T> NetworkState<T>.DuringComposableState(
	success: @Composable (T) -> Unit = {},
	error: @Composable (String) -> Unit = {},
	idle: @Composable () -> Unit = {},
	loading: @Composable () -> Unit = {},
) {
	when (this) {
		is NetworkState.Success -> success(this.data)
		is NetworkState.Error -> error(this.message)
		is NetworkState.Idle -> idle()
		is NetworkState.Loading -> loading()
	}
}
