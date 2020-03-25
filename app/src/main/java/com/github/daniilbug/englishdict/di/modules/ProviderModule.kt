package com.github.daniilbug.englishdict.di.modules

import android.content.res.Resources
import com.github.daniilbug.englishdict.model.provider.impl.ResourceStringResolver
import com.github.daniilbug.englishdict.model.provider.interfaces.StringResolver
import dagger.Module
import dagger.Provides

@Module
class ProviderModule(private val resources: Resources) {
    @Provides
    fun stringResolver(): StringResolver = ResourceStringResolver(resources)
}