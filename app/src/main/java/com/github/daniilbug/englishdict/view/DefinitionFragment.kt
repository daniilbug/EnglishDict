package com.github.daniilbug.englishdict.view

import android.os.Bundle
import android.os.Handler
import android.speech.tts.TextToSpeech
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.github.daniilbug.englishdict.App
import com.github.daniilbug.englishdict.R
import com.github.daniilbug.englishdict.model.DictionaryDefinition
import com.github.daniilbug.englishdict.view.adapter.DefinitionsAdapter
import com.github.daniilbug.englishdict.viewmodel.DefinitionViewModel
import com.github.daniilbug.englishdict.viewmodel.factory.DefinitionFactory
import com.github.daniilbug.englishdict.viewmodel.states.DefinitionState
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_definition.view.*
import kotlinx.android.synthetic.main.layout_message.view.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import java.util.*

@FlowPreview
@ExperimentalCoroutinesApi
class DefinitionFragment: Fragment(R.layout.fragment_definition) {
    companion object {
        private const val WORD_KEY = "word"

        fun forWord(word: String) = DefinitionFragment().apply {
            arguments = bundleOf(WORD_KEY to word)
        }
    }

    private val adapter = DefinitionsAdapter()
    private lateinit var textToSpeech: TextToSpeech

    private val viewModel by viewModels<DefinitionViewModel> {
        DefinitionFactory(
            App.component(activity?.applicationContext),
            arguments?.getString(WORD_KEY) ?: error("You must put a 'word' argument to arguments of the fragment")
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.state.observe(viewLifecycleOwner) { state -> setState(state) }
        view.definitionsRecycler.adapter = adapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val activity = this.activity ?: return
        textToSpeech = TextToSpeech(activity) { }
        textToSpeech.language = Locale.UK
    }

    private fun setState(state: DefinitionState) {
        when(state){
            is DefinitionState.Loading -> view?.showLoading()
            is DefinitionState.Definition -> view?.showDefinition(state.definition)
            is DefinitionState.Error -> view?.showError(state.message)
        }
    }

    private fun View.showError(message: String) {
        loadingBar.visibility = View.GONE
        definitionCard.visibility = View.GONE
        speakButton.hide()
        with(errorViewStub.inflate()) {
            messageText.text = message
            scaleUpAnimation()
        }
    }

    private fun View.showDefinition(definition: DictionaryDefinition) {
        loadingBar.visibility = View.GONE
        with(definitionCard) {
            visibility = View.VISIBLE
            scaleUpAnimation()
        }
        adapter.submitDefinitions(definition.definitions)
        titleText.text = definition.word
        if (definition.imageUrls.isNotEmpty())
            Picasso.get().load(definition.imageUrls.random()).fit().into(definitionImage)
        Handler().postDelayed({ speakButton.show() }, 200)
        speakButton.setOnClickListener { textToSpeech.speak(definition.word, TextToSpeech.QUEUE_FLUSH, null, null) }
    }

    private fun View.showLoading() {
        loadingBar.visibility = View.VISIBLE
        speakButton.hide()
        definitionCard.visibility = View.GONE
    }
}