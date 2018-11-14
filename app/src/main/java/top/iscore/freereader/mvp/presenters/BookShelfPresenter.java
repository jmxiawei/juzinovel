package top.iscore.freereader.mvp.presenters;

import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import top.iscore.freereader.http.BaseHttpHandler;
import top.iscore.freereader.http.BaseModel;
import top.iscore.freereader.http.BookService;
import top.iscore.freereader.mvp.view.BookShelfView;
import xcvf.top.readercore.bean.Book;
import xcvf.top.readercore.bean.Category;

/**
 * 书架
 * Created by xiaw on 2018/9/18.
 */
public class BookShelfPresenter extends MvpBasePresenter<BookShelfView> {


    /**
     * 获取书架数据
     *
     * @param
     */
    public void loadAllCate() {

        Call<BaseModel<ArrayList<Category>>> resCall = BaseHttpHandler.create().getProxy(BookService.class).allcate("Book.Allcate");
        resCall.enqueue(new Callback<BaseModel<ArrayList<Category>>>() {
            @Override
            public void onResponse(Call<BaseModel<ArrayList<Category>>> call, final Response<BaseModel<ArrayList<Category>>> response) {

                ifViewAttached(new ViewAction<BookShelfView>() {
                    @Override
                    public void run(@NonNull BookShelfView view) {
                        if (response != null && response.isSuccessful()) {
                            view.onLoadAllCate(response.body().getData());
                        } else {
                            view.onLoadAllCate(null);
                        }
                    }
                });

            }

            @Override
            public void onFailure(Call<BaseModel<ArrayList<Category>>> call, Throwable t) {
                ifViewAttached(new ViewAction<BookShelfView>() {
                    @Override
                    public void run(@NonNull BookShelfView view) {
                        view.onLoadAllCate(null);
                    }
                });
            }
        });
    }

    /**
     * 获取书架数据
     *
     * @param userid
     */
    public void loadBookShelf(int userid) {

        Call<BaseModel<ArrayList<Book>>> resCall = BaseHttpHandler.create().getProxy(BookService.class).getBookShelf("Book.getBookshelf", String.valueOf(userid));
        resCall.enqueue(new Callback<BaseModel<ArrayList<Book>>>() {
            @Override
            public void onResponse(Call<BaseModel<ArrayList<Book>>> call, final Response<BaseModel<ArrayList<Book>>> response) {

                ifViewAttached(new ViewAction<BookShelfView>() {
                    @Override
                    public void run(@NonNull BookShelfView view) {
                        view.setData(response.body().getData());
                    }
                });

            }

            @Override
            public void onFailure(Call<BaseModel<ArrayList<Book>>> call, Throwable t) {
                ifViewAttached(new ViewAction<BookShelfView>() {
                    @Override
                    public void run(@NonNull BookShelfView view) {
                        view.setData(null);
                    }
                });
            }
        });
    }


    /**
     * 新增书架
     *
     * @param userid
     */
    public void addBookShelf(int userid, String bookid) {


        ifViewAttached(new ViewAction<BookShelfView>() {
            @Override
            public void run(@NonNull BookShelfView view) {
                view.showLoading(true);
            }
        });

        Call<BaseModel<Book>> resCall = BaseHttpHandler.create().getProxy(BookService.class).addBookShelf("Book.addShelf", String.valueOf(userid), bookid);
        resCall.enqueue(new Callback<BaseModel<Book>>() {
            @Override
            public void onResponse(Call<BaseModel<Book>> call, final Response<BaseModel<Book>> response) {
                ifViewAttached(new ViewAction<BookShelfView>() {
                    @Override
                    public void run(@NonNull BookShelfView view) {
                        view.onLoadBookDetail(response.body().getData());
                        view.showLoading(false);
                    }
                });
            }

            @Override
            public void onFailure(Call<BaseModel<Book>> call, Throwable t) {
                ifViewAttached(new ViewAction<BookShelfView>() {
                    @Override
                    public void run(@NonNull BookShelfView view) {
                        view.onLoadBookDetail(null);
                        view.showLoading(false);
                    }
                });
            }
        });

    }

    /**
     * 删除书架
     *
     * @param shelfid
     */
    public void deleteBookShelf(int userid,String shelfid,String bookid) {
        ifViewAttached(new ViewAction<BookShelfView>() {
            @Override
            public void run(@NonNull BookShelfView view) {
                view.showLoading(true);
            }
        });

        Call<BaseModel<Book>> resCall = BaseHttpHandler.create().getProxy(BookService.class).deleteBookShelf("Book.deleteShelf",String.valueOf(userid), shelfid,bookid);
        resCall.enqueue(new Callback<BaseModel<Book>>() {
            @Override
            public void onResponse(Call<BaseModel<Book>> call, final Response<BaseModel<Book>> response) {

                ifViewAttached(new ViewAction<BookShelfView>() {
                    @Override
                    public void run(@NonNull BookShelfView view) {
                        view.onLoadBookDetail(response.body().getData());
                        view.showLoading(false);
                    }
                });
            }

            @Override
            public void onFailure(Call<BaseModel<Book>> call, Throwable t) {
                ifViewAttached(new ViewAction<BookShelfView>() {
                    @Override
                    public void run(@NonNull BookShelfView view) {
                        view.onLoadBookDetail(null);
                        view.showLoading(false);
                    }
                });
            }
        });

    }

    /**
     * 书籍
     *
     * @param userid
     * @param bookid
     */
    public void loadBookDetail(int userid, String bookid) {
        final Call<BaseModel<Book>> resCall = BaseHttpHandler.create().getProxy(BookService.class).detail("Book.Detail", bookid, String.valueOf(userid));
        resCall.enqueue(new Callback<BaseModel<Book>>() {
            @Override
            public void onResponse(Call<BaseModel<Book>> call, final Response<BaseModel<Book>> response) {

                ifViewAttached(new ViewAction<BookShelfView>() {
                    @Override
                    public void run(@NonNull BookShelfView view) {
                        if (response != null && response.isSuccessful()) {
                            view.onLoadBookDetail(response.body().getData());
                        } else {
                            view.onLoadBookDetail(null);
                        }
                    }
                });

            }

            @Override
            public void onFailure(Call<BaseModel<Book>> call, Throwable t) {
                ifViewAttached(new ViewAction<BookShelfView>() {
                    @Override
                    public void run(@NonNull BookShelfView view) {
                        view.onLoadBookDetail(null);
                    }
                });

            }
        });
    }


}
