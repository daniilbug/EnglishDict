package com.github.daniilbug.englishdict.di.modules

import com.github.daniilbug.englishdict.model.DictionaryRepository
import com.github.daniilbug.englishdict.model.network.DictionaryApi
import com.github.daniilbug.englishdict.model.network.DictionaryApiRepository
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.ExperimentalCoroutinesApi
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
class NetworkModule {

    @Provides
    fun moshi(): Moshi =
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

    @Provides
    fun retrofit(moshi: Moshi): Retrofit {
        return Retrofit.Builder()
            .addCallAdapterFactory(CoroutineCallAdapterFactory.invoke())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl("https://www.dictionaryapi.com/api/v3/references/")
            .build()
    }

    @Provides
    fun dictionaryApi(retrofit: Retrofit): DictionaryApi {
        return retrofit.create(DictionaryApi::class.java)
    }

    @ExperimentalCoroutinesApi
    @Provides
    fun dictionaryRepository(api: DictionaryApi): DictionaryRepository {
        return DictionaryApiRepository(api)
    }
}