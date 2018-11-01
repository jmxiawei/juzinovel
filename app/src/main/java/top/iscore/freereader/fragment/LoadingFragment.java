package top.iscore.freereader.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import top.iscore.freereader.R;

/**
 * loading 动画
 * Created by xiaw on 2018/11/1.
 */
public class LoadingFragment extends DialogFragment {

    String msg;

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
        msg = getArguments().getString("msg");
        return inflater.inflate(R.layout.fragment_loading, container, false);
    }
}
