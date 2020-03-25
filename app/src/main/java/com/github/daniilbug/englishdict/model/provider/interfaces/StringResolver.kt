package com.github.daniilbug.englishdict.model.provider.interfaces

interface StringResolver {
    fun getString(stringId: Int): String
    fun getString(stringId: Int, vararg args: Any): String
}