package hr.algebra.android_api_manager.api

import android.content.ContentValues
import android.content.Context
import android.util.Log
import hr.algebra.android_api_manager.APIMANAGER_PROVIDER_URI
import hr.algebra.android_api_manager.APIManagerReceiver
import hr.algebra.android_api_manager.DATA_IMPORTED
import hr.algebra.android_api_manager.framework.sendBroadcast
import hr.algebra.android_api_manager.framework.setBooleanPreference
import hr.algebra.android_api_manager.handler.downloadImageAndStore
import hr.algebra.android_api_manager.model.Item
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class APIManagerFetcher(private val context: Context) {

    private var apiManagerApi: APIManagerApi

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        apiManagerApi = retrofit.create(APIManagerApi::class.java)
    }

    fun fetchItems() {
        val request = apiManagerApi.fetchItems()

        request.enqueue(object : Callback<APIManagerItem> {
            override fun onResponse(
                call: Call<APIManagerItem>,
                response: Response<APIManagerItem>
            ) {
                response.body()?.let {
                    populateItems(it)
                }
            }

            override fun onFailure(call: Call<APIManagerItem>, t: Throwable) {
                Log.d(javaClass.name, t.message, t)
            }

        })
    }

    private fun populateItems(apiManagerItems: APIManagerItem) {

        GlobalScope.launch {
            apiManagerItems.items.forEach {
                val picturePath = downloadImageAndStore(
                    context,
                    it.snippet.thumbnails.maxres.url,
                    it.snippet.title.replace(" ", "_")
                )

                val values = ContentValues().apply {
                    put(Item::title.name, it.snippet.title)
                    put(Item::description.name, it.snippet.description)
                    put(Item::picturePath.name, picturePath ?: "")
                    put(Item::videoPath.name, it.id)
                    put(Item::publishedDate.name, it.snippet.publishedAt)
                    put(Item::favourite.name, false)
                }
                context.contentResolver.insert(APIMANAGER_PROVIDER_URI, values)
            }
            context.setBooleanPreference(DATA_IMPORTED, true)
            context.sendBroadcast<APIManagerReceiver>()
        }
    }
}