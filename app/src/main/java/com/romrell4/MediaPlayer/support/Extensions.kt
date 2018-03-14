package com.romrell4.MediaPlayer.support

import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by romrell4 on 3/3/18
 */
fun ContentResolver.queryV2(uri: Uri, projection: Array<out String>? = null, selection: String? = null, selectionArgs: Array<out String>? = null, sortOrder: String? = null): Cursor {
    return this.query(uri, projection, selection, selectionArgs, sortOrder)
}

fun LayoutInflater.inflateV2(resource: Int, parent: ViewGroup?, attachToParent: Boolean = false): View {
    return this.inflate(resource, parent, attachToParent)
}