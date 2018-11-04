package top.iscore.freereader.adapter;

import android.view.ViewGroup;

import top.iscore.freereader.adapter.holders.BookHolder;
import top.iscore.freereader.fragment.adapters.BaseRecyclerAdapter;
import top.iscore.freereader.fragment.adapters.CommonViewHolder;
import top.iscore.freereader.fragment.adapters.ViewHolderCreator;
import xcvf.top.readercore.bean.Book;

public class BookAdapter extends BaseRecyclerAdapter<Book> {
    @Override
    public ViewHolderCreator createViewHolderCreator() {
        return new ViewHolderCreator() {
            @Override
            public BookHolder createByViewGroupAndType(ViewGroup parent, int viewType, Object... p) {
                return new BookHolder(parent.getContext(),parent) ;
            }
        };
    }
}
