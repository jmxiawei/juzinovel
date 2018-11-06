package top.iscore.freereader;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hannesdorfmann.mosby3.mvp.MvpActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import top.iscore.freereader.adapter.SpaceItemDivider;
import top.iscore.freereader.fragment.adapters.BaseRecyclerAdapter;
import top.iscore.freereader.fragment.adapters.CommonViewHolder;
import top.iscore.freereader.fragment.adapters.OnRecyclerViewItemClickListener;
import top.iscore.freereader.fragment.adapters.ViewHolderCreator;
import top.iscore.freereader.mode.Colorful;
import top.iscore.freereader.mode.setter.ViewBackgroundColorSetter;
import top.iscore.freereader.mode.setter.ViewGroupSetter;
import top.iscore.freereader.mvp.presenters.BookShelfPresenter;
import top.iscore.freereader.mvp.view.BookShelfView;
import xcvf.top.readercore.bean.Book;
import xcvf.top.readercore.bean.Category;
import xcvf.top.readercore.styles.ModeProvider;

/**
 * 分类列表
 */
public class CateListActivity extends MvpActivity<BookShelfView, BookShelfPresenter> implements BookShelfView {

    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ll_title)
    LinearLayout llTitle;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    MAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cate_list);
        ButterKnife.bind(this);
        initView();
        presenter.loadAllCate();
    }


    @Override
    protected void onResume() {
        super.onResume();
        changeMode();
    }

    private void changeMode() {
        new Colorful.Builder(this).setter(new ViewBackgroundColorSetter(llTitle, R.attr.colorAccent))
                .setter(new ViewGroupSetter(recycler, R.attr.colorPrimary)
                        .childViewTextColor(R.id.tv_name, R.attr.text_color)
                        .childViewTextColor(R.id.tv_num, R.attr.text_second_color))
                .create()
                .setTheme(ModeProvider.getCurrentModeTheme());
    }

    private void initView() {
        tvTitle.setText("分类");
        recycler.addItemDecoration(new SpaceItemDivider());
        recycler.setLayoutManager(new GridLayoutManager(this,3));
        mAdapter = new MAdapter();
        recycler.setAdapter(mAdapter);
        mAdapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener<Category>() {
            @Override
            public void onRecyclerViewItemClick(CommonViewHolder holder, int position, Category item) {
                 BookListActivity.toBookList(CateListActivity.this,item.getName(),0,item.getName());
            }
        });
    }

    private static class  MAdapter extends BaseRecyclerAdapter<Category>{


        @Override
        public ViewHolderCreator createViewHolderCreator() {
            return new ViewHolderCreator() {
                @Override
                public CommonViewHolder<Category> createByViewGroupAndType(ViewGroup parent, int viewType, Object... p) {
                    return new CommonViewHolder<Category>(parent.getContext(),parent,R.layout.item_holder_cate) {
                        @Override
                        public void bindData(Category category, int position) {
                            TextView tv_name = itemView.findViewById(R.id.tv_name);
                            TextView tv_num = itemView.findViewById(R.id.tv_num);
                            tv_name.setText(category.getName());
                            tv_num.setText(category.getIntValue()+"本");
                        }
                    };
                }
            };
        }
    }

    @NonNull
    @Override
    public BookShelfPresenter createPresenter() {
        return new BookShelfPresenter();
    }

    @Override
    public void onLoadBookDetail(Book book) {

    }

    @Override
    public void onLoadAllCate(List<Category> categories) {
        mAdapter.setDataList(categories);
    }

    @Override
    public void showLoading(boolean pullToRefresh) {

    }

    @Override
    public void showContent() {

    }

    @Override
    public void showError(Throwable e, boolean pullToRefresh) {

    }

    @Override
    public void setData(List<Book> data) {

    }

    @Override
    public void loadData(boolean pullToRefresh) {

    }

    @OnClick(R.id.img_back)
    public void onViewClicked() {
        finish();
    }
}
