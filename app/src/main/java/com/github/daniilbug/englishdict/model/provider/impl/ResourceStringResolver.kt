package com.github.daniilbug.englishdict.model.provider.impl

import android.content.res.Resources
import com.github.daniilbug.englishdict.model.provider.interfaces.StringResolver

class ResourceStringResolver(private val resources: Resources) : StringResolver {
    override fun getString(stringId: Int) = resources.getString(stringId)
    override fun getString(stringId: Int, vararg args: Any) = resources.getString(stringId, args)
}