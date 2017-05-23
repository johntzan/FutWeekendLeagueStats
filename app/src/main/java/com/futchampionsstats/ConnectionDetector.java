package com.futchampionsstats;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by yiannitzan on 5/23/17.
 */

public class ConnectionDetector {
    private Context _context;

    public ConnectionDetector(Context context){
        this._context = context;
    }
    /**
     * @return {boolean} true if connected
     * */
    public boolean isConnectingToInternet(){
        ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isConnected = false;
        if (connectivity != null)
        {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            isConnected = info != null &&
                    info.isConnected();
        }
        return isConnected;
    }

    public boolean isConnectedWifi(){
        boolean isOnWifi = false;
        ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivity!=null){
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if(info.getType() == ConnectivityManager.TYPE_WIFI){
                isOnWifi = true;
            }
        }
        return isOnWifi;
    }

    public boolean isConnectedMobile(){
        boolean isOnMobile = false;
        ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivity!=null){
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if(info.getType() == ConnectivityManager.TYPE_MOBILE){
                isOnMobile = true;
            }
        }
        return isOnMobile;
    }
}
