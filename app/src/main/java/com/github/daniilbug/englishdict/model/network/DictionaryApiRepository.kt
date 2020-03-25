package com.github.daniilbug.englishdict.model.network

import com.github.daniilbug.englishdict.model.DictionaryDefinition
import com.github.daniilbug.englishdict.model.DictionaryRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

@ExperimentalCoroutinesApi
class DictionaryApiRepository(private val dictionaryApi: DictionaryApi, private val imageApi: PixabyImageApi): DictionaryRepository {

    override fun getDefinitionsFor(word: String): Flow<DictionaryDefinition> {
        val definitionFlow = flow { emit(dictionaryApi.getDefinition(word).first()) }
        val imageFlow = flow { emit(imageApi.getImagesByWord(word).hits.map(ApiImage::webFormatURL)) }
            .catch { emit(emptyList()) }

        return definitionFlow.zip(imageFlow) { def, images ->
            DictionaryDefinition(def.word, def.shortdef, images)
        }
    }
}
