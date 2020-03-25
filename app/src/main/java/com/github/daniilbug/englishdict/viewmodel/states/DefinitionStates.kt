package com.github.daniilbug.englishdict.viewmodel.states

import com.github.daniilbug.englishdict.model.DictionaryDefinition

sealed class DefinitionState {
    class Error(val message: String): DefinitionState()
    class Definition(val definition: DictionaryDefinition): DefinitionState()
    object Loading: DefinitionState()
}