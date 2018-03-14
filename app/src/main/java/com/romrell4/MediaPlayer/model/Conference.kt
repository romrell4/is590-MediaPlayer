package com.romrell4.MediaPlayer.model

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import com.romrell4.MediaPlayer.provider.ConferenceContract
import com.romrell4.MediaPlayer.support.queryV2

/**
 * Created by romrell4 on 2/28/18
 */
data class Conference(var id: Int, var month: String, var year: String, var sessions: ArrayList<Session>): Parcelable {
    constructor(parcel: Parcel): this(
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.createTypedArrayList(Session)) {
    }

    companion object CREATOR: Parcelable.Creator<Conference> {
        override fun createFromParcel(parcel: Parcel): Conference {
            return Conference(parcel)
        }

        override fun newArray(size: Int): Array<Conference?> {
            return arrayOfNulls(size)
        }

        fun readConferences(context: Context): List<Conference> {
            val cursor = context.contentResolver.queryV2(ConferenceContract.CONFERENCE_URI)

            val conferences = mutableListOf<Conference>()
            if (cursor.moveToFirst()) {

                val idIndex = cursor.getColumnIndex(ConferenceContract.FIELD_ID)
                val monthIndex = cursor.getColumnIndex(ConferenceContract.FIELD_MONTH)
                val yearIndex = cursor.getColumnIndex(ConferenceContract.FIELD_YEAR)

                while (!cursor.isAfterLast) {
                    val conferenceId = cursor.getInt(idIndex)
                    conferences.add(Conference(
                            conferenceId,
                            cursor.getString(monthIndex),
                            cursor.getString(yearIndex),
                            Session.readSessionsForConference(context, conferenceId)
                    ))
                    cursor.moveToNext()
                }
            }

            cursor.close()
            return conferences
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(month)
        parcel.writeString(year)
        parcel.writeTypedList(sessions)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun toString(): String {
        return "${month} ${year}\n${sessions.joinToString("\n") { "\t$it" }}"
    }
}