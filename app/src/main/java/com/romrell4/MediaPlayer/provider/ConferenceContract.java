package com.romrell4.MediaPlayer.provider;

import android.net.Uri;

/**
 * Contract for the general conference talk provider.  There are three
 * tables: conference, session, and talk.  They are joined by foreign
 * keys: talk points to session via SessionID, and session points to
 * conference via ConferenceID.  The SQL to do the join would be
 * something like this:
 *
 *     SELECT * FROM talk t JOIN session s JOIN conference c
 *     WHERE t.SessionID=s.ID AND s.ConferenceID=c.ID;
 *
 * The fields for each table are listed below.
 */
public class ConferenceContract {
    public static final String AUTHORITY = "com.romrell4.MediaPlayer.provider";

    public static final String TOPIC_CONFERENCE = "conference";
    public static final String FIELD_ID = "ID";
    public static final String FIELD_YEAR = "Year";
    public static final String FIELD_MONTH = "Month";

    public static final String TOPIC_SESSION = "session";
    // Also has an ID field
    public static final String FIELD_TITLE = "Title";
    public static final String FIELD_CONFERENCE_ID = "ConferenceID";

    public static final String TOPIC_TALK = "talk";
    // Also has an ID field
    public static final String FIELD_SESSION_ID = "SessionID";
    // Also has a Title field
    public static final String FIELD_SPEAKER = "Speaker";
    public static final String FIELD_DATE = "Date";
    public static final String FIELD_AUDIO_URL = "AudioUrl";
    public static final String FIELD_VIDEO_URL = "VideoUrl";

    public static final Uri CONFERENCE_URI = Uri.withAppendedPath(Uri.parse("content://" + AUTHORITY), TOPIC_CONFERENCE);
    public static final Uri SESSION_URI = Uri.withAppendedPath(Uri.parse("content://" + AUTHORITY), TOPIC_SESSION);
    public static final Uri TALK_URI = Uri.withAppendedPath(Uri.parse("content://" + AUTHORITY), TOPIC_TALK);
}
