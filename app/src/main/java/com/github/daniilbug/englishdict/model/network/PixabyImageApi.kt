package com.github.daniilbug.englishdict.model.network

import retrofit2.http.GET
import retrofit2.http.Query

interface PixabyImageApi {

    @GET("api")
    suspend fun getImagesByWord(
        @Query("q") word: String,
        @Query("per_page") perPage: Int = 10,
        @Query("page") page: Int = 1,
        @Query("key") key: String = IMAGES_API_KEY,
        @Query("image_type") imageType: String = "illustration"
    ): ApiPixabayAnswer
}