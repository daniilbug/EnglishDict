@file:Suppress("UNCHECKED_CAST")

package com.github.daniilbug.englishdict.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.daniilbug.englishdict.di.AppComponent
import com.github.daniilbug.englishdict.viewmodel.SavedDictionaryViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
class SavedDictionaryFactory(private val appComponent: AppComponent): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when(modelClass){
            SavedDictionaryViewModel::class.java -> SavedDictionaryViewModel(
                appComponent
            ) as T
            else -> super.create(modelClass)
        }
    }
}