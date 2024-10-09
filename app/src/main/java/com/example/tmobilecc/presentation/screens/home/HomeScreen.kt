package com.example.tmobilecc.presentation.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.tmobilecc.domain.models.CardInfo
import com.example.tmobilecc.domain.models.Description
import com.example.tmobilecc.domain.models.FontInfo
import com.example.tmobilecc.domain.models.Image
import com.example.tmobilecc.domain.models.ImageSize
import com.example.tmobilecc.domain.models.Page
import com.example.tmobilecc.domain.models.TextAttributes
import com.example.tmobilecc.domain.models.Title
import com.example.tmobilecc.domain.utils.DuringComposableState
import com.example.tmobilecc.domain.utils.NetworkState
import com.example.tmobilecc.presentation.theme.ui.parseHexColor

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun HomeScreen(
	homePageFeeds: MutableState<NetworkState<Page>>,
	loadHomePageFeeds: () -> Unit = {},
) {
	loadHomePageFeeds()

	val homePageState by homePageFeeds

	Scaffold(
		modifier = Modifier
			.fillMaxSize(),
		containerColor = MaterialTheme.colorScheme.background
	) { innerPadding ->
		Column(
			modifier = Modifier
				.padding(innerPadding)
				.padding(8.dp)
		) {
			homePageState.DuringComposableState(
				success = { data ->
					LazyColumn(
						modifier = Modifier.fillMaxSize(),
						horizontalAlignment = Alignment.CenterHorizontally
					) {
						items(data.cards) { card ->
							when (card) {
								is CardInfo.ImageTitleDescriptionCardInfo -> {
									CardWithImageTitleDescription(
										image = card.image,
										title = card.title,
										description = card.description
									)
								}

								is CardInfo.TitleDescriptionCardInfo -> {
									AppCard {
										Column(modifier = Modifier.padding(8.dp)) {
											Text(
												text = card.title.value,
												color = parseHexColor(card.title.attributes.textColor),
												fontSize = card.title.attributes.fontInfo.size.sp
											)
											Spacer(modifier = Modifier.height(4.dp))
											Box(
												modifier = Modifier
													.height(1.dp)
													.fillMaxWidth()
													.background(Color(0x80333333))
											)
											Spacer(modifier = Modifier.height(4.dp))
											Text(
												text = card.description.value,
												color = parseHexColor(card.description.attributes.textColor),
												fontSize = card.description.attributes.fontInfo.size.sp
											)
										}

									}
								}

								is CardInfo.TextCardInfo -> {
									val attrs = card.attributes
									AppCard {
										Column(modifier = Modifier.padding(8.dp)) {
											Text(
												text = card.value,
												color = parseHexColor(attrs.textColor),
												fontSize = attrs.fontInfo.size.sp
											)
										}

									}
								}
							}
						}
					}
				},
				loading = {
					Column {
						Text("Loading...")
					}
				},
				// Would like to show a toast message when an error occurs give more time
				error = { errorMessage ->
					Column {
						Text(errorMessage)
					}
				}
			)
		}
	}
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun CardWithImageTitleDescription(
	image: Image,
	title: Title,
	description: Description,
) {
	AppCard {
		Box {
			// Display image using Glide with specified width and aspect ratio
			GlideImage(
				model = image.url,
				contentDescription = description.value,
				modifier = Modifier
					.fillMaxWidth() // Scale the image to fit the screen width
					.aspectRatio(image.size.width.toFloat() / image.size.height.toFloat()) // Preserve aspect ratio
			)

			// Column to display text overlay at the bottom of the image
			Column(
				modifier = Modifier
					.align(Alignment.BottomStart)
					.padding(8.dp)
			) {
				Text(
					text = title.value,
					color = parseHexColor(title.attributes.textColor),
					fontSize = title.attributes.fontInfo.size.sp,
					fontWeight = FontWeight.Bold
				)
				Text(
					text = description.value,
					color = parseHexColor(description.attributes.textColor),
					fontSize = description.attributes.fontInfo.size.sp
				)
			}
		}
	}
}

@Composable
fun AppCard(
	content: @Composable () -> Unit,
) {
	Card(
		modifier = Modifier
			.padding(vertical = 8.dp)
			.fillMaxWidth(),
		colors = CardDefaults.cardColors(
			containerColor = MaterialTheme.colorScheme.surface
		)
	) {
		content()
	}
}

@Preview
@Composable
fun CardWithImageTitleDescriptionPreview() {
	CardWithImageTitleDescription(
		image = Image(
			"https://via.placeholder.com/150x150",
			ImageSize(150, 150)
		),
		title = Title(
			value = "Title",
			TextAttributes("#FFFFFF", FontInfo(18))
		),
		description = Description(
			value = "Description",
			TextAttributes("#FFFFFF", FontInfo(12))
		)
	)
}
