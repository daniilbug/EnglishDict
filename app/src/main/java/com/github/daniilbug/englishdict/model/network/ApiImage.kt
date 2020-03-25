package com.github.daniilbug.englishdict.model.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class ApiImage(
    @field:Json(name = "webformatURL")
    val webFormatURL: String
)

@JsonClass(generateAdapter = true)
class ApiPixabayAnswer(
    @field:Json(name = "hits")
    val hits: List<ApiImage>
)