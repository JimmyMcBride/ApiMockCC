package com.example.tmobilecc.domain.models

import com.google.gson.annotations.SerializedName

data class TextAttributes(
	@SerializedName("text_color")
	val textColor: String,
	@SerializedName("font")
	val fontInfo: FontInfo,
)
