package com.github.daniilbug.englishdict.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.github.daniilbug.englishdict.R.string.something_wrong
import com.github.daniilbug.englishdict.di.AppComponent
import com.github.daniilbug.englishdict.model.DictionaryDefinition
import com.github.daniilbug.englishdict.model.SavedDictionaryRepository
import com.github.daniilbug.englishdict.model.provider.interfaces.StringResolver
import com.github.daniilbug.englishdict.viewmodel.events.SavedDictionaryEvent
import com.github.daniilbug.englishdict.viewmodel.states.SavedDictionaryState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
class SavedDictionaryViewModel(appComponent: AppComponent) : ViewModel() {
    @Inject
    lateinit var repository: SavedDictionaryRepository

    @Inject
    lateinit var stringResolver: StringResolver

    private val searchChannel: BroadcastChannel<String> = BroadcastChannel(BUFFERED)

    val state: LiveData<SavedDictionaryState>

    init {
        appComponent.inject(this)
        state = searchChannel.asFlow()
            .onStart { emit("") }
            .debounce(500L)
            .flatMapLatest { query -> repository.getSavedDefinitions(query).map { query to it } }
            .map { queryAndWords -> val (query, words) = queryAndWords; stateFromWordList(words, query) }
            .catch { ex -> emit(stateFromError(ex)) }
            .onStart { emit(SavedDictionaryState.Loading) }
            .distinctUntilChanged()
            .asLiveData(viewModelScope.coroutineContext)
    }

    private fun stateFromError(ex: Throwable) = SavedDictionaryState.Error(stringResolver.getString(something_wrong))
        .also { ex.printStackTrace() }

    fun postEvent(event: SavedDictionaryEvent) {
        when (event) {
            is SavedDictionaryEvent.RemoveDefinition -> removeDefinition(event.word)
            is SavedDictionaryEvent.Search -> searchRequest(event.query)
        }
    }

    private fun searchRequest(query: String) = viewModelScope.launch {
        searchChannel.send(query)
    }

    private fun removeDefinition(word: DictionaryDefinition) = viewModelScope.launch {
        repository.deleteDefinition(word.word)
    }

    private fun stateFromWordList(
        words: List<DictionaryDefinition>,
        query: String
    ) = if (words.isEmpty() && query.isEmpty())
        SavedDictionaryState.Empty
    else
        SavedDictionaryState.Loaded(words)
}