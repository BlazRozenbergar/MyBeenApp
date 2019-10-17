package si.example.mybeenapp.endpoint;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;

import androidx.lifecycle.LiveData;

public class NetworkUtil extends LiveData<Boolean> {
    private Application mApplication;
    private BroadcastReceiver mBroadcastReceiver;

    public NetworkUtil(Application application) {
        this.mApplication = application;
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager)mApplication.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnectedOrConnecting();
    }

    @Override
    protected void onActive() {
        super.onActive();
        registerBroadcastReceiver();
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        unregisterBroadcastReceiver();
    }

    private void registerBroadcastReceiver() {
        if (mBroadcastReceiver == null) {

            IntentFilter filter = new IntentFilter();
            filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            mBroadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (intent.getExtras() != null && intent.getExtras().getParcelable(ConnectivityManager.EXTRA_NETWORK_INFO) != null) {
                        postValue(((NetworkInfo) intent.getExtras().getParcelable(ConnectivityManager.EXTRA_NETWORK_INFO)).getState() == NetworkInfo.State.CONNECTED);
                    }
                }
            };

            mApplication.registerReceiver(mBroadcastReceiver, filter);
        }
    }

    private void unregisterBroadcastReceiver() {
        if (mBroadcastReceiver != null) {
            mApplication.unregisterReceiver(mBroadcastReceiver);
            mBroadcastReceiver = null;
        }
    }
}
