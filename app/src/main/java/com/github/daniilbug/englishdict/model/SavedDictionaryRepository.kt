package com.github.daniilbug.englishdict.model

import kotlinx.coroutines.flow.Flow

interface SavedDictionaryRepository: DictionaryRepository {
    fun getSavedDefinitions(query: String = ""): Flow<List<DictionaryDefinition>>
    suspend fun deleteDefinition(word: String)
    suspend fun addDefinition(word: DictionaryDefinition)
}

class DefinitionNotFoundError(): Error()