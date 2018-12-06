package top.iscore.freereader.mvp.presenters;

import android.support.annotation.NonNull;
import android.telecom.Call;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import java.util.ArrayList;

import retrofit2.Callback;
import retrofit2.Response;
import top.iscore.freereader.http.BaseHttpHandler;
import top.iscore.freereader.http.BaseModel;
import top.iscore.freereader.http.BookService;
import top.iscore.freereader.mvp.view.SearchView;
import xcvf.top.readercore.bean.Book;

/**
 *
 * 搜索书籍
 * Created by xiaw on 2018/11/2.
 */

public class SearchPresenter extends MvpBasePresenter<SearchView> {


    /**
     * 搜索书籍
     * @param keyword
     */
    public  void searchBook(String keyword,int type,int ranklistid,int page){


      ifViewAttached(new ViewAction<SearchView>() {
          @Override
          public void run(@NonNull SearchView view) {
              view.showLoading();
          }
      });
      retrofit2.Call<BaseModel<ArrayList<Book>>> arrayListCall =  BaseHttpHandler.create().getProxy(BookService.class).search("Book.search",keyword,type,ranklistid,page);
      arrayListCall.enqueue(new Callback<BaseModel<ArrayList<Book>>>() {
          @Override
          public void onResponse(retrofit2.Call<BaseModel<ArrayList<Book>>> call, final Response<BaseModel<ArrayList<Book>>> response) {

                ifViewAttached(new ViewAction<SearchView>() {
                    @Override
                    public void run(@NonNull SearchView view) {
                        if(response!=null && response.body()!=null){
                            view.onLoad(response.body().getData());
                        }else {
                            view.onLoad(null);
                        }
                    }
                });
          }

          @Override
          public void onFailure(retrofit2.Call<BaseModel<ArrayList<Book>>> call, Throwable t) {
                    ifViewAttached(new ViewAction<SearchView>() {
                        @Override
                        public void run(@NonNull SearchView view) {
                             view.onLoad(null);
                        }
                    });
          }
      });
    }
}
