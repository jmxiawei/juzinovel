package top.iscore.freereader.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.ArrayList;

import top.iscore.freereader.R;
import top.iscore.freereader.adapter.holders.BookHolder;
import top.iscore.freereader.fragment.adapters.CommonViewHolder;
import xcvf.top.readercore.ReaderActivity;
import xcvf.top.readercore.bean.Book;

/**
 * 书架
 * Created by xiaw on 2018/9/18.
 */
public class BookshelfFragment extends BaseListFragment<Book> {

    ArrayList<Book> bookArrayList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookshelf, container, false);
        initRecyclerView(view);
        bookArrayList.add(new Book());
        getAdapter().setDataList(bookArrayList);
        return view;
    }

    @Override
    public CommonViewHolder getHolderCreateByViewGroupAndType(ViewGroup parent, int viewType, Object... p) {
        return new BookHolder(parent.getContext(), parent);
    }


    @Override
    public void onRefreshLoad() {
        swipeRefreshLayout.finishRefresh(true);
    }


    @Override
    public void onReachBottomLoad() {
        swipeRefreshLayout.finishLoadMore(true);
    }


    @Override
    public void onItemClick(CommonViewHolder holder, int position, Book item) {
        ReaderActivity.toReadPage(getActivity(), item);
    }
}
