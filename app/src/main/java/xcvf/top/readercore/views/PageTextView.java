package xcvf.top.readercore.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import java.util.List;

import xcvf.top.readercore.bean.Line;
import xcvf.top.readercore.bean.Page;
import xcvf.top.readercore.bean.TextConfig;
import xcvf.top.readercore.bean.TxtChar;

/**
 * 显示一页数据
 * Created by xiaw on 2018/10/26.
 */
public class PageTextView extends AppCompatTextView {

    Page page;
    int mWidth;
    int mHeight;
    int mLeft;
    private static final boolean DEBUG = false;


    public PageTextView(Context context) {
        super(context);
    }

    public PageTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PageTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    public PageTextView setPage(Page page) {
        this.page = page;
        postInvalidate();
        return this;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mWidth = getWidth();
        mHeight = getHeight();
        mLeft = getLeft();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (DEBUG) {
            canvas.drawColor(Color.RED);
        }
        if (page == null) {
            return;
        }
        List<Line> lines = page.getLines();
        if (lines == null || lines.size() == 0) {
            return;
        }
        TextConfig config = TextConfig.getConfig();
        int line = page.getLines().size();
        Paint mPaint = config.getSamplePaint();
        mPaint.setColor(getResources().getColor(config.getTextColor()));
        int lineHeight = config.getTextSize();
        int sx = 0, sy = lineHeight + config.paddingTop;
        for (int i = 0; i < line; i++) {
            Line line1 = (Line) page.getLines().get(i);
            String lineStr = line1.toString();
            canvas.drawText(lineStr, sx, sy, mPaint);
//            int charLength = line1.getChars().size();
//            for (int j = 0; j < charLength; j++) {
//                //一个字符
//                TxtChar txtChar = (TxtChar) line1.getChars().get(j);
//                String str = String.valueOf(txtChar.getData());
//                canvas.drawText(str, sx, sy, mPaint);
//                sx += txtChar.getWidth();
//            }
            if (DEBUG) {
                canvas.drawLine(0, sy, 1000, sy, mPaint);
                canvas.drawText(String.valueOf(i), 0, sy, mPaint);
            }
            sx = 0;
            sy += (lineHeight + config.getLineSpace());
        }

    }
}
