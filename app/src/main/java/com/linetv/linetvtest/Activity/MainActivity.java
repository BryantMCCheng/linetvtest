package com.linetv.linetvtest.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.linetv.linetvtest.CustomAdapter;
import com.linetv.linetvtest.DB.DBHelper;
import com.linetv.linetvtest.Model.DramaModel;
import com.linetv.linetvtest.LoadDataTask;
import com.linetv.linetvtest.R;
import com.linetv.linetvtest.Utils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends LoadingActivity implements LoadDataTask.LoadDataTaskCallback,
        SwipeRefreshLayout.OnRefreshListener, SearchView.OnQueryTextListener {

    public static final String TAG = MainActivity.class.getCanonicalName();
    private CustomAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeLayout;
    private LoadDataTask loadDataTask;
    private SearchView mSearchView;
    private String lastQuery = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        onRefresh();
    }

    private void initView() {
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mSwipeLayout = findViewById(R.id.swipe_container);
        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setEnabled(true);
    }

    private void setAdapter(List<DramaModel.DataBean> result) {
        if (result != null) {
            DBHelper.updateDramaDataList(this, result);
            mAdapter = new CustomAdapter(getApplicationContext(), result);
            mRecyclerView.setVisibility(View.VISIBLE);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            Log.d(TAG, "no data");
            mRecyclerView.setVisibility(View.GONE);
            mRecyclerView.setAdapter(null);
        }
    }

    @Override
    public void getData() {
        releaseTask();
        loadDataTask = new LoadDataTask(this);
        loadDataTask.execute();
    }

    @Override
    public void loadFromDB() {
        if (mSwipeLayout.isRefreshing()) {
            mSwipeLayout.setRefreshing(false);
        }
        ArrayList<DramaModel.DataBean> dramaDataList = DBHelper.getDramaDataList(this);
        if (dramaDataList != null) {
            setAdapter(dramaDataList);
        }
        restoreQueryRecord();
    }

    @Override
    public void releaseTask() {
        if (loadDataTask != null) {
            loadDataTask.cancel(true);
            loadDataTask = null;
        }
    }

    @Override
    public void onRefresh() {
        Log.d(TAG, "onRefresh");
        // for SwipeRefresh
        if (mSearchView != null) {
            mSearchView.setQuery("", true);
            saveQueryRecord("");
        }
        if (Utils.isConnected(this)) {
            getData();
        } else {
            loadFromDB();
        }
    }

    @Override
    public void onPreExecute() {
        if (mSwipeLayout.isRefreshing()) {
            mSwipeLayout.setRefreshing(false);
        }
        openProgressDialog();
    }

    @Override
    public void onPostExecute(List<DramaModel.DataBean> result) {
        setAdapter(result);
        restoreQueryRecord();
        closeProgressDialog();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) searchItem.getActionView();
        mSearchView.setOnQueryTextListener(this);
        if (!TextUtils.isEmpty(lastQuery)) {
            mSearchView.setQuery(lastQuery, true);
        }
        return true;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        // Here is where we are going to implement the filter logic
        Log.e(TAG, "onQueryTextChange, query = " + query);
        if (mAdapter != null) {
            mAdapter.getFilter().filter(query);
            saveQueryRecord(query);
        }
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Log.e(TAG, "onQueryTextSubmit, query = " + query);
        return false;
    }

    private void saveQueryRecord(String query) {
        SharedPreferences sp = getSharedPreferences("search", Context.MODE_PRIVATE);
        sp.edit().putString("query", query).apply();
    }

    private void restoreQueryRecord() {
        SharedPreferences sp = getSharedPreferences("search", Context.MODE_PRIVATE);
        lastQuery = sp.getString("query", "");
        if (!TextUtils.isEmpty(lastQuery) && mSearchView != null) {
            mSearchView.setQuery(lastQuery, true);
        }
    }
}
