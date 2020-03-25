package com.github.daniilbug.englishdict.model

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

@ExperimentalCoroutinesApi
class CachedDictionaryRepository(
    private val persistence: SavedDictionaryRepository,
    private val api: DictionaryRepository
): SavedDictionaryRepository by persistence {

    override fun getDefinitionsFor(word: String): Flow<DictionaryDefinition> {
        return persistence.getDefinitionsFor(word).catch { ex ->
            if (ex is DefinitionNotFoundError)
                emitAll(api.getDefinitionsFor(word).onEach { persistence.addDefinition(it) })
            else
                throw ex
        }
    }
}