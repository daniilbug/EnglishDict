package com.github.daniilbug.englishdict.view

import android.view.View
import androidx.appcompat.widget.SearchView
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
fun SearchView.asFlow(): Flow<String> = callbackFlow {
    val listener = object: SearchView.OnQueryTextListener {
        override fun onQueryTextChange(newText: String?): Boolean {
            launch {
                send(newText.toString())
            }
            return true
        }
        override fun onQueryTextSubmit(query: String?): Boolean { return true }
    }
    setOnQueryTextListener(listener)
    awaitClose { setOnQueryTextListener(null) }
}

fun View.scaleUpAnimation() {
    scaleX = 0f
    scaleY = 0f
    animate().scaleX(1.0f).scaleY(1.0f).start()
}