package com.linetv.linetvtest.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "linetvtest.db";
    private static MySQLiteOpenHelper sSingleton;
    private static final int versionNum = 20190424;

    public static synchronized MySQLiteOpenHelper getInstance(Context context) {
        if (sSingleton == null) {
            sSingleton = new MySQLiteOpenHelper(context.getApplicationContext());
        }
        return sSingleton;
    }

    private MySQLiteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, versionNum);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String drama = "CREATE TABLE IF NOT EXISTS " + ContentDataList.Table_Names.DRAMADATA
                + " ( "
                + ContentDataList.DramaData.ID + " INTEGER  PRIMARY KEY AUTOINCREMENT, "
                + ContentDataList.DramaData.DRAMA_ID + " INTEGER, "
                + ContentDataList.DramaData.NAME + " TEXT, "
                + ContentDataList.DramaData.TOTAL_VIEWS + " INTEGER, "
                + ContentDataList.DramaData.CREATED_AT + " TEXT, "
                + ContentDataList.DramaData.THUMB + " TEXT, "
                + ContentDataList.DramaData.RATING + " FLOAT "
                + " ) ";

        db.execSQL(drama);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("test tag", " onUpgrade");
        if (newVersion > oldVersion) {
            db.beginTransaction();//建立交易
            boolean success = false;//判斷參數
            if (success) {
                db.setTransactionSuccessful();//正確交易才成功
            }
            db.endTransaction();
        } else {
            onCreate(db);
        }
    }
}
