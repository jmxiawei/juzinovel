package top.iscore.freereader.mvp.presenters;

import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import top.iscore.freereader.http.BaseHttpHandler;
import top.iscore.freereader.http.BaseModel;
import top.iscore.freereader.http.BookService;
import top.iscore.freereader.mvp.view.ILoginView;
import xcvf.top.readercore.bean.User;

public class UserPresenter extends MvpBasePresenter<ILoginView> {


    /**
     * @param u
     * @param p
     * @param
     */
    public void login(String u, String p, String nick, String avatar, String gender) {

        ifViewAttached(new ViewAction<ILoginView>() {
            @Override
            public void run(@NonNull ILoginView view) {
                 view.loginAction("qq",true);
            }
        });

        Call<BaseModel<User>> modelCall = BaseHttpHandler.create().getProxy(BookService.class).login("User.Login", u, p, avatar, nick, gender);
        modelCall.enqueue(new Callback<BaseModel<User>>() {
            @Override
            public void onResponse(Call<BaseModel<User>> call, final Response<BaseModel<User>> response) {
                ifViewAttached(new ViewAction<ILoginView>() {
                    @Override
                    public void run(@NonNull ILoginView view) {
                        if (response.isSuccessful()) {
                            if (response.body().getCode() == 0) {
                                view.onLoginSuccess(response.body().getData());
                            } else {
                                view.onLoginSuccess(null);
                            }
                        }

                    }
                });
            }

            @Override
            public void onFailure(Call<BaseModel<User>> call, Throwable t) {
                ifViewAttached(new ViewAction<ILoginView>() {
                    @Override
                    public void run(@NonNull ILoginView view) {
                        view.onLoginSuccess(null);
                    }
                });
            }
        });
    }
}
