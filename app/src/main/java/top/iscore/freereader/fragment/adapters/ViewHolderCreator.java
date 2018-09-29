package top.iscore.freereader.fragment.adapters;

import android.view.ViewGroup;


/**
 * Created by xiawei on 2017/3/15.
 */


public interface ViewHolderCreator<VH extends CommonViewHolder> {
    VH createByViewGroupAndType(ViewGroup parent, int viewType, Object... p);
}