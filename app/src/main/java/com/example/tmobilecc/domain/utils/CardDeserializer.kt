package com.example.tmobilecc.domain.utils

import com.example.tmobilecc.domain.models.CardInfo
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import java.lang.reflect.Type

class CardDeserializer : JsonDeserializer<CardInfo> {
	override fun deserialize(
		json: JsonElement, typeOfT: Type, context: JsonDeserializationContext,
	): CardInfo {
		val jsonObject = json.asJsonObject
		val cardType = jsonObject["card_type"].asString

		return when (cardType) {
			"text" -> context.deserialize<CardInfo.TextCardInfo>(
				jsonObject["card"],
				CardInfo.TextCardInfo::class.java
			)

			"title_description" -> context.deserialize<CardInfo.TitleDescriptionCardInfo>(
				jsonObject["card"],
				CardInfo.TitleDescriptionCardInfo::class.java
			)

			"image_title_description" -> context.deserialize<CardInfo.ImageTitleDescriptionCardInfo>(
				jsonObject["card"],
				CardInfo.ImageTitleDescriptionCardInfo::class.java
			)

			else -> throw JsonParseException("Unknown card type: $cardType")
		}
	}
}
