package com.github.daniilbug.englishdict.di

import com.github.daniilbug.englishdict.di.modules.NetworkModule
import com.github.daniilbug.englishdict.di.modules.PersistentModule
import com.github.daniilbug.englishdict.di.modules.ProviderModule
import com.github.daniilbug.englishdict.viewmodel.DefinitionViewModel
import com.github.daniilbug.englishdict.viewmodel.SavedDictionaryViewModel
import dagger.Component
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Singleton

@FlowPreview
@ExperimentalCoroutinesApi
@Singleton
@Component(modules = [NetworkModule::class, PersistentModule::class, ProviderModule::class])
interface AppComponent {
    fun inject(definitionViewModel: DefinitionViewModel)
    fun inject(savedDictionaryViewModel: SavedDictionaryViewModel)

    @FlowPreview
    @Component.Builder
    interface Builder {
        fun networkModule(networkModule: NetworkModule): Builder
        fun persistentModule(persistentModule: PersistentModule): Builder
        fun providerModule(providerModule: ProviderModule): Builder
        fun build(): AppComponent
    }
}