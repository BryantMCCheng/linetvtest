package com.linetv.linetvtest.Activity;

public abstract class LoadingActivity extends ProgressActivity {

    public abstract void getData();
    public abstract void loadFromDB();
    public abstract void releaseTask();

    @Override
    protected void onDestroy() {
        releaseTask();
        super.onDestroy();
    }
}
