package com.linetv.linetvtest.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Window;

import com.linetv.linetvtest.R;

public abstract class ProgressActivity extends AppCompatActivity {

    public ProgressDialog mProgressDialog;
    public int timeout = 20 * 1000;
    public Handler mHandler = new Handler();
    public Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            closeProgressDialog();
        }
    };

    @Override
    protected void onDestroy() {
        closeProgressDialog();
        super.onDestroy();
    }

    public void openProgressDialog() {
        if (mProgressDialog == null) mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);
        if (!isFinishing()) {
            mProgressDialog.show();
        }
        if (mHandler == null) mHandler = new Handler();
        mHandler.postDelayed(mRunnable, timeout);
        Window window = mProgressDialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            mProgressDialog.setContentView(R.layout.progressdialog_component);
        }
        mProgressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    closeProgressDialog();
                }
                return false;
            }
        });
    }

    public void closeProgressDialog() {
        if ((mProgressDialog != null) && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
        if (mHandler != null) {
            mHandler.removeCallbacks(mRunnable);
            mHandler = null;
        }
    }
}
