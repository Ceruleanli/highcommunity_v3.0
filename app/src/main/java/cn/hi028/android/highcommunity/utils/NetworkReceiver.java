package cn.hi028.android.highcommunity.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by Lee_yting on 2016/10/1 0001.
 * 说明：监听网络状态变化
 */
public class NetworkReceiver extends BroadcastReceiver {
static final String Tag="NetworkReceiver--->";
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)){
            Log.d(Tag, "网络状态改变");
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            if(networkInfo != null && networkInfo.isAvailable()){
                int type = networkInfo.getType();
                if(ConnectivityManager.TYPE_WIFI == type){
                }else if(ConnectivityManager.TYPE_MOBILE == type){
                }else if(ConnectivityManager.TYPE_ETHERNET == type){
                }
                String name = networkInfo.getTypeName();
                Log.d(Tag, "当前网络名称：" + name);
                isNoNetwork = false;
            }else{
                Log.d(Tag,"接收广播 没有网络");
                HighCommunityUtils.GetInstantiation().ShowToast("网络状态异常", 0);
                isNoNetwork = true;
            }
        }
    }

    private boolean isNoNetwork;

}
