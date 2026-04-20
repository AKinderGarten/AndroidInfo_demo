package com.example.personinfodemo

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.example.personinfodemo.db.PersonDao

class MyContentProvider : ContentProvider() {
    private lateinit var dao: PersonDao
    private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH)

    companion object {
        const val AUTHORITY = "com.example.personinfodemo.provider"
    }

    init {
        uriMatcher.addURI(AUTHORITY, "info", 1)
    }

    override fun onCreate(): Boolean {
        dao = PersonDao(context!!)
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor? {
        if (uriMatcher.match(uri) != 1) return null
        return dao.getAllPersonsCursor()
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        if (values == null || uriMatcher.match(uri) != 1) return null

        val id = values.getAsInteger("id")
        val name = values.getAsString("name")
        val age = values.getAsInteger("age")
        val height = values.getAsFloat("height")

        dao.insertPersonById(id, name, age, height)
        return uri
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        if (values == null || uriMatcher.match(uri) != 1 || selectionArgs.isNullOrEmpty()) return 0

        val id = selectionArgs[0].toIntOrNull() ?: return 0
        val name = values.getAsString("name")
        val age = values.getAsInteger("age")
        val height = values.getAsFloat("height")

        return if (dao.updatePersonById(id, name, age, height)) 1 else 0
    }

    override fun delete(
        uri: Uri,
        selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        if (uriMatcher.match(uri) != 1 || selectionArgs.isNullOrEmpty()) return 0

        val id = selectionArgs[0].toIntOrNull() ?: return 0
        return if (dao.deleteById(id)) 1 else 0
    }

    override fun getType(uri: Uri): String? {
        return null
    }
}