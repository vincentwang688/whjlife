package com.vincentwang.android.tools;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.util.Log;

/**
 * 基于静态内部类实现的单例，保证线程安全的网络信息工具类
 * <per> 使用该工具类之前，记得在AndroidManifest.xml添加权限许可 <xmp>
 * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
 * </xmp>
 * </per>
 * <p/>
 * 安卓判断网络状态，只需要在相应的Activity的相关方法（onCreat/onResum）调用一行代码即可
 * NetworkTools.getInstance(getActivity()).validateNetWork();
 *
 * @author vincentwang
 * @version 0.1
 */
public class NetworkTools {

    public final static String TAG = NetworkTools.class.getSimpleName();

    public final static String NETWORK_CMNET = "CMNET";
    public final static String NETWORK_CMWAP = "CMWAP";
    public final static String NETWORK_WIFI = "WIFI";

    private static NetworkInfo mNetworkInfo = null;
    private Context mContext = null;

    private NetworkTools() {
    }

    public static NetworkTools getInstance() {
        return SingletonHolder.instance;
    }

    public NetworkTools init(Context context) {
        this.mContext = context;
        return this;
    }

    /**
     * 判断网络是否可用
     *
     * @return 是/否
     */
    public boolean isAvailable() {
        ConnectivityManager manager = (ConnectivityManager) mContext
                .getApplicationContext().getSystemService(
                        Context.CONNECTIVITY_SERVICE);
        if (null == manager) {
            return false;
        }
        mNetworkInfo = manager.getActiveNetworkInfo();
        if (null == mNetworkInfo || !mNetworkInfo.isAvailable()) {
            return false;
        }
        return true;
    }

    /**
     * 判断网络是否已连接
     *
     * @return 是/否
     */
    public boolean isConnected() {
        if (!isAvailable()) {
            return false;
        }
        if (!mNetworkInfo.isConnected()) {
            return false;
        }
        return true;
    }

    /**
     * 检查当前环境网络是否可用，不可用跳转至开启网络界面,不设置网络强制关闭当前Activity
     */
    public void validateNetWork() {
        if (!isConnected()) {
            Builder dialogBuilder = new Builder(mContext);
            dialogBuilder.setTitle("网络设置");
            dialogBuilder.setMessage("网络不可用，是否现在设置网络？");
            dialogBuilder.setPositiveButton(android.R.string.ok,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            ((Activity) mContext).startActivityForResult(
                                    new Intent(
                                            Settings.ACTION_SETTINGS),
                                    which);
                        }
                    });
            dialogBuilder.setNegativeButton(android.R.string.cancel,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            dialogBuilder.create();
            dialogBuilder.show();
        }
    }

    /**
     *
     * 获取网络连接信息</br>
     * 无网络：</br>
     * WIFI网络：WIFI</br>
     * WAP网络：CMWAP</br>
     * NET网络：CMNET</br>
     *
     * @return
     */
    public String getNetworkType() {
        if (isConnected()) {
            int type = mNetworkInfo.getType();
            if (ConnectivityManager.TYPE_MOBILE == type) {
                Log.i(TAG,
                        "networkInfo.getExtraInfo()-->"
                                + mNetworkInfo.getExtraInfo());
                if (NETWORK_CMWAP.equals(mNetworkInfo.getExtraInfo()
                        .toLowerCase())) {
                    return NETWORK_CMWAP;
                } else {
                    return NETWORK_CMNET;
                }
            } else if (ConnectivityManager.TYPE_WIFI == type) {
                return NETWORK_WIFI;
            }
        }

        return "";
    }

    private static class SingletonHolder {
        private static NetworkTools instance = new NetworkTools();
    }
}
