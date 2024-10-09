package com.example.tmobilecc.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.tmobilecc.presentation.screens.home.HomeScreen
import com.example.tmobilecc.presentation.screens.home.HomeViewModel
import com.example.tmobilecc.presentation.theme.TMobileCCTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()
		setContent {
			TMobileCCTheme {
				val viewModel by viewModels<HomeViewModel>()
				HomeScreen(
					viewModel.homePageFeeds,
					viewModel::loadHomePageFeeds
				)
			}
		}
	}
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
	Text(
		text = "Hello, $name!",
		modifier = modifier
	)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
	TMobileCCTheme {
		Greeting("Android")
	}
}
