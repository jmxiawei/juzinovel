package top.iscore.freereader.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import top.iscore.freereader.R;

/**
 * loading 动画
 * Created by xiaw on 2018/11/1.
 */
public class LoadingFragment extends DialogFragment {

    String msg = "waiting...";
    @BindView(R.id.tv_msg)
    TextView tvMsg;
    Unbinder unbinder;

    //开始显示时间戳
    long showTime = 0;
    //最小显示时间
    int MIN_TIME = 100;
    Handler mDismissHandler = new Handler(Looper.getMainLooper());


    public static LoadingFragment newOne(String msg, int minShowTime) {
        Bundle bundle = new Bundle();
        bundle.putString("msg", msg);
        bundle.putInt("minShowTime", minShowTime);
        LoadingFragment l = new LoadingFragment();
        l.setArguments(bundle);
        return l;
    }

    public static LoadingFragment newOne(String msg) {
        Bundle bundle = new Bundle();
        bundle.putString("msg", msg);
        LoadingFragment l = new LoadingFragment();
        l.setArguments(bundle);
        return l;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            msg = getArguments().getString("msg");
            MIN_TIME = getArguments().getInt("minShowTime");
        }

        View view = inflater.inflate(R.layout.fragment_loading, container, false);
        unbinder = ButterKnife.bind(this, view);
        tvMsg.setText(msg);
        return view;
    }


    @Override
    public void show(FragmentManager manager, String tag) {
        showTime = System.currentTimeMillis();
        super.show(manager, tag);
    }

    @Override
    public void dismiss() {
        long current = System.currentTimeMillis();
        if (current - showTime > MIN_TIME) {
            super.dismiss();
        } else {
            int left = (int) (MIN_TIME - (current - showTime));
            mDismissHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    LoadingFragment.super.dismiss();
                }
            }, left);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        changeMode();
    }

    private void changeMode() {


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
