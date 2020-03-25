package com.github.daniilbug.englishdict.model.network

import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface DictionaryApi {

    @GET("learners/json/{word}")
    fun getDefinitionAsync(
        @Path("word") word: String,
        @Query("key") key: String = DICTIONARY_API_KEY
    ): Deferred<List<APIDictionaryDefinition>>

    @GET
    fun getImagesByWordAsync(
        @Url url: String = "https://pixabay.com/api",
        @Query("q") word: String,
        @Query("per_page") perPage: Int = 10,
        @Query("page") page: Int = 1,
        @Query("key") key: String = IMAGES_API_KEY,
        @Query("image_type") imageType: String = "illustration"
    ): Deferred<ApiPixabayAnswer>
}