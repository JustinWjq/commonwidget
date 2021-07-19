package com.common.widget.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;

import com.common.widget.immersionbar.TxImmersionBar;
import com.common.widget.titlebar.TitleBar;
import com.common.widget.R;
import com.common.widget.aciton.BundleAction;
import com.common.widget.aciton.NetStatusAction;
import com.common.widget.aciton.TitleBarAction;
import com.common.widget.toast.ToastUtils;
import com.common.widget.util.NetUtil;


public abstract class  BaseActivity extends FragmentActivity implements TitleBarAction, NetStatusAction, BundleAction {
    private NetWorkChangeReceive mNetWorkReceive;
    public final String TAG = getClass().getSimpleName();

    /** 标题栏对象 */
    private TitleBar mTitleBar;

    /** 状态栏沉浸 */
    private TxImmersionBar mImmersionBar;

    private void registerNetWork() {
        if (mNetWorkReceive==null){
            mNetWorkReceive=new NetWorkChangeReceive();
            IntentFilter filter=new IntentFilter();
            filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            registerReceiver(mNetWorkReceive, filter);
        }
    }


    public boolean isSplsh = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isFullScreen();

        setContentView(getLayoutId());
        initView();
        initData();
    }

    public abstract int getLayoutId();

    public void isFullScreen() {
    }

    public void setCurrentSplash(boolean isSplsh) {
        this.isSplsh = isSplsh;
    }

    public void initView() {
        if (getTitleBar() != null) {
            getTitleBar().setOnTitleBarListener(this);
        }

        // 初始化沉浸式状态栏
        if (isStatusBarEnabled()) {
            getStatusBarConfig().init();

            // 设置标题栏沉浸
            if (getTitleBar() != null) {
                TxImmersionBar.setTitleBar(this, getTitleBar());

            }
        }
    }

    public void initData() {

    }


    @Override
    public Bundle getBundle() {
        return getIntent().getExtras();
    }

    @Override
    protected void onStart() {
        super.onStart();

        registerNetWork();
    }



    private void unregisterNetWork() {
        if (mNetWorkReceive!=null){
            unregisterReceiver(mNetWorkReceive);
            mNetWorkReceive=null;
        }
    }




    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterNetWork();
    }

    public <T extends SystemBase>T getSystem(Class<T> tClass){
        return SystemManager.getInstance().getSystem(tClass);
    }
    public class NetWorkChangeReceive extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                //检查网络状态的类型
                NetUtil.NetworkType netWrokState = NetUtil.getNetWorkState(context);
                isNetConnect(netWrokState);
            }
        }
    }

    private void isNetConnect(NetUtil.NetworkType netMobile ) {
        switch (netMobile) {
            case NETWORK_WIFI://wifi
                changeWifi();
                break;
            case NETWORK_2G://2g
            case NETWORK_3G://3g
            case NETWORK_4G://4g
                changeMobile();
                break;
            case NETWORK_NO://没有网络
                changeNetNull();
                break;
        }
    }




    /**
     * 设置标题栏的标题
     */
    @Override
    public void setTitle(@StringRes int id) {
        setTitle(getString(id));
    }

    /**
     * 设置标题栏的标题
     */
    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        if (getTitleBar() != null) {
            getTitleBar().setTitle(title);
        }
    }

    @Override
    @Nullable
    public TitleBar getTitleBar() {
        if (mTitleBar == null) {
            mTitleBar = obtainTitleBar(getContentView());
        }
        return mTitleBar;
    }

    /**
     * 一般情况下，左上角返回按钮默认直接finish当前页面
     * 如需要特殊处理，可重写onbackpressed（不需要继承父类方法）单独处理，比如某些页面返回需要特殊的提示
     *
     * */
    @Override
    public void onLeftClick(View view) {
        onBackPressed();
    }

    /**
     * 和 setContentView 对应的方法
     */
    public ViewGroup getContentView() {
        return findViewById(Window.ID_ANDROID_CONTENT);
    }


    protected void hideInput() {
        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE) ;
        View view = getWindow().peekDecorView();
        if (null != view) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    /**
     * 是否使用沉浸式状态栏
     */
    protected boolean isStatusBarEnabled() {
        return true;
    }

    /**
     * 状态栏字体深色模式
     */
    protected boolean isStatusBarDarkFont() {
        return false;
    }

    /**
     * 获取状态栏沉浸的配置对象
     */
    @NonNull
    public TxImmersionBar getStatusBarConfig() {
        if (mImmersionBar == null) {
            mImmersionBar = createStatusBarConfig();
        }
        return mImmersionBar;
    }

    /**
     * 初始化沉浸式状态栏
     */
    @NonNull
    protected TxImmersionBar createStatusBarConfig() {
        return TxImmersionBar.with(this)
                // 默认状态栏字体颜色为黑色
                .statusBarDarkFont(isStatusBarDarkFont())
                // 指定导航栏背景颜色
                .navigationBarColor(R.color.tx_colorwhite)
                .statusBarColor(R.color.tx_colorPrimary)
                // 状态栏字体和导航栏内容自动变色，必须指定状态栏颜色和导航栏颜色才可以自动变色
                .autoDarkModeEnable(true, 0.2f);
    }

    /*
    * 显示toast
    * */
    public  void showToastMsg(String msg){
        ToastUtils.show(msg);
    }

    /*
     * 退出app
     * */
    public void  exitApp(){
        System.exit(0);

    }

}
