package top.iscore.freereader;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import top.iscore.freereader.mode.SwitchModeListener;
import xcvf.top.readercore.utils.Constant;
//切换日间夜间模式
public class SwitchModeHandler {

    public SwitchModeListener mSwitchListener;

    public Context context;

    public SwitchModeHandler(SwitchModeListener mSwitchListener, Context context) {
        this.mSwitchListener = mSwitchListener;
        this.context = context;
    }

    /**
     *
     */
    BroadcastReceiver switchModelReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(Constant.ACTION_SWITCH_MODE.equals(intent.getAction())){
                 if(mSwitchListener !=null){
                     mSwitchListener.switchMode(null);
                 }
            }
        }
    };


    public void onCreate() {

        IntentFilter filter = new IntentFilter(Constant.ACTION_SWITCH_MODE);
        LocalBroadcastManager.getInstance(context).registerReceiver(switchModelReceiver,filter);
    }

    public void onDestroy() {

        LocalBroadcastManager.getInstance(context).unregisterReceiver(switchModelReceiver);
    }

}
