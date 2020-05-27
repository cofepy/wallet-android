package com.bhex.network.mvx.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import com.bhex.network.R;
import com.bhex.network.receiver.NetWorkStatusChangeReceiver;
import com.bhex.tools.constants.BHConstants;
import com.bhex.tools.language.LocalManageUtil;
import com.gyf.immersionbar.ImmersionBar;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * created by gongdongyang
 * on 2020/2/24
 */
public abstract class BaseActivity<T extends IPresenter> extends AppCompatActivity {

    protected final static String TAG = BaseActivity.class.getSimpleName();

    public Toolbar mToolBar;

    private Unbinder mUnbinder;

    private NetWorkStatusChangeReceiver netWorkChangReceiver;

    private boolean isRegistered = false;

    protected T mPresenter;

    protected ProgressDialog mProgressDialog;

    private int dialogRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        //StatusBarUtil.setStatusColor(this,false,false,R.color.blue);
        setStatusColor();
        initPresenter();

        if(mPresenter!=null){
            getLifecycle().addObserver(mPresenter);
        }
        mUnbinder = ButterKnife.bind(this);

        initReceiver();
        initToolbar();
        initView();
        addEvent();
    }

    protected  void setStatusColor(){
        if(!isNight()){
            if(getStatusColorValue()== BHConstants.STATUS_COLOR_WHITE){
                ImmersionBar.with(this).statusBarColor(R.color.app_bg).statusBarDarkFont(true).barColor(R.color.app_bg).fitsSystemWindows(true).init();
            }else if(getStatusColorValue()== BHConstants.STATUS_COLOR_BLUE){
                ImmersionBar.with(this).statusBarColor(R.color.status_bar_bg_blue).statusBarDarkFont(false).fitsSystemWindows(true).init();
            }else if(getStatusColorValue()== BHConstants.STATUS_COLOR_TRANS){
                ImmersionBar.with(this).transparentStatusBar().statusBarDarkFont(true).init();
            }else{

            }
        }else{
            if(getStatusColorValue()== BHConstants.STATUS_COLOR_WHITE){
                ImmersionBar.with(this).statusBarColor(R.color.app_bg).statusBarDarkFont(false).barColor(R.color.app_bg).fitsSystemWindows(true).init();
            }else if(getStatusColorValue()== BHConstants.STATUS_COLOR_BLUE){
                ImmersionBar.with(this).statusBarColor(R.color.status_bar_bg_blue).statusBarDarkFont(false).fitsSystemWindows(true).init();
            }else if(getStatusColorValue()== BHConstants.STATUS_COLOR_TRANS){
                ImmersionBar.with(this).transparentStatusBar().statusBarDarkFont(true).init();
            }
        }
    }

    @Override
    public void applyOverrideConfiguration(Configuration overrideConfiguration) {
        if (overrideConfiguration != null) {
            int uiMode = overrideConfiguration.uiMode;
            overrideConfiguration.setTo(getBaseContext().getResources().getConfiguration());
            overrideConfiguration.uiMode = uiMode;
        }
        super.applyOverrideConfiguration(overrideConfiguration);
    }

    protected  int getStatusColorValue(){
        return BHConstants.STATUS_COLOR_WHITE;
    }

    protected  void initPresenter(){

    }

    protected abstract int getLayoutId();

    protected abstract void initView();

    protected abstract void addEvent();

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocalManageUtil.attachBaseContext(newBase,""));
    }

    private void initReceiver() {
        netWorkChangReceiver = new NetWorkStatusChangeReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(netWorkChangReceiver, filter);
        isRegistered = true;
    }

    private void initToolbar(){
        mToolBar = findViewById(R.id.toolbar);
        if(mToolBar!=null){
            mToolBar.setTitle("");
            setSupportActionBar(mToolBar);
            getSupportActionBar().setHomeButtonEnabled(isShowBacking());
            getSupportActionBar().setDisplayHomeAsUpEnabled(isShowBacking());
            getSupportActionBar().setDisplayShowTitleEnabled(false);

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mUnbinder!=null){
            mUnbinder.unbind();
        }

        //解绑
        if (isRegistered) {
            unregisterReceiver(netWorkChangReceiver);
        }
    }

    protected boolean isShowBacking() {
        return true;
    }

    /**
     * 显示对话框
     */
    public void showProgressbarLoading() {
        showProgressbarLoading("",getResources().getString(R.string.http_loading));
    }

    public void showProgressbarLoading(String title, String hint) {
        if (mProgressDialog != null && mProgressDialog.isShowing())
            return;
        this.mProgressDialog = ProgressDialog.show(this, title, hint);
        this.mProgressDialog.setCancelable(true);
        this.mProgressDialog.setCanceledOnTouchOutside(false);
    }

    /**
     * 隐藏对话框
     */
    public void dismissProgressDialog() {
        if (mProgressDialog == null)
            return;

        if (mProgressDialog.isShowing()  && --dialogRef <= 0){
            this.mProgressDialog.dismiss();
        }

    }

    public boolean isNight(){
       return (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES);
    }


    public T getPresenter() {
        return mPresenter;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
