package com.example.tmobilecc.test

import com.example.tmobilecc.domain.models.Page
import com.example.tmobilecc.domain.repository.ApiaryMockRepository
import com.example.tmobilecc.domain.utils.NetworkState
import com.example.tmobilecc.presentation.screens.home.HomeViewModel
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

	private lateinit var mockRepository: ApiaryMockRepository
	private lateinit var viewModel: HomeViewModel

	@Before
	fun setup() {
		Dispatchers.setMain(StandardTestDispatcher())
		mockRepository = mock(ApiaryMockRepository::class.java)
		viewModel = HomeViewModel(mockRepository)
	}

	// Clean up the Main dispatcher after each test
	@After
	fun tearDown() {
		Dispatchers.resetMain()
	}


	@Test
	fun `loadHomePageFeeds sets Loading and then Success state`() = runTest {
		// Mock repository to return a successful response
		val mockPage = Page(
			cards = listOf()
		)
		whenever(mockRepository.getHomePageFeeds()).thenReturn(NetworkState.Success(mockPage))

		// Initially, state should be Idle
		assertEquals(NetworkState.Idle, viewModel.homePageFeeds.value)

		// Trigger loading
		viewModel.loadHomePageFeeds()

		// Verify that the state is set to Loading
		assertEquals(NetworkState.Loading, viewModel.homePageFeeds.value)

		// Advance the coroutine until idle
		advanceUntilIdle()

		// After the coroutine, the state should be Success with the mock data
		assertEquals(NetworkState.Success(mockPage), viewModel.homePageFeeds.value)
	}

	@Test
	fun `loadHomePageFeeds sets Loading and then Error state`() = runTest {
		// Mock repository to return an error
		val errorMessage = "Error occurred"
		whenever(mockRepository.getHomePageFeeds()).thenReturn(NetworkState.Error(errorMessage))

		// Trigger loading
		viewModel.loadHomePageFeeds()

		// Verify that the state is set to Loading
		assertEquals(NetworkState.Loading, viewModel.homePageFeeds.value)

		// Advance the coroutine until idle
		advanceUntilIdle()

		// After the coroutine, the state should be Error
		assertEquals(NetworkState.Error(errorMessage), viewModel.homePageFeeds.value)
	}
}
