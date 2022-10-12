package hr.algebra.android_api_manager.factory

import java.net.HttpURLConnection
import java.net.URL

private const val TIMEOUT = 10000
private const val METHOD = "GET"
private const val USER_AGENT = "User-Agent"
private const val MOZILLA = "Mozilla/5.0"

fun createGetHttpUrlConnection(path: String): HttpURLConnection {
    val url = URL(path)
    return (url.openConnection() as HttpURLConnection).apply {
        connectTimeout = TIMEOUT
        readTimeout = TIMEOUT
        requestMethod = METHOD
        addRequestProperty(USER_AGENT, MOZILLA)
    }
}