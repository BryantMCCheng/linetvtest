package com.linetv.linetvtest;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.google.gson.Gson;
import com.linetv.linetvtest.Model.DramaModel;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Utils {

    public static final String TAG = Utils.class.getCanonicalName();

    public static List<DramaModel.DataBean> getData() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(Constant.DRAMAS_URL)
                .get()
                .build();
        List<DramaModel.DataBean> list = null;
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                Gson gson = new Gson();
                DramaModel dramaModel = gson.fromJson(response.body().string(), DramaModel.class);
                if (dramaModel != null) {
                    list = dramaModel.getData();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static boolean isConnected(Context context) {
        if (context != null) {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnected();
        }
        return false;
    }

    public static DramaModel.DataBean findDataById(String drama_id, List<DramaModel.DataBean> dramaData) {
        Log.d(TAG, "findDataById, drama_id = " + drama_id + ", dramaData size = " + dramaData.size());
        for (DramaModel.DataBean data : dramaData) {
            if (String.valueOf(data.getDrama_id()).equalsIgnoreCase(drama_id)) {
                return data;
            }
        }
        return null;
    }
}
