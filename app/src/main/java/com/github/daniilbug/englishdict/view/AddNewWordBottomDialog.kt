package com.github.daniilbug.englishdict.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.Navigation
import com.github.daniilbug.englishdict.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.dialog_find_definition.*
import kotlinx.android.synthetic.main.dialog_find_definition.view.*

class AddNewWordBottomDialog: BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_find_definition, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findWordEditText.doAfterTextChanged { text ->
            findButton.isEnabled = !text.isNullOrEmpty()
        }
        view.findButton.setOnClickListener { navigateToDefinition(view.findWordEditText.text.toString()) }
    }

    private fun navigateToDefinition(word: String) = activity?.let {
        Navigation.findNavController(it, R.id.navContainer)
            .navigate(R.id.findWordDefinition, bundleOf("word" to word))
        dismiss()
    }
}