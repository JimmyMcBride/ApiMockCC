package com.example.tmobilecc.domain.models

sealed class CardInfo {
	data class TextCardInfo(
		val value: String,
		val attributes: TextAttributes,
	) : CardInfo()

	data class TitleDescriptionCardInfo(
		val title: Title,
		val description: Description,
	) : CardInfo()

	data class ImageTitleDescriptionCardInfo(
		val image: Image,
		val title: Title,
		val description: Description,
	) : CardInfo()
}
