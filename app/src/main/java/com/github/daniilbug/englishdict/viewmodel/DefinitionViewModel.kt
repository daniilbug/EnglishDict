package com.github.daniilbug.englishdict.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.github.daniilbug.englishdict.R.string.*
import com.github.daniilbug.englishdict.di.AppComponent
import com.github.daniilbug.englishdict.model.SavedDictionaryRepository
import com.github.daniilbug.englishdict.model.provider.interfaces.StringResolver
import com.github.daniilbug.englishdict.viewmodel.states.DefinitionState
import com.squareup.moshi.JsonDataException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import java.net.UnknownHostException
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
class DefinitionViewModel(appComponent: AppComponent, word: String): ViewModel() {

    @Inject
    lateinit var dictionaryRepository: SavedDictionaryRepository

    @Inject
    lateinit var stringResolver: StringResolver

    val state: LiveData<DefinitionState>

    init {
        appComponent.inject(this)
        state = dictionaryRepository.getDefinitionsFor(word)
            .map { definition -> DefinitionState.Definition(definition) as DefinitionState }
            .catch { ex -> emit(eventFromError(ex)) }
            .onStart { emit(DefinitionState.Loading) }
            .asLiveData(viewModelScope.coroutineContext)
    }

    private fun eventFromError(ex: Throwable) = when(ex) {
        is NoSuchElementException, is JsonDataException -> DefinitionState.Error(stringResolver.getString(definition_does_not_exist))
        is UnknownHostException -> DefinitionState.Error(stringResolver.getString(internet_error))
        else -> DefinitionState.Error(stringResolver.getString(something_wrong))
    }.also { ex.printStackTrace() }

}