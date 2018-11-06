package top.iscore.freereader.adapter;

import android.view.ViewGroup;

import top.iscore.freereader.adapter.holders.PriorityBookHolder;
import top.iscore.freereader.adapter.holders.SearchBookHolder;
import top.iscore.freereader.fragment.adapters.BaseRecyclerAdapter;
import top.iscore.freereader.fragment.adapters.CommonViewHolder;
import top.iscore.freereader.fragment.adapters.ViewHolderCreator;
import xcvf.top.readercore.bean.Book;

/**
 *  推荐书籍
 */
public class PriorityBookAdapter extends BaseRecyclerAdapter<Book> {
    @Override
    public ViewHolderCreator createViewHolderCreator() {
        return new ViewHolderCreator() {
            @Override
            public CommonViewHolder createByViewGroupAndType(ViewGroup parent, int viewType, Object... p) {
                return new PriorityBookHolder(parent.getContext(),parent);
            }
        };
    }
}
