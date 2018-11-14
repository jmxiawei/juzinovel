package top.iscore.freereader;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.LocalBroadcastManager;

import java.util.ArrayList;

import xcvf.top.readercore.bean.User;
import xcvf.top.readercore.utils.Constant;

/**
 * 用户信息更新了
 */
public class UserInfoChangedHandler {


    /**
     *
     */
    public interface OnUserChanged {
        void onChanged(User user);
    }

    private Handler handler = new Handler(Looper.getMainLooper());
    private Context context;
    private ArrayList<OnUserChanged> onUserChangedArrayList = new ArrayList<>();


    public static UserInfoChangedHandler newInstance(Context context) {
        return new UserInfoChangedHandler(context);
    }


    public UserInfoChangedHandler put(OnUserChanged onUserChanged) {
        if (!onUserChangedArrayList.contains(onUserChanged)) {
            onUserChangedArrayList.add(onUserChanged);
        }
        return this;
    }


    private void notifyOnUserChangedListeners() {
        for (OnUserChanged onUserChanged :
                onUserChangedArrayList) {
            if (onUserChanged != null) {
                onUserChanged.onChanged(User.currentUser());
            }
        }
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && Constant.ACTION_LOGIN_INFO.equals(intent.getAction())) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        notifyOnUserChangedListeners();
                    }
                });
            }
        }
    };


    public UserInfoChangedHandler register() {
        IntentFilter filter = new IntentFilter(Constant.ACTION_LOGIN_INFO);
        LocalBroadcastManager.getInstance(context).registerReceiver(broadcastReceiver, filter);
        return this;
    }

    public void unregister() {
        IntentFilter filter = new IntentFilter(Constant.ACTION_LOGIN_INFO);
        LocalBroadcastManager.getInstance(context).unregisterReceiver(broadcastReceiver);
    }

    private UserInfoChangedHandler(Context context) {
        this.context = context;
    }

}
