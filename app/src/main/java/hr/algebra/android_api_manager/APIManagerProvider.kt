package hr.algebra.android_api_manager

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import hr.algebra.android_api_manager.dao.APIManagerRepository
import hr.algebra.android_api_manager.dao.getAPIManagerRepository
import hr.algebra.android_api_manager.model.Item
import java.lang.IllegalArgumentException

private const val AUTHORITY = "hr.algebra.android_api_manager.api.provider"
private const val PATH = "items"

val APIMANAGER_PROVIDER_URI = Uri.parse("content://$AUTHORITY/$PATH")

private const val ITEMS = 10
private const val ITEM_ID = 20

private val URI_MATCHER = with(UriMatcher(UriMatcher.NO_MATCH)) {
    addURI(AUTHORITY, PATH, ITEMS)
    addURI(AUTHORITY, "$PATH/#", ITEM_ID)
    this
}

class APIManagerProvider : ContentProvider() {

    private lateinit var apiManagerRepository: APIManagerRepository

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        when (URI_MATCHER.match(uri)) {
            ITEMS -> return apiManagerRepository.delete(selection, selectionArgs)
            ITEM_ID -> {
                uri.lastPathSegment?.let {
                    return apiManagerRepository.delete("${Item::_id.name}=?", arrayOf(it))
                }
            }
        }
        throw IllegalArgumentException("Wrong uri")
    }

    override fun getType(uri: Uri): String? {
        TODO(
            "Implement this to handle requests for the MIME type of the data" +
                    "at the given URI"
        )
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val id = apiManagerRepository.insert(values)
        return ContentUris.withAppendedId(APIMANAGER_PROVIDER_URI, id)
    }

    override fun onCreate(): Boolean {
        apiManagerRepository = getAPIManagerRepository(context)
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? = apiManagerRepository.query(projection, selection, selectionArgs, sortOrder)

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        when (URI_MATCHER.match(uri)) {
            ITEMS -> return apiManagerRepository.update(values, selection, selectionArgs)
            ITEM_ID -> {
                uri.lastPathSegment?.let {
                    return apiManagerRepository.update(values, "${Item::_id.name}=?", arrayOf(it))
                }
            }
        }
        throw IllegalArgumentException("Wrong uri")
    }
}