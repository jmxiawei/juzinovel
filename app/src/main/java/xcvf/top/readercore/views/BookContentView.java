package xcvf.top.readercore.views;

import android.content.Context;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewConfigurationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

import com.blankj.utilcode.util.LogUtils;

import xcvf.top.readercore.bean.TextConfig;
import xcvf.top.readercore.interfaces.Area;
import xcvf.top.readercore.interfaces.IAreaClickListener;
import xcvf.top.readercore.interfaces.IPageScrollListener;

/**
 * 翻页显示内容
 * Created by xiaw on 2018/7/11.
 */
public class BookContentView extends RecyclerView {

    static final int V_CLICK = 6;

    PagerSnapHelper mPagerSnapHelper;
    IPageScrollListener mPageScrollListener;
    IAreaClickListener mAreaClickListener;
    boolean isReachLastPage = false;
    boolean isReachStartPage = false;

    float downX;
    float downY;

    int currentPage = 0;
    int totalPage = 0;
    int touchSlop;

    long downTimestamp = 0L;

    public void setAreaClickListener(IAreaClickListener mAreaClickListener) {
        this.mAreaClickListener = mAreaClickListener;
    }

    public void setPageScrollListener(IPageScrollListener mPageScrollListener) {
        this.mPageScrollListener = mPageScrollListener;
    }


    public void newChapter() {
        currentPage = 0;
        totalPage = 0;
        isReachLastPage = false;
        isReachStartPage = false;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = ev.getX();
                downY = ev.getY();
                downTimestamp = SystemClock.elapsedRealtime();
                break;
            case MotionEvent.ACTION_UP:

                int cdownX = (int) ev.getX();
                int cdownY = (int) ev.getY();
                if (Math.abs(cdownX - downX) > touchSlop) {
                    if (cdownX > downX) {
                        //右滑动
                        if (isReachStartPage) {
                            if (mPageScrollListener != null) {
                                mPageScrollListener.onScroll(currentPage, totalPage, IPageScrollListener.PRE_CHAPTER);
                            }
                            LogUtils.e("右滑动---上一章");
                        }
                    } else {
                        if (isReachLastPage) {
                            if (mPageScrollListener != null) {
                                mPageScrollListener.onScroll(currentPage, totalPage, IPageScrollListener.NEXT_CHAPTER);
                            }
                            LogUtils.e("左滑动---下一章");
                        }
                    }
                } else {
                    if (Math.abs(downX - cdownX) < V_CLICK && Math.abs(downY - cdownY) < V_CLICK
                            && (SystemClock.elapsedRealtime() - downTimestamp < 100)) {
                        //点击
                        LogUtils.e("点击事件");
                        if (mAreaClickListener != null) {
                            mAreaClickListener.clickArea(Area.CENTER);
                        }
                    }
                }
                break;

            default:
                break;
        }
        LogUtils.e("isReachStartPage="+isReachStartPage+",isReachLastPage="+isReachLastPage
                +",currentPage="+currentPage+",totalPage="+totalPage);
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onScrollStateChanged(int state) {


        switch (state) {
            case SCROLL_STATE_IDLE:
                LinearLayoutManager ll = (LinearLayoutManager) getLayoutManager();
                Adapter adapter = getAdapter();
                if (ll != null) {
                    int p = ll.findFirstVisibleItemPosition();
                    int l = adapter == null ? 0 : adapter.getItemCount();
                    currentPage = p;
                    totalPage = l;

                    //是否滑动到最后一页
                    if (totalPage > 0 && currentPage == totalPage - 1) {
                        isReachLastPage = true;
                    } else {
                        isReachLastPage = false;
                    }

                    //是否滑动到第一页
                    if (totalPage > 0 && currentPage == 0) {
                        isReachStartPage = true;
                    } else {
                        isReachStartPage = false;
                    }

                    if (mPageScrollListener != null) {
                        mPageScrollListener.onScroll(p, l, IPageScrollListener.CURRENT_CHAPTER);
                    }
                }
                break;
            case SCROLL_STATE_DRAGGING:
                break;
            default:
                break;
        }


    }

    public BookContentView(Context context) {
        super(context);
        init();
    }

    public BookContentView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BookContentView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    void init() {
        touchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop() * 4;
        mPagerSnapHelper = new PagerSnapHelper();
        mPagerSnapHelper.attachToRecyclerView(this);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        TextConfig.getConfig().setPageWidth(getWidth());
        TextConfig.getConfig().setPageHeight(getHeight());
    }
}
