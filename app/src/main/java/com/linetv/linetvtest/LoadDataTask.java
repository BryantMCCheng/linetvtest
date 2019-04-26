package com.linetv.linetvtest;

import android.app.Activity;
import android.os.AsyncTask;

import com.linetv.linetvtest.Model.DramaModel;

import java.lang.ref.WeakReference;
import java.util.List;

public class LoadDataTask extends AsyncTask<Void, Void, List<DramaModel.DataBean>> {

    private WeakReference<Activity> mActivity;

    public interface LoadDataTaskCallback {
        void onPreExecute();
        void onPostExecute(List<DramaModel.DataBean> list);
    }

    public LoadDataTask(Activity activity) {
        this.mActivity = new WeakReference<>(activity);
    }

    @Override
    protected void onPreExecute() {
        final Activity activity = mActivity.get();
        if (activity != null) {
            ((LoadDataTaskCallback) activity).onPreExecute();
        }
    }

    @Override
    protected List<DramaModel.DataBean> doInBackground(Void... voids) {
        return Utils.getData();
    }

    @Override
    protected void onPostExecute(List<DramaModel.DataBean> result) {
        super.onPostExecute(result);
        final Activity activity = mActivity.get();
        if (activity != null) {
            ((LoadDataTaskCallback) activity).onPostExecute(result);
        }
    }
}
