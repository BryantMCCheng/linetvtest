package com.linetv.linetvtest.DB;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

public class MyContentProvider extends ContentProvider {
    public static final String TAG = MyContentProvider.class.getCanonicalName();
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private static final int DRAMA_LIST = 1;
    private static final int DRAMA_ID = 2;

    static {
        sUriMatcher.addURI(ContentDataList.AUTHORITY, ContentDataList.Table_Names.DRAMADATA, DRAMA_LIST);
        sUriMatcher.addURI(ContentDataList.AUTHORITY, ContentDataList.Table_Names.DRAMADATA + "/#", DRAMA_ID);
    }

    private MySQLiteOpenHelper mSQLiteOpenHelper;

    @Override
    public boolean onCreate() {
        mSQLiteOpenHelper = MySQLiteOpenHelper.getInstance(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        // TODO: Implement this to handle query requests from clients.
        Log.i(TAG, "Provider Query Data");
        int uriType = sUriMatcher.match(uri);
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        switch (uriType) {
            case DRAMA_LIST:
                queryBuilder.setTables(ContentDataList.Table_Names.DRAMADATA);
                break;
            case DRAMA_ID:
                queryBuilder.setTables(ContentDataList.Table_Names.DRAMADATA);
                queryBuilder.appendWhere(ContentDataList.DramaData.ID + "=" + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI");
        }

        Cursor cursor = queryBuilder.query(mSQLiteOpenHelper.getReadableDatabase(),
                projection, selection, selectionArgs, null, null,
                sortOrder);

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Log.i(TAG, "Provider Insert Data");
        int uriType = sUriMatcher.match(uri);
        SQLiteDatabase sqlDB = mSQLiteOpenHelper.getWritableDatabase();//dangerous to close it if used in different thread
        long id;
        if (uriType == DRAMA_LIST) {
            id = sqlDB.insert(ContentDataList.Table_Names.DRAMADATA, null, values);
        } else {
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);

        return Uri.parse(uri + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.i(TAG, "Provider Delete Data");
        int uriType = sUriMatcher.match(uri);
        SQLiteDatabase sqlDB = mSQLiteOpenHelper.getWritableDatabase();//dangerous to close it if used in different thread
        int rows;
        String id = uri.getLastPathSegment();
        switch (uriType) {
            case DRAMA_LIST:
                rows = sqlDB.delete(ContentDataList.Table_Names.DRAMADATA, selection, selectionArgs);
                break;
            case DRAMA_ID:
                if (TextUtils.isEmpty(selection)) {
                    rows = sqlDB.delete(ContentDataList.Table_Names.DRAMADATA, ContentDataList.DramaData.ID + "=" + id, null);
                } else {
                    rows = sqlDB.delete(ContentDataList.Table_Names.DRAMADATA, ContentDataList.DramaData.ID + "=" + id + " and " + selection, selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        if (rows > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rows;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        Log.i(TAG, "Provider update Data");
        Log.i(TAG, "Provider update Data uri is " + uri);
        Log.i(TAG, "Provider update Data values is " + values);
        Log.i(TAG, "Provider update Data selection is " + selection);
        Log.i(TAG, "Provider update Data selectionArgs is " + selectionArgs);
        int uriType = sUriMatcher.match(uri);
        SQLiteDatabase sqlDB = mSQLiteOpenHelper.getWritableDatabase();//dangerous to close it if used in different thread
        int rows;
        String id = uri.getLastPathSegment();
        switch (uriType) {
            case DRAMA_LIST:
                rows = sqlDB.update(ContentDataList.Table_Names.DRAMADATA,
                        values,
                        selection,
                        selectionArgs);
                break;
            case DRAMA_ID:
                if (TextUtils.isEmpty(selection)) {
                    rows = sqlDB.update(ContentDataList.Table_Names.DRAMADATA,
                            values,
                            ContentDataList.DramaData.ID + "=" + id,
                            null);
                } else {
                    rows = sqlDB.update(ContentDataList.Table_Names.DRAMADATA,
                            values,
                            ContentDataList.DramaData.ID + "=" + id
                                    + " and " + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        if (rows > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rows;
    }
}
