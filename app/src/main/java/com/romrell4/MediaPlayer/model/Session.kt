package com.romrell4.MediaPlayer.model

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import com.romrell4.MediaPlayer.provider.ConferenceContract
import com.romrell4.MediaPlayer.support.queryV2

/**
 * Created by romrell4 on 3/4/18
 */
data class Session(var id: Int, var title: String, var talks: ArrayList<Talk>): Parcelable {
    constructor(parcel: Parcel): this(
            parcel.readInt(),
            parcel.readString(),
            parcel.createTypedArrayList(Talk)) {
    }

    companion object CREATOR: Parcelable.Creator<Session> {
        override fun createFromParcel(parcel: Parcel): Session {
            return Session(parcel)
        }

        override fun newArray(size: Int): Array<Session?> {
            return arrayOfNulls(size)
        }

        fun readSessionsForConference(context: Context, conferenceId: Int): ArrayList<Session> {
            val cursor = context.contentResolver.queryV2(ConferenceContract.SESSION_URI, selection = ConferenceContract.FIELD_CONFERENCE_ID + " = ?", selectionArgs = arrayOf(conferenceId.toString()))

            val sessions = arrayListOf<Session>()
            if (cursor.moveToFirst()) {
                val idIndex = cursor.getColumnIndex(ConferenceContract.FIELD_ID)
                val titleIndex = cursor.getColumnIndex(ConferenceContract.FIELD_TITLE)

                while (!cursor.isAfterLast) {
                    val sessionId = cursor.getInt(idIndex)
                    sessions.add(Session(
                            sessionId,
                            cursor.getString(titleIndex),
                            Talk.readTalksForSession(context, sessionId)
                    ))

                    cursor.moveToNext()
                }
            }

            cursor.close()
            return sessions
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(title)
        parcel.writeTypedList(talks)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun toString(): String {
        return "\t${title}\n${talks.joinToString("\n") { "\t\t$it" }}"
    }
}