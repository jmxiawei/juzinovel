package xcvf.top.readercore.views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.blankj.utilcode.util.LogUtils;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import top.iscore.freereader.R;

public class LoginDialog extends DialogFragment {


    @BindView(R.id.btn_ok)
    Button btnOk;
    Unbinder unbinder;

    Tencent mTencent;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_login, container, false);
        unbinder = ButterKnife.bind(this, view);



        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.btn_ok)
    public void onViewClicked() {

        mTencent = Tencent.createInstance("101523134",getContext().getApplicationContext());


        mTencent.login(this, "all", new IUiListener() {
            @Override
            public void onComplete(Object o) {
                LogUtils.e(o);
            }

            @Override
            public void onError(UiError uiError) {
                LogUtils.e(uiError);
            }

            @Override
            public void onCancel() {

            }
        });

    }
}
