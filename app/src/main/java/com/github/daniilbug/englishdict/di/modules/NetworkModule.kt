package com.github.daniilbug.englishdict.di.modules

import com.github.daniilbug.englishdict.model.DictionaryRepository
import com.github.daniilbug.englishdict.model.network.DictionaryApi
import com.github.daniilbug.englishdict.model.network.DictionaryApiRepository
import com.github.daniilbug.englishdict.model.network.PixabyImageApi
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

    private val moshi: Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private fun dictionaryRetrofit(moshi: Moshi): Retrofit {
        return Retrofit.Builder()
            .addCallAdapterFactory(CoroutineCallAdapterFactory.invoke())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl("https://www.dictionaryapi.com/api/v3/references/")
            .build()
    }

    private fun imageRetrofit(moshi: Moshi): Retrofit {
        return Retrofit.Builder()
            .addCallAdapterFactory(CoroutineCallAdapterFactory.invoke())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl("https://pixabay.com/")
            .build()
    }

    @Provides
    fun dictionaryApi(): DictionaryApi {
        return dictionaryRetrofit(moshi).create(DictionaryApi::class.java)
    }

    @Provides
    fun imageApi(): PixabyImageApi {
        return imageRetrofit(moshi).create(PixabyImageApi::class.java)
    }

    @ExperimentalCoroutinesApi
    @Provides
    fun dictionaryRepository(dictionaryApi: DictionaryApi, imageApi: PixabyImageApi): DictionaryRepository {
        return DictionaryApiRepository(dictionaryApi, imageApi)
    }
}