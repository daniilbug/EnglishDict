package com.github.daniilbug.englishdict.model.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class APIDictionaryDefinition(
    @field:Json(name = "meta")
    val meta: Meta,
    @field:Json(name = "shortdef")
    val shortdef: List<String>
)

@JsonClass(generateAdapter = true)
class Meta(
    @field:Json(name = "id")
    val id: String
)

val APIDictionaryDefinition.word: String get() {
    return if (":" in meta.id) meta.id.split(":").first() else meta.id
}