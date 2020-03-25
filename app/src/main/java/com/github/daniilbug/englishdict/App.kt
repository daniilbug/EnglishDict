package com.github.daniilbug.englishdict

import android.app.Application
import android.content.Context
import com.github.daniilbug.englishdict.di.DaggerAppComponent
import com.github.daniilbug.englishdict.di.modules.NetworkModule
import com.github.daniilbug.englishdict.di.modules.PersistentModule
import com.github.daniilbug.englishdict.di.modules.ProviderModule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
class App: Application() {

    companion object {
        fun component(appContext: Context?) = (appContext as App).appComponent
    }

    //TODO()
    private val appComponent by lazy { DaggerAppComponent.builder()
        .networkModule(NetworkModule())
        .persistentModule(PersistentModule(this))
        .providerModule(ProviderModule(resources))
        .build()
    }
}