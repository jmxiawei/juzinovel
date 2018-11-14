package top.iscore.freereader.mvp.view;

import com.hannesdorfmann.mosby3.mvp.MvpView;

import xcvf.top.readercore.bean.User;

public interface ILoginView extends MvpView {


    void loginAction(String source,boolean start);

    void onLoginSuccess(User user);
}
