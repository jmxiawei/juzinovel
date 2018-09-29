package top.iscore.freereader.fragment.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by xiawei on 2017/3/16.
 */

public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<CommonViewHolder> {

    protected List<T> dataList = new ArrayList<>();

    private OnReachBottomListener reachBottomListener;

    private int loadState = ILoadMoreView.STATE_COMPLETE;


    private OnRecyclerViewItemClickListener<T> onRecyclerViewItemClickListener;

    protected ViewHolderCreator viewHolderCreator;

    public abstract ViewHolderCreator createViewHolderCreator();

    public OnReachBottomListener getReachBottomListener() {
        return reachBottomListener;
    }

    public void setReachBottomListener(OnReachBottomListener reachBottomListener) {
        this.reachBottomListener = reachBottomListener;
    }

    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener<T> onRecyclerViewItemClickListener) {
        this.onRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
    }


    @Override
    public CommonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewHolderCreator == null) {
            //放在构造函数里，createViewHolderCreator 里的数据会在之类构造方法之前执行
            viewHolderCreator = createViewHolderCreator();
        }
        return viewHolderCreator.createByViewGroupAndType(parent, viewType);
    }

    public List<T> getDataList() {
        return dataList;
    }

    public int getLoadState() {
        return loadState;
    }

    public void setLoadState(int loadState) {
        this.loadState = loadState;
    }

    @Override
    public void onBindViewHolder(final CommonViewHolder holder, final int position) {
        if (position >= 0 && position < getItemCount()) {
            holder.setExtras(this);
            holder.setData(getItem(position), position);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onRecyclerViewItemClickListener != null) {
                        onRecyclerViewItemClickListener.onRecyclerViewItemClick(holder, position, getItem(position));
                    }
                }
            });

            if (loadState == ILoadMoreView.STATE_COMPLETE
                    && position == getItemCount() - 1) {
                //滑动到最底部
                if (reachBottomListener != null) {
                    loadState = ILoadMoreView.STATE_LOADING;
                    reachBottomListener.onReachBottom();
                }
            }

        }
    }

    public void appendDataList(List<T> datas) {
        if (null != datas) {
            dataList.addAll(datas);
        }
        notifyDataSetChanged();
        setLoadState(ILoadMoreView.STATE_COMPLETE);
    }

    public void setDataList(List<T> datas) {
        dataList.clear();
        if (null != datas) {
            dataList.addAll(datas);
        }
        notifyDataSetChanged();
        setLoadState(ILoadMoreView.STATE_COMPLETE);
    }


    public T getItem(int position) {
        if (position >= 0 && position < dataList.size()) {
            return dataList.get(position);
        } else {
            return null;
        }
    }

    public void addDataList(List<T> datas) {
        dataList.addAll(datas);
        notifyDataSetChanged();
        setLoadState(ILoadMoreView.STATE_COMPLETE);
    }


    @Override
    public int getItemCount() {
        if (dataList == null) {
            return 0;
        }
        return dataList.size();

    }
}
