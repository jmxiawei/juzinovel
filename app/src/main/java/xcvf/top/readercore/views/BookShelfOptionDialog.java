package xcvf.top.readercore.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import top.iscore.freereader.BookDetailActivity;
import top.iscore.freereader.R;
import top.iscore.freereader.fragment.adapters.BaseRecyclerAdapter;
import top.iscore.freereader.fragment.adapters.CommonViewHolder;
import top.iscore.freereader.fragment.adapters.OnRecyclerViewItemClickListener;
import top.iscore.freereader.fragment.adapters.ViewHolderCreator;
import top.iscore.freereader.mode.Colorful;
import top.iscore.freereader.mode.setter.ViewBackgroundColorSetter;
import xcvf.top.readercore.bean.Book;
import xcvf.top.readercore.bean.Category;
import xcvf.top.readercore.styles.ModeProvider;

/**
 * 长按
 */
public class BookShelfOptionDialog extends DialogFragment {


    @BindView(R.id.recycler)
    RecyclerView recycler;
    Unbinder unbinder;
    BAdapter mBAdapter;

    Book mBook;

    public void setBook(Book mBook) {
        this.mBook = mBook;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_dialog_bookshelf, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        changeMode();
    }

    private void changeMode() {
        new Colorful.Builder(getActivity())
                .setter(new ViewBackgroundColorSetter(getView(),R.attr.colorPrimary))
                .create()
                .setTheme(ModeProvider.getCurrentModeTheme());
    }

    private void initView() {
        mBAdapter = new BAdapter();
        recycler.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        recycler.setAdapter(mBAdapter);
        mBAdapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener<Category>() {
            @Override
            public void onRecyclerViewItemClick(CommonViewHolder holder, int position, Category item) {
                if(item.getId() == 0){
                    BookDetailActivity.toBookDetail(getActivity(),mBook.extern_bookid);
                }else if(item.getId() == 1){

                }else if(item.getId() == 2){

                }


            }
        });
        List<Category> categories = new ArrayList<>();
        categories.add(new Category(0,"书籍详情",0));
        categories.add(new Category(1,"删除书籍",1));
        categories.add(new Category(2,"缓存全本",2));
        mBAdapter.setDataList(categories);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    private  static  class BAdapter extends BaseRecyclerAdapter<Category>{

        @Override
        public ViewHolderCreator createViewHolderCreator() {
            return new ViewHolderCreator() {
                @Override
                public CommonViewHolder<Category> createByViewGroupAndType(ViewGroup parent, int viewType, Object... p) {
                    return new CommonViewHolder<Category>(parent.getContext(),parent,R.layout.item_bookshelf_option) {
                        @Override
                        public void bindData(Category category, int position) {
                            TextView tv_option = itemView.findViewById(R.id.tv_option);
                            tv_option.setText(category.getName());
                        }
                    };
                }
            };
        }
    }

}
