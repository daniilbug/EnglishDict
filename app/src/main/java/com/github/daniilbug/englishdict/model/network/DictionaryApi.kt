package com.github.daniilbug.englishdict.model.network

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DictionaryApi {

    @GET("learners/json/{word}")
    suspend fun getDefinition(
        @Path("word") word: String,
        @Query("key") key: String = DICTIONARY_API_KEY
    ): List<APIDictionaryDefinition>
}