package com.github.daniilbug.englishdict.model

import kotlinx.coroutines.flow.Flow

interface DictionaryRepository {
    fun getDefinitionsFor(word: String): Flow<DictionaryDefinition>
}