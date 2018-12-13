
package xcvf.top.readercore.views;

import android.content.Context;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ScreenUtils;

import xcvf.top.readercore.bean.TextConfig;
import xcvf.top.readercore.impl.ChapterPagerSnapHelper;
import xcvf.top.readercore.impl.SimplePageChangeListener;
import xcvf.top.readercore.interfaces.Area;
import xcvf.top.readercore.interfaces.IAreaClickListener;
import xcvf.top.readercore.interfaces.IPageScrollListener;
import xcvf.top.readercore.interfaces.OnPageChangedListener;
import xcvf.top.readercore.transformer.AccordionTransformer;
import xcvf.top.readercore.transformer.CubeInTransformer;
import xcvf.top.readercore.transformer.ParallaxTransformer;
import xcvf.top.readercore.transformer.StackTransformer;

/**
 * 翻页显示内容
 * Created by xiaw on 2018/7/11.
 */
public class BookContentView extends ViewPager {

    /**
     * 判定点击事件的
     */
    static final int V_CLICK = 6;
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
    int mLastPosition = 0;

    int screenWidth;

    /**
     * 滑动页面
     */
    private OnPageChangeListener mPageChangedListener = new OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {
            LogUtils.e("---page-_current-i" + i);
//            if (i == 0) {
//                if (mPageScrollListener != null) {
//                    mPageScrollListener.onScroll(currentPage, totalPage, IPageScrollListener.NEXT_CHAPTER);
//                }
//                if (mPageScrollListener != null) {
//                    mPageScrollListener.onScroll(currentPage, totalPage, IPageScrollListener.PRE_CHAPTER);
//                }
//            } else if (i > mLastPosition) {
//                if (mPageScrollListener != null) {
//                    mPageScrollListener.onScroll(currentPage, totalPage, IPageScrollListener.PRE_CHAPTER);
//                }
//            } else {
//                if (mPageScrollListener != null) {
//                    mPageScrollListener.onScroll(currentPage, totalPage, IPageScrollListener.NEXT_CHAPTER);
//                }
//            }
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };

     void setAreaClickListener(IAreaClickListener mAreaClickListener) {
        this.mAreaClickListener = mAreaClickListener;
    }

     void setPageScrollListener(IPageScrollListener mPageScrollListener) {
        this.mPageScrollListener = mPageScrollListener;

    }


    void scrollNextPage() {
        int page = getCurrentItem();
        int count = getAdapter().getCount();
        if (page == count - 1) {

        } else {
            setCurrentItem(page + 1);
        }

        if (mPageScrollListener != null) {
            mPageScrollListener.onScroll(page, count, IPageScrollListener.NEXT_CHAPTER);
        }

    }


    void scrollPrePage() {
        int page = getCurrentItem();
        int count = getAdapter().getCount();
        if (page == 0) {
            // ToastUtils.showShort("已是第一页");
        } else {
            setCurrentItem(page - 1, true);
        }

        if (mPageScrollListener != null) {
            mPageScrollListener.onScroll(page, count, IPageScrollListener.PRE_CHAPTER);
        }

    }


    public void newChapter() {
        currentPage = 0;
        totalPage = 0;
        isReachLastPage = false;
        isReachStartPage = false;
    }

     int getCurrentPage() {
        return getCurrentItem();
    }

     BookContentView setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
        return this;
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
                        if (mPageScrollListener != null) {
                            mPageScrollListener.onScroll(currentPage, totalPage, IPageScrollListener.PRE_CHAPTER);
                        }
                    } else {
                        if (mPageScrollListener != null) {
                            mPageScrollListener.onScroll(currentPage, totalPage, IPageScrollListener.NEXT_CHAPTER);
                        }
                    }
                    LogUtils.e("move move move");
                } else {
                    if (Math.abs(downX - cdownX) < V_CLICK && Math.abs(downY - cdownY) < V_CLICK
                            && (SystemClock.elapsedRealtime() - downTimestamp < 100)) {

                        if (cdownX < screenWidth / 3) {
                            //点击
                            if (mAreaClickListener != null) {
                                mAreaClickListener.clickArea(Area.LEFT);
                            }
                        } else if (cdownX > screenWidth * 2 / 3) {
                            if (mAreaClickListener != null) {
                                mAreaClickListener.clickArea(Area.RIGHT);
                            }
                        } else {
                            //点击
                            if (mAreaClickListener != null) {
                                mAreaClickListener.clickArea(Area.CENTER);
                            }
                        }


                    }
                }
                break;

            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }


    public BookContentView(Context context) {
        super(context);
        init();
    }

    public BookContentView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    void init() {
        touchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop() * 4;
        screenWidth = ScreenUtils.getScreenWidth();
        setOffscreenPageLimit(2);
        //AccordionTransformer StackTransformer ParallaxTransformer
        setPageTransformer(true, new StackTransformer());
        removeOnPageChangeListener(mPageChangedListener);
        addOnPageChangeListener(mPageChangedListener);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        TextConfig.getConfig().setPageWidth(getWidth());
        TextConfig.getConfig().setPageHeight(getHeight());
    }
}
