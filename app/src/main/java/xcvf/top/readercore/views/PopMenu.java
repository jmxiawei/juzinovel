package xcvf.top.readercore.views;

import android.content.Context;
import android.database.DataSetObserver;
import android.support.annotation.NonNull;
import android.support.v7.widget.ListPopupWindow;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ListAdapter;

import java.util.List;

import top.iscore.freereader.adapter.AdapterBase;
import xcvf.top.readercore.bean.Category;

/**
 * 首页menu弹出
 * Created by xiaw on 2018/11/1.
 */
public class PopMenu extends ListPopupWindow {






    public PopMenu(@NonNull Context context) {
        super(context);
        setWidth(WRAP_CONTENT);
        setHeight(WRAP_CONTENT);
    }



    public static class MenuAdapter extends AdapterBase<Category>{

        @Override
        protected View getExView(int position, View convertView, ViewGroup parent) {
            return null;
        }
    }
}
