package top.iscore.freereader;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby3.mvp.MvpActivity;

import top.iscore.freereader.mvp.presenters.SearchPresenter;
import top.iscore.freereader.mvp.view.SearchView;

/**搜索
 * Created by xiaw on 2018/11/2.
 */
public class SearchActivity extends MvpActivity<SearchView, SearchPresenter> {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
    }

    @NonNull
    @Override
    public SearchPresenter createPresenter() {
        return new SearchPresenter();
    }
}
