package com.github.daniilbug.englishdict.viewmodel.events

import com.github.daniilbug.englishdict.model.DictionaryDefinition

sealed class SavedDictionaryEvent {
    class Search(val query: String): SavedDictionaryEvent()
    class RemoveDefinition(val word: DictionaryDefinition) : SavedDictionaryEvent()
}