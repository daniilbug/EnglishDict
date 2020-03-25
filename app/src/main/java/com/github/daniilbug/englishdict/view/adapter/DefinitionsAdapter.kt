package com.github.daniilbug.englishdict.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.daniilbug.englishdict.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_definition.*

class DefinitionsAdapter: RecyclerView.Adapter<DefinitionsAdapter.ViewHolder>() {
    private val definitions = mutableListOf<String>()

    fun submitDefinitions(defs: List<String>) {
        definitions.clear()
        definitions.addAll(defs)
        notifyDataSetChanged()
    }

    override fun getItemCount() = definitions.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_definition, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), LayoutContainer {

        override val containerView: View?
            get() = itemView

        fun bind(position: Int) {
            definitionText.text = definitions[position]
        }
    }
}