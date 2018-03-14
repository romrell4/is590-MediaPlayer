package com.romrell4.MediaPlayer.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.romrell4.MediaPlayer.R;

/**
 * Content provider for the general conference database.
 *
 * Created by Liddle on 2/23/18.
 */

public class ConferenceProvider extends ContentProvider {
    //
    // Internal codes we use to identify a URI we've matched.
    //
    private static final int URI_TYPE_CONFERENCE_DIR = 1;
    private static final int URI_TYPE_CONFERENCE_ITEM = 2;
    private static final int URI_TYPE_SESSION_DIR = 3;
    private static final int URI_TYPE_SESSION_ITEM = 4;
    private static final int URI_TYPE_TALK_DIR = 5;
    private static final int URI_TYPE_TALK_ITEM = 6;

    //
    // Android primary MIME types for ContentProviders.
    //
    private static final String ANDROID_MIME_TYPE_BASE_SET = "vnd.android.cursor.dir/vnd.";
    private static final String ANDROID_MIME_TYPE_BASE_ITEM = "vnd.android.cursor.item/vnd.";

    //
    // UriMatcher for handling incoming URI requests on this content provider.
    //
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(ConferenceContract.AUTHORITY, ConferenceContract.TOPIC_CONFERENCE, URI_TYPE_CONFERENCE_DIR);
        sUriMatcher.addURI(ConferenceContract.AUTHORITY, ConferenceContract.TOPIC_CONFERENCE + "/#", URI_TYPE_CONFERENCE_ITEM);
        sUriMatcher.addURI(ConferenceContract.AUTHORITY, ConferenceContract.TOPIC_SESSION, URI_TYPE_SESSION_DIR);
        sUriMatcher.addURI(ConferenceContract.AUTHORITY, ConferenceContract.TOPIC_SESSION + "/#", URI_TYPE_SESSION_ITEM);
        sUriMatcher.addURI(ConferenceContract.AUTHORITY, ConferenceContract.TOPIC_TALK, URI_TYPE_TALK_DIR);
        sUriMatcher.addURI(ConferenceContract.AUTHORITY, ConferenceContract.TOPIC_TALK + "/#", URI_TYPE_TALK_ITEM);
    }

    //
    // Instance variables
    //
    private DatabaseHelper mDatabaseHelper;

    @Override
    public boolean onCreate() {
        mDatabaseHelper = new DatabaseHelper(getContext());

        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        int uriType = sUriMatcher.match(uri);

        switch (uriType) {
            case URI_TYPE_CONFERENCE_DIR:
                queryBuilder.setTables(ConferenceContract.TOPIC_CONFERENCE);
                break;
            case URI_TYPE_CONFERENCE_ITEM:
                queryBuilder.setTables(ConferenceContract.TOPIC_CONFERENCE);
                queryBuilder.appendWhere(ConferenceContract.FIELD_ID + "="
                        + uri.getLastPathSegment());
                break;
            case URI_TYPE_SESSION_DIR:
                queryBuilder.setTables(ConferenceContract.TOPIC_SESSION);
                break;
            case URI_TYPE_SESSION_ITEM:
                queryBuilder.setTables(ConferenceContract.TOPIC_SESSION);
                queryBuilder.appendWhere(ConferenceContract.FIELD_ID + "="
                        + uri.getLastPathSegment());
                break;
            case URI_TYPE_TALK_DIR:
                queryBuilder.setTables(ConferenceContract.TOPIC_TALK);
                break;
            case URI_TYPE_TALK_ITEM:
                queryBuilder.setTables(ConferenceContract.TOPIC_TALK);
                queryBuilder.appendWhere(ConferenceContract.FIELD_ID + "="
                        + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI");
        }

        Cursor cursor = queryBuilder.query(mDatabaseHelper.getReadableDatabase(),
                projection, selection, selectionArgs, null, null, sortOrder);
        Context context = getContext();

        if (context == null) {
            throw new RuntimeException("Unable to access context");
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        //
        // Return the MIME type for each supported query type.
        // If the URI doesn't match, it's not a type we support.
        //
        switch (sUriMatcher.match(uri)) {
            case URI_TYPE_CONFERENCE_DIR:
                return ANDROID_MIME_TYPE_BASE_SET + ConferenceContract.AUTHORITY + "." + ConferenceContract.TOPIC_CONFERENCE;
            case URI_TYPE_CONFERENCE_ITEM:
                return ANDROID_MIME_TYPE_BASE_ITEM + ConferenceContract.AUTHORITY + "." + ConferenceContract.TOPIC_CONFERENCE;
            case URI_TYPE_SESSION_DIR:
                return ANDROID_MIME_TYPE_BASE_SET + ConferenceContract.AUTHORITY + "." + ConferenceContract.TOPIC_SESSION;
            case URI_TYPE_SESSION_ITEM:
                return ANDROID_MIME_TYPE_BASE_ITEM + ConferenceContract.AUTHORITY + "." + ConferenceContract.TOPIC_SESSION;
            case URI_TYPE_TALK_DIR:
                return ANDROID_MIME_TYPE_BASE_SET + ConferenceContract.AUTHORITY + "." + ConferenceContract.TOPIC_TALK;
            case URI_TYPE_TALK_ITEM:
                return ANDROID_MIME_TYPE_BASE_ITEM + ConferenceContract.AUTHORITY + "." + ConferenceContract.TOPIC_TALK;
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        // For now, this provider is read-only
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        // For now, this provider is read-only
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        // For now, this provider is read-only
        return 0;
    }

    /**
     * This helper creates and maintains the underlying database
     */
    protected static final class DatabaseHelper extends SQLiteOpenHelper {
        private static final String TAG = "DatabaseHelper";
        private static final String DATABASE_NAME = "conference.sqlite";
        private static final int DATABASE_VERSION = 1;

        private Context mContext;

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            mContext = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            StringBuilder sqlBuilder = new StringBuilder();

            try {
                InputStream inputStream = mContext.getResources().openRawResource(R.raw.conference);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;

                // Read the whole SQL script file all at once.
                while ((line = bufferedReader.readLine()) != null) {
                    sqlBuilder.append(line);
                    sqlBuilder.append('\n');
                }

                bufferedReader.close();

                for (String statement : sqlBuilder.toString().split(";")) {
                    if (statement.trim().length() > 0) {
                        // Execute the SQL statements one at a time.
                        db.execSQL(statement + ";");
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "Error creating database: " + e);
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // There's nothing to do yet since we only have one database schema version
        }
    }
}
