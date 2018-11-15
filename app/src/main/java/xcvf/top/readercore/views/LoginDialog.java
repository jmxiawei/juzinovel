package xcvf.top.readercore.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import top.iscore.freereader.R;
import top.iscore.freereader.UserInfoChangedHandler;
import top.iscore.freereader.mvp.presenters.UserPresenter;
import top.iscore.freereader.mvp.view.ILoginView;
import xcvf.top.readercore.bean.User;
import xcvf.top.readercore.utils.Constant;

/**
 * 登录弹框
 */
public class LoginDialog extends DialogFragment implements ILoginView {


    Unbinder unbinder;

    Tencent mTencent;
    @BindView(R.id.img_qq)
    View imgQq;
    UserPresenter userPresenter;
    @BindView(R.id.ll_progress)
    ProgressBar llProgress;
    UserInfoChangedHandler.OnUserChanged mFinishTask;
    @BindView(R.id.ll_qq)
    LinearLayout llQq;
    @BindView(R.id.tv_logout)
    TextView tvLogout;
    @BindView(R.id.ll_logout)
    LinearLayout llLogout;
    User mUser;

    public LoginDialog setFinishTask(UserInfoChangedHandler.OnUserChanged mFinishTask) {
        this.mFinishTask = mFinishTask;
        return this;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_login, container, false);
        unbinder = ButterKnife.bind(this, view);
        mTencent = Tencent.createInstance("101523134", getContext().getApplicationContext());
        userPresenter = new UserPresenter();
        userPresenter.attachView(this);

        initView();
        return view;
    }

    private void initView() {
        mUser = User.currentUser();
        if (mUser.getUid() > 0) {
            llQq.setVisibility(View.GONE);
            llLogout.setVisibility(View.VISIBLE);
        } else {
            llLogout.setVisibility(View.GONE);
            llQq.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Tencent.onActivityResultData(requestCode, resultCode, data, listener);
    }


    private IUiListener listener = new IUiListener() {
        @Override
        public void onComplete(Object o) {
            JSONObject jsonObject = (JSONObject) o;
            LogUtils.e(o);
            final String openId = jsonObject.optString(Constants.PARAM_OPEN_ID);
            String token = jsonObject.optString(Constants.PARAM_ACCESS_TOKEN);
            String expires = jsonObject.optString(Constants.PARAM_EXPIRES_IN);
            mTencent.setAccessToken(token, expires);
            mTencent.setOpenId(openId);
            UserInfo userInfo = new UserInfo(getContext(), mTencent.getQQToken());
            userInfo.getUserInfo(new IUiListener() {
                @Override
                public void onComplete(Object o) {
                    JSONObject info = (JSONObject) o;
                    userPresenter.login(openId, openId, info.optString("nickname"), info.optString("figureurl_qq_2"), info.optString("gender"));
                }

                @Override
                public void onError(UiError uiError) {
                    ToastUtils.showShort("登录失败");
                    loginAction("qq", false);
                }

                @Override
                public void onCancel() {
                    ToastUtils.showShort("登录失败");
                    loginAction("qq", false);
                }
            });
        }

        @Override
        public void onError(UiError uiError) {
            ToastUtils.showShort("登录失败");
            loginAction("qq", false);
        }

        @Override
        public void onCancel() {
            ToastUtils.showShort("登录失败");
            loginAction("qq", false);
        }
    };


    @OnClick(R.id.img_qq)
    public void onViewClicked() {
        loginAction("qq", true);
        mTencent.login(this, "all", listener);
    }

    @Override
    public void loginAction(String source, boolean start) {
        if (start) {
            llProgress.setVisibility(View.VISIBLE);
            imgQq.setEnabled(false);
        } else {
            llProgress.setVisibility(View.GONE);
            imgQq.setEnabled(true);
        }
    }

    @Override
    public void onLoginSuccess(User user) {
        if (user != null) {
            user.save();
            Intent intent = new Intent(Constant.ACTION_LOGIN_INFO);
            intent.putExtra("user", user);
            LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
            if (mFinishTask != null) {
                mFinishTask.onChanged(user);
            }
            dismiss();
        }
        loginAction("qq", false);
    }

    @OnClick(R.id.tv_logout)
    public void onViewClicked1() {
        mTencent.logout(getContext());
        mUser.deleteUser();
        Intent intent = new Intent(Constant.ACTION_LOGIN_INFO);
        intent.putExtra("user", User.currentUser());
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
        if (mFinishTask != null) {
            mFinishTask.onChanged(User.currentUser());
        }
        dismiss();
    }
}
