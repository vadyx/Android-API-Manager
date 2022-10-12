package hr.algebra.android_api_manager.dao

import android.content.Context

fun getAPIManagerRepository(context: Context?) = APIManagerSqlHelper(context)