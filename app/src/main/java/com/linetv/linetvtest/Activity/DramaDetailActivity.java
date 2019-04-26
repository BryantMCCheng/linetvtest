package com.linetv.linetvtest.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.linetv.linetvtest.Constant;
import com.linetv.linetvtest.DB.DBHelper;
import com.linetv.linetvtest.Model.DramaModel;
import com.linetv.linetvtest.LoadDataTask;
import com.linetv.linetvtest.R;
import com.linetv.linetvtest.Utils;

import java.util.List;

public class DramaDetailActivity extends LoadingActivity implements LoadDataTask.LoadDataTaskCallback {

    public static final String TAG = DramaDetailActivity.class.getCanonicalName();
    private LoadDataTask loadDataTask;
    private ImageView iv_thumb;
    private TextView tv_name, tv_rating, tv_created_at, tv_total_views;
    private String drama_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drama_detail);
        iv_thumb = findViewById(R.id.iv_thumb);
        tv_name = findViewById(R.id.tv_name);
        tv_rating = findViewById(R.id.tv_rating);
        tv_created_at = findViewById(R.id.tv_created_at);
        tv_total_views = findViewById(R.id.tv_total_views);
        Intent intent = getIntent();
        if (intent != null) {
            Uri data = intent.getData();
            if (data != null) {
                Log.d(TAG, "data = " + data.toString());
                List<String> params = data.getPathSegments();
                if (params.size() == 2) {
                    drama_id = params.get(1);
                    Log.d(TAG, "id = " + drama_id);
                    if (intent.getIntExtra("type", Constant.FROM_URL) == Constant.FROM_LIST) {
                        loadFromDB();
                    } else {
                        if (Utils.isConnected(this.getApplicationContext())) {
                            // Make sure the data is the newest
                            getData();
                        } else {
                            loadFromDB();
                        }
                    }
                }
            }
        }
    }

    private void setViews(DramaModel.DataBean data) {
        tv_name.setText(data.getName());
        tv_rating.setText(String.valueOf(data.getRating()));
        tv_created_at.setText(data.getCreated_at());
        tv_total_views.setText(String.valueOf(data.getTotal_views()));
        Glide.with(this.getApplicationContext())
                .load(data.getThumb())
                .into(iv_thumb);
    }

    @Override
    public void loadFromDB() {
        DramaModel.DataBean dramaData = DBHelper.getDramaDataById(this, drama_id);
        if (dramaData != null) {
            setViews(dramaData);
        }
    }

    @Override
    public void onPreExecute() {
        openProgressDialog();
    }

    @Override
    public void onPostExecute(List<DramaModel.DataBean> result) {
        if (result != null) {
            DBHelper.updateDramaDataList(this, result);
            DramaModel.DataBean data = Utils.findDataById(drama_id, result);
            if (data != null) {
                setViews(data);
            }
        }
        closeProgressDialog();
    }

    @Override
    public void getData() {
        releaseTask();
        loadDataTask = new LoadDataTask(this);
        loadDataTask.execute();
    }

    @Override
    public void releaseTask() {
        if (loadDataTask != null) {
            loadDataTask.cancel(true);
            loadDataTask = null;
        }
    }
}
