package com.example.tmobilecc.test

import com.example.tmobilecc.data.remote.ApiService
import com.example.tmobilecc.data.repository.ApiaryMockRepositoryImpl
import com.example.tmobilecc.domain.models.HomePageResponse
import com.example.tmobilecc.domain.models.Page
import com.example.tmobilecc.domain.utils.NetworkState
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class ApiaryMockRepositoryImplTest {

	private lateinit var apiService: ApiService
	private lateinit var repository: ApiaryMockRepositoryImpl

	@Before
	fun setUp() {
		apiService = mock(ApiService::class.java) // Mock the API service
		repository = ApiaryMockRepositoryImpl(apiService) // Pass mock to the repository
	}

	@Test
	fun `getHomePageFeeds returns Success when API call is successful`() = runTest {
		// Mock a successful response with a Page object
		val mockPage = Page(cards = listOf())
		val mockResponse = Response.success(HomePageResponse(mockPage))
		`when`(apiService.getHomePageFeeds()).thenReturn(mockResponse)

		// Call the repository function
		val result = repository.getHomePageFeeds()

		// Assert the result is Success
		assert(result is NetworkState.Success)
		assertEquals(mockPage, (result as NetworkState.Success).data)
	}

	@Test
	fun `getHomePageFeeds returns Error when API call is unsuccessful`() = runTest {
		// Mock an unsuccessful response (e.g., 400 error)
		val mockResponse = Response.error<HomePageResponse>(400, mockResponseBody())
		`when`(apiService.getHomePageFeeds()).thenReturn(mockResponse)

		// Call the repository function
		val result = repository.getHomePageFeeds()

		// Assert the result is Error
		assert(result is NetworkState.Error)
		assertEquals("Something went wrong.", (result as NetworkState.Error).message)
	}

	@Test
	fun `getHomePageFeeds returns Error when exception is thrown`() = runTest {
		// Mock an exception being thrown from the API call
		`when`(apiService.getHomePageFeeds()).thenThrow(RuntimeException("Network error"))

		// Call the repository function
		val result = repository.getHomePageFeeds()

		// Assert the result is Error with the correct message
		assert(result is NetworkState.Error)
		assertEquals("Network error", (result as NetworkState.Error).message)
	}

	// Helper method to mock response body for error case
	private fun mockResponseBody(): okhttp3.ResponseBody {
		return okhttp3.ResponseBody.create(
			okhttp3.MediaType.parse("application/json"),
			"{}" // Mock empty JSON response
		)
	}
}
