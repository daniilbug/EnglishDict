package com.github.daniilbug.englishdict.model

data class DictionaryDefinition(
    val word: String,
    val definitions: List<String>,
    val imageUrls: List<String>
)