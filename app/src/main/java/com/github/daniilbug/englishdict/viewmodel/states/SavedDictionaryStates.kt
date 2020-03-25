package com.github.daniilbug.englishdict.viewmodel.states

import com.github.daniilbug.englishdict.model.DictionaryDefinition

sealed class SavedDictionaryState {
    object Loading: SavedDictionaryState()
    object Empty: SavedDictionaryState()
    class Loaded(val words: List<DictionaryDefinition>): SavedDictionaryState()
    class Error(val message: String): SavedDictionaryState()
}