package com.github.daniilbug.englishdict.view

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.os.postDelayed
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ItemTouchHelper
import com.github.daniilbug.englishdict.App
import com.github.daniilbug.englishdict.R
import com.github.daniilbug.englishdict.model.DictionaryDefinition
import com.github.daniilbug.englishdict.view.adapter.SavedDictAdapter
import com.github.daniilbug.englishdict.view.adapter.SavedDictTouchCallback
import com.github.daniilbug.englishdict.viewmodel.SavedDictionaryViewModel
import com.github.daniilbug.englishdict.viewmodel.events.SavedDictionaryEvent
import com.github.daniilbug.englishdict.viewmodel.factory.SavedDictionaryFactory
import com.github.daniilbug.englishdict.viewmodel.states.SavedDictionaryState
import kotlinx.android.synthetic.main.fragment_saved_dict.view.*
import kotlinx.android.synthetic.main.layout_message.view.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@FlowPreview
@ExperimentalCoroutinesApi
class SavedDictionaryFragment: Fragment(R.layout.fragment_saved_dict) {

    private val viewModel by viewModels<SavedDictionaryViewModel> {
        SavedDictionaryFactory(
            App.component(activity?.applicationContext)
        )
    }

    private lateinit var historyAdapter: SavedDictAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.state.observe(viewLifecycleOwner) { state -> setState(state) }
        view.newWordButton.setOnClickListener { findNewWord() }
        historyAdapter = SavedDictAdapter(clickCallback = { historyWord -> navigateToDefinition(historyWord.word)})
        view.historyRecycler.adapter = historyAdapter
        val touchHelper = ItemTouchHelper(
            SavedDictTouchCallback(removeCallback = { word -> viewModel.postEvent(SavedDictionaryEvent.RemoveDefinition(word)) })
        )
        touchHelper.attachToRecyclerView(view.historyRecycler)
        view.initSearch()
    }

    private fun findNewWord() = activity?.let {
        AddNewWordBottomDialog().show(it.supportFragmentManager, AddNewWordBottomDialog::class.simpleName)
    }

    private fun setState(state: SavedDictionaryState) {
        when(state) {
            is SavedDictionaryState.Error -> view?.showError(state.message)
            is SavedDictionaryState.Loading -> view?.showLoading()
            is SavedDictionaryState.Loaded -> view?.showWords(state.words)
            is SavedDictionaryState.Empty -> view?.showEmpty()
        }
    }

    private fun View.showWords(words: List<DictionaryDefinition>) {
        setVisibilities(recycler = true, button = true, searchCard = true)
        historyAdapter.submitList(words)
        Handler().postDelayed(200) { newWordButton.show() }
    }

    private fun View.showLoading() = setVisibilities(loadingBar = true)

    private fun View.showError(message: String) = showMessageStub(message, R.drawable.ic_error, false)

    private fun View.showEmpty() = showMessageStub(
        getString(R.string.empty_list), R.drawable.ic_empty_list, true
    )

    private fun View.initSearch() = lifecycleScope.launch {
        //TODO()
        searchView.asFlow()
            .debounce(500L)
            .distinctUntilChanged()
            .collectLatest { query ->
                viewModel.postEvent(SavedDictionaryEvent.Search(query))
            }
    }

    private fun View.showMessageStub(message: String, @DrawableRes imageId: Int, showAddButton: Boolean) {
        setVisibilities(viewStub = true, button = showAddButton)
        messageViewStub?.inflate()
        findViewById<LinearLayout>(R.id.messageLayout)?.run {
            messageText.text = message
            messageImage.setImageDrawable(ContextCompat.getDrawable(context, imageId))
            scaleUpAnimation()
        }
    }

    private fun View.setVisibilities(
        recycler: Boolean = false,
        searchCard: Boolean = false,
        viewStub: Boolean = false,
        button: Boolean = false,
        loadingBar: Boolean = false
    ) {
        fun Boolean.toVisibility() = if (this) View.VISIBLE else View.GONE
        historyRecycler.visibility = recycler.toVisibility()
        searchCardView.visibility = searchCard.toVisibility()
        if (messageViewStub != null) {
            messageViewStub.visibility = viewStub.toVisibility()
        } else {
            findViewById<LinearLayout>(R.id.messageLayout).visibility = viewStub.toVisibility()
        }
        if (button)
            newWordButton.show()
        else
            newWordButton.hide()
        historyLoadingBar.visibility = loadingBar.toVisibility()
    }

    private fun navigateToDefinition(word: String) = activity?.let {
        Navigation.findNavController(it, R.id.navContainer)
            .navigate(R.id.findWordDefinition, bundleOf("word" to word))
    }
}
