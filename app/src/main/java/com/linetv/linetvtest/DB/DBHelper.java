package com.linetv.linetvtest.DB;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.linetv.linetvtest.Model.DramaModel;

import java.util.ArrayList;
import java.util.List;

public class DBHelper {
    public static final String TAG = DBHelper.class.getCanonicalName();

    public static void updateDramaDataList(Context context, List<DramaModel.DataBean> list) {
        if (context == null || list == null) return;
        clearDramaDataList(context);
        ContentResolver contentResolver = context.getContentResolver();
        if (list.size() == 0) return;
        Log.d(TAG, "updateDramaDataList, list size = " + list.size());
        for (int i = 0; i < list.size(); i++) {
            ContentValues currentValue = new ContentValues();
            currentValue.put(ContentDataList.DramaData.DRAMA_ID, list.get(i).getDrama_id());
            currentValue.put(ContentDataList.DramaData.NAME, list.get(i).getName());
            currentValue.put(ContentDataList.DramaData.TOTAL_VIEWS, list.get(i).getTotal_views());
            currentValue.put(ContentDataList.DramaData.CREATED_AT, list.get(i).getCreated_at());
            currentValue.put(ContentDataList.DramaData.THUMB, list.get(i).getThumb());
            currentValue.put(ContentDataList.DramaData.RATING, list.get(i).getRating());
            contentResolver.insert(ContentDataList.DramaData.CONTACT_URI, currentValue);
        }
    }

    public static void clearDramaDataList(Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        contentResolver.delete(ContentDataList.DramaData.CONTACT_URI, null, null);
        Log.d(TAG, "clearDramaDataList");
    }

    public static ArrayList<DramaModel.DataBean> getDramaDataList(Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(
                ContentDataList.DramaData.CONTACT_URI,
                null,
                null,
                null,
                null
        );
        if (cursor != null && cursor.getCount() > 0) {
            Log.d(TAG, "get drama list from db, cursor size = " + cursor.getCount());
            ArrayList<DramaModel.DataBean> dataBeansList = new ArrayList<>();
            while (cursor.moveToNext()) {
                DramaModel.DataBean dataBean = new DramaModel.DataBean();
                dataBean.setDrama_id(cursor.getInt(cursor.getColumnIndex(ContentDataList.DramaData.DRAMA_ID)));
                dataBean.setName(cursor.getString(cursor.getColumnIndex(ContentDataList.DramaData.NAME)));
                dataBean.setTotal_views(cursor.getInt(cursor.getColumnIndex(ContentDataList.DramaData.TOTAL_VIEWS)));
                dataBean.setCreated_at(cursor.getString(cursor.getColumnIndex(ContentDataList.DramaData.CREATED_AT)));
                dataBean.setThumb(cursor.getString(cursor.getColumnIndex(ContentDataList.DramaData.THUMB)));
                dataBean.setRating(cursor.getDouble(cursor.getColumnIndex(ContentDataList.DramaData.RATING)));
                dataBeansList.add(dataBean);
            }
            if (!cursor.isClosed()) {
                cursor.close();
            }
            return dataBeansList;
        }
        return null;
    }

    public static DramaModel.DataBean getDramaDataById(Context context, String dramaId) {
        ContentResolver contentResolver = context.getContentResolver();
        String selection = ContentDataList.DramaData.DRAMA_ID + " = ?";
        String[] selectionArg = new String[]{dramaId};
        Cursor cursor = contentResolver.query(
                ContentDataList.DramaData.CONTACT_URI,
                null,
                selection,
                selectionArg,
                null
        );
        if (cursor != null && cursor.getCount() > 0) {
            Log.d(TAG, "get drama id = " + dramaId + " data from db, cursor size = " + cursor.getCount());
            DramaModel.DataBean dataBean = new DramaModel.DataBean();
            while (cursor.moveToNext()) {
                dataBean.setDrama_id(cursor.getInt(cursor.getColumnIndex(ContentDataList.DramaData.DRAMA_ID)));
                dataBean.setName(cursor.getString(cursor.getColumnIndex(ContentDataList.DramaData.NAME)));
                dataBean.setTotal_views(cursor.getInt(cursor.getColumnIndex(ContentDataList.DramaData.TOTAL_VIEWS)));
                dataBean.setCreated_at(cursor.getString(cursor.getColumnIndex(ContentDataList.DramaData.CREATED_AT)));
                dataBean.setThumb(cursor.getString(cursor.getColumnIndex(ContentDataList.DramaData.THUMB)));
                dataBean.setRating(cursor.getDouble(cursor.getColumnIndex(ContentDataList.DramaData.RATING)));
            }
            if (!cursor.isClosed()) {
                cursor.close();
            }
            return dataBean;
        }
        return null;
    }
}
