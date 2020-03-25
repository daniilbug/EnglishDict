package com.github.daniilbug.englishdict.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.daniilbug.englishdict.R
import com.github.daniilbug.englishdict.model.DictionaryDefinition
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_saved_word.*

class SavedDictAdapter(
    private val clickCallback: (DictionaryDefinition) -> Unit
): ListAdapter<DictionaryDefinition, SavedDictAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_saved_word, parent, false)
        val holder = ViewHolder(view)
        return holder.apply {
            itemView.setOnClickListener { onClick(adapterPosition) }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), LayoutContainer {
        override val containerView: View?
            get() = itemView

        val item: DictionaryDefinition get() = getItem(adapterPosition)

        fun onClick(position: Int) {
            clickCallback(getItem(position))
        }

        fun bind(position: Int) = with(getItem(position)){
            wordText.text = word
            wordDefinition.text = definitions.first()
        }
    }

    class DiffCallback: DiffUtil.ItemCallback<DictionaryDefinition>() {
        override fun areContentsTheSame(
            oldItem: DictionaryDefinition,
            newItem: DictionaryDefinition
        ): Boolean {
            return oldItem == newItem
        }

        override fun areItemsTheSame(
            oldItem: DictionaryDefinition,
            newItem: DictionaryDefinition
        ): Boolean {
            return oldItem.word == newItem.word
        }
    }
}