package top.iscore.freereader.fragment.adapters;

import android.content.Context;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class CommonViewHolder<T> extends RecyclerView.ViewHolder {


    protected Object[] mExtras;


    protected T data;

    public CommonViewHolder(Context context, ViewGroup root, int layoutRes) {
        super(LayoutInflater.from(context).inflate(layoutRes, root, false));
        //setItemViewClickListener();
    }


    public void setExtras(Object... extras) {
        this.mExtras = extras;
    }

    /**
     *
     */
    protected void setItemViewClickListener() {
        itemView.setOnClickListener(itemViewClickListener);
    }

    /**
     * 点击事件
     */
    protected View.OnClickListener itemViewClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Parcelable p = getDataForBroadcast();
            if (p != null) {
                //Sender.send(getContext(), p);
            }
        }
    };


    public Parcelable getDataForBroadcast() {
        return (Parcelable) null;
    }


    public CommonViewHolder(View layoutView) {
        super(layoutView);
    }


    public Context getContext() {
        return itemView.getContext();
    }

    public abstract void bindData(T t, int position);

    public void setData(T t, int position) {
        data = t;
        bindData(t, position);
    }


    protected void noBackground() {
        itemView.setBackgroundResource(0);
    }

    public <T extends View> T getView(int id) {
        return (T) itemView.findViewById(id);
    }


}