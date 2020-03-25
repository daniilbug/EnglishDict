package com.github.daniilbug.englishdict.model.network

import com.github.daniilbug.englishdict.model.DictionaryDefinition
import com.github.daniilbug.englishdict.model.DictionaryRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow

@ExperimentalCoroutinesApi
class DictionaryApiRepository(private val api: DictionaryApi): DictionaryRepository {
    override fun getDefinitionsFor(word: String) = flow {
        val imageUrls = try {
            api.getImagesByWordAsync(word = word).await().hits.map(ApiImage::webFormatURL)
        } catch (e: Exception) {
            null
        }
        val definition = api.getDefinitionAsync(word).await().first()
        emit(DictionaryDefinition(definition.word, definition.shortdef, imageUrls ?: emptyList()))
    }
}
