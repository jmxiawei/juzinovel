package top.iscore.freereader.adapter;

import android.view.ViewGroup;

import top.iscore.freereader.adapter.holders.BookShelfHolder;
import top.iscore.freereader.fragment.adapters.BaseRecyclerAdapter;
import top.iscore.freereader.fragment.adapters.ViewHolderCreator;
import xcvf.top.readercore.bean.Book;

/**
 * 首页书架
 * Created by xiaw on 2018/10/26.
 */
public class BookShelfAdapter extends BaseRecyclerAdapter<Book> {

    public static final int TYPE_ADD = 1;
    public static final int TYPE_BOOK = 0;

    @Override
    public ViewHolderCreator createViewHolderCreator() {
        return new ViewHolderCreator() {
            @Override
            public BookShelfHolder createByViewGroupAndType(ViewGroup parent, int viewType, Object... p) {
                return new BookShelfHolder(parent.getContext(), parent);
            }
        };
    }


    @Override
    public int getItemCount() {
        return super.getItemCount() + 1;
    }

    @Override
    public Book getItem(int position) {
        if (position == getItemCount() - 1) {
            return new Book(-1);
        }
        return super.getItem(position);
    }


    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return TYPE_ADD;
        } else {
            return TYPE_BOOK;
        }
    }
}
