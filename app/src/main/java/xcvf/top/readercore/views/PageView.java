package xcvf.top.readercore.views;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import xcvf.top.readercore.bean.TextConfig;
import xcvf.top.readercore.interfaces.IPage;


/**
 * 页面显示的内容
 * Created by xiaw on 2018/9/28.
 */
public class PageView extends View {

    TextConfig mTextConfig;
    IPage mPage;


    public void setPage(IPage mPage) {
        this.mPage = mPage;
        postInvalidate();
    }

    public PageView(Context context) {
        super(context);
        init();
    }

    private void init() {
        mTextConfig = TextConfig.getConfig();
    }

    public PageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    @Override
    protected void onDraw(Canvas canvas) {

    }
}
