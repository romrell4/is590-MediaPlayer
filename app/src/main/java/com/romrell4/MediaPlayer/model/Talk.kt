package com.romrell4.MediaPlayer.model

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import com.romrell4.MediaPlayer.provider.ConferenceContract
import com.romrell4.MediaPlayer.support.queryV2

/**
 * Created by romrell4 on 3/4/18
 */
data class Talk(var id: Int, var title: String, var speaker: String, var date: String, var audioUrl: String, var videoUrl: String): Parcelable {
    private constructor(parcel: Parcel): this(
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()) {
    }

    companion object CREATOR: Parcelable.Creator<Talk> {
        override fun createFromParcel(parcel: Parcel): Talk {
            return Talk(parcel)
        }

        override fun newArray(size: Int): Array<Talk?> {
            return arrayOfNulls(size)
        }

        fun readTalksForSession(context: Context, sessionId: Int): ArrayList<Talk> {
            val cursor = context.contentResolver.queryV2(ConferenceContract.TALK_URI,
                    selection = ConferenceContract.FIELD_SESSION_ID + " = ?",
                    selectionArgs = arrayOf(sessionId.toString()))

            val talks = arrayListOf<Talk>()
            if (cursor.moveToFirst()) {
                val idIndex = cursor.getColumnIndex(ConferenceContract.FIELD_ID)
                val titleIndex = cursor.getColumnIndex(ConferenceContract.FIELD_TITLE)
                val speakerIndex = cursor.getColumnIndex(ConferenceContract.FIELD_SPEAKER)
                val dateIndex = cursor.getColumnIndex(ConferenceContract.FIELD_DATE)
                val audioUrlIndex = cursor.getColumnIndex(ConferenceContract.FIELD_AUDIO_URL)
                val videoUrlIndex = cursor.getColumnIndex(ConferenceContract.FIELD_VIDEO_URL)

                while (!cursor.isAfterLast) {
                    talks.add(Talk(
                            cursor.getInt(idIndex),
                            cursor.getString(titleIndex),
                            cursor.getString(speakerIndex),
                            cursor.getString(dateIndex),
                            cursor.getString(audioUrlIndex),
                            cursor.getString(videoUrlIndex)
                    ))

                    cursor.moveToNext()
                }
            }

            cursor.close()
            return talks
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(title)
        parcel.writeString(speaker)
        parcel.writeString(date)
        parcel.writeString(audioUrl)
        parcel.writeString(videoUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun toString(): String {
        return "\t\t${title}"
    }
}