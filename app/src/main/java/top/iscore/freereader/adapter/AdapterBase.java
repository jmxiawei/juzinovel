package top.iscore.freereader.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public abstract class AdapterBase<T> extends BaseAdapter {

    private final ArrayList<T> mList = new ArrayList<T>();




    public ArrayList<T> getList() {
        return mList;
    }

    public void appendToList(ArrayList<T> list) {
        if (list == null) {
            return;
        }
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public void appendToList(T t) {
        if (t == null) {
            return;
        }
        mList.add(t);
        notifyDataSetChanged();
    }

    public void clearAppendToList(List<T> list) {
        if (list == null) {
            return;
        }
        mList.clear();
        mList.addAll(list);

        notifyDataSetChanged();
    }

    public void appendToTopList(List<T> list) {
        if (list == null) {
            return;
        }
        mList.addAll(0, list);
        notifyDataSetChanged();
    }

    public void clear() {
        mList.clear();
        notifyDataSetChanged();
    }



    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        if (position > mList.size() - 1) {
            return null;
        }
        return mList.get(position);
    }

    public void clearItem(int position) {
        mList.remove(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        return getExView(position, convertView, parent);
    }

    protected abstract View getExView(int position, View convertView,
                                      ViewGroup parent);



}
