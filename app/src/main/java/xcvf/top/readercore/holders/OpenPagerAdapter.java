package xcvf.top.readercore.holders;

import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by homgwu on 2018/4/2 14:29.
 */
public abstract class OpenPagerAdapter<T> extends PagerAdapter {
    private static final String TAG = "FragmentStatePagerAdapt";
    private static final boolean DEBUG = false;

    private ArrayList<ItemInfo<T>> mItemInfos = new ArrayList<>();
    private View mCurrentPrimaryItem = null;
    private boolean mNeedProcessCache = false;

    /**
     * Return the Fragment associated with a specified position.
     */
    public abstract View getItem(int position);

    protected View getCachedItem(int position) {
        return mItemInfos.size() > position ? mItemInfos.get(position).view : null;
    }

    @Override
    public void startUpdate(ViewGroup container) {
        if (container.getId() == View.NO_ID) {
            throw new IllegalStateException("ViewPager with adapter " + this
                    + " requires a view id");
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // If we already have this item instantiated, there is nothing
        // to do.  This can happen when we are restoring the entire pager
        // from its saved state, where the fragment manager has already
        // taken care of restoring the fragments we previously had instantiated.
        if (mItemInfos.size() > position) {
            ItemInfo ii = mItemInfos.get(position);
            if (ii != null) {
                //判断位置是否相等，如果不相等说明新数据有增加或删除(导致了ViewPager那边有空位)，
                // 而这时notifyDataSetChanged方法还没有完成，ViewPager会先调用instantiateItem来获取新的页面
                //所以为了不取错页面，我们需要对缓存进行检查和调整位置：checkProcessCacheChanged
                if (ii.position == position) {
                    return ii;
                } else {
                    checkProcessCacheChanged();
                }
            }
        }

        View view = getItem(position);
        while (mItemInfos.size() <= position) {
            mItemInfos.add(null);
        }
        ItemInfo<T> iiNew = new ItemInfo<>(view, getItemData(position), position);
        mItemInfos.set(position, iiNew);
        container.addView(view);
        return iiNew;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ItemInfo ii = (ItemInfo) object;
        if (DEBUG) Log.v(TAG, "Removing item #" + position + ": f=" + object
                + " v=" + (ii.view));
        mItemInfos.set(position, null);
        container.removeView(ii.view);
    }

    @Override
    @SuppressWarnings("ReferenceEquality")
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        ItemInfo ii = (ItemInfo) object;
        View view = ii.view;
        if (view != mCurrentPrimaryItem) {
            mCurrentPrimaryItem = view;
        }
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        View view1 = ((ItemInfo) object).view;
        return view1 == view;
    }

    @Override
    public int getItemPosition(Object object) {
        mNeedProcessCache = true;
        ItemInfo<T> itemInfo = (ItemInfo) object;
        int oldPosition = mItemInfos.indexOf(itemInfo);
        if (oldPosition >= 0) {
            T oldData = itemInfo.data;
            T newData = getItemData(oldPosition);
            if (dataEquals(oldData, newData)) {
                return POSITION_UNCHANGED;
            } else {
                ItemInfo<T> oldItemInfo = mItemInfos.get(oldPosition);
                int oldDataNewPosition = getDataPosition(oldData);
                if (oldDataNewPosition < 0) {
                    oldDataNewPosition = POSITION_NONE;
                }
                //把新的位置赋值到缓存的itemInfo中，以便调整时使用
                if (oldItemInfo != null) {
                    oldItemInfo.position = oldDataNewPosition;
                }
                return oldDataNewPosition;
            }
        }
        return POSITION_UNCHANGED;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        //通知ViewPager更新完成后对缓存的ItemInfo List进行调整
        checkProcessCacheChanged();
    }

    private void checkProcessCacheChanged() {
        //只有调用过getItemPosition(也就是有notifyDataSetChanged)才进行缓存的调整
        if (!mNeedProcessCache) return;
        mNeedProcessCache = false;
        ArrayList<ItemInfo<T>> pendingItemInfos = new ArrayList<>(mItemInfos.size());
        //先存入空数据
        for (int i = 0; i < mItemInfos.size(); i++) {
            pendingItemInfos.add(null);
        }
        //根据缓存的itemInfo中的新position把itemInfo入正确的位置
        for (ItemInfo<T> itemInfo : mItemInfos) {
            if (itemInfo != null) {
                if (itemInfo.position >= 0) {
                    while (pendingItemInfos.size() <= itemInfo.position) {
                        pendingItemInfos.add(null);
                    }
                    pendingItemInfos.set(itemInfo.position, itemInfo);
                }
            }
        }
        mItemInfos = pendingItemInfos;
    }

    protected View getCurrentPrimaryItem() {
        return mCurrentPrimaryItem;
    }

    protected View getFragmentByPosition(int position) {
        if (position < 0 || position >= mItemInfos.size()) return null;
        return mItemInfos.get(position).view;
    }

    protected abstract T getItemData(int position);

    protected abstract boolean dataEquals(T oldData, T newData);

    protected abstract int getDataPosition(T data);

    protected static class ItemInfo<D> {
        D data;
        int position;
        View view;

        public ItemInfo(View view, D data, int position) {
            this.data = data;
            this.view = view;
            this.position = position;
        }
    }
}