package com.example.tmobilecc.domain.utils

import androidx.compose.runtime.Composable

sealed class NetworkState<out T> {
	object Idle : NetworkState<Nothing>()
	object Loading : NetworkState<Nothing>()
	data class Success<T>(val data: T) : NetworkState<T>()
	data class Error(val message: String) : NetworkState<Nothing>()
}

fun <T> NetworkState<T>.duringState(
	success: (T) -> Unit = {},
	error: (String) -> Unit = {},
	idle: () -> Unit = {},
	loading: () -> Unit = {},
): NetworkState<T> {
	when (this) {
		is NetworkState.Success -> success(this.data)
		is NetworkState.Error -> error(this.message)
		is NetworkState.Idle -> idle()
		is NetworkState.Loading -> loading()
	}
	return this
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
