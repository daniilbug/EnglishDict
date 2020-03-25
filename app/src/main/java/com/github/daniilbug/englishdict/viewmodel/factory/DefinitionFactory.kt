@file:Suppress("UNCHECKED_CAST")

package com.github.daniilbug.englishdict.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.daniilbug.englishdict.di.AppComponent
import com.github.daniilbug.englishdict.viewmodel.DefinitionViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class DefinitionFactory(private val appComponent: AppComponent, private val word: String): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when(modelClass){
            DefinitionViewModel::class.java -> DefinitionViewModel(appComponent, word) as T
            else -> super.create(modelClass)
        }
    }
}