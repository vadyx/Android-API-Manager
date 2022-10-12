package hr.algebra.android_api_manager.api

import hr.algebra.android_api_manager.BuildConfig
import retrofit2.Call
import retrofit2.http.GET

const val API_URL = "https://youtube.googleapis.com/youtube/v3/"
const val key = BuildConfig.YOUTUBE_API_KEY

interface APIManagerApi {
    @GET("videos?part=snippet%2CcontentDetails&id=F9UC9DY-vIU&id=3qBXWUpoPHo&id=OXGznpKZ_sA&key=${key}")
    fun fetchItems() : Call<APIManagerItem>
}