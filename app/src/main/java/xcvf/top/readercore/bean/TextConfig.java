package xcvf.top.readercore.bean;

import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.TypedValue;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.scwang.smartrefresh.layout.util.DensityUtil;

import top.iscore.freereader.R;

/**
 * 字体设置
 * Created by xiaw on 2018/7/11.
 */
public class TextConfig {

    private static final String SAVE_NAME = "TxtConfig";
    private static final String C_TEXT_SIZE = "TEXT_SIZE ";
    private static final String C_TEXT_COLOR = "TEXT_COLOR";
    private static final String C_BACKGROUND_COLOR = "BACKGROUND_COLOR";
    private static final String C_BOLD = "BOLD";
    private static final String C_WIDTH = "C_WIDTH";
    private static final String C_HEIGHT = "C_HEIGHT";
    private static final String C_SHOW_SPECIAL_CHAR = "SHOW_SPECIAL_CHAR";
    private static final String C_PADDING_TOP = "C_PADDING_TOP";


    public static final int MAX_TEXT_SIZE = 150;
    public static final int MIN_TEXT_SIZE = 50;
    public int textSize = MIN_TEXT_SIZE;
    public int textColor = R.color.text_black;
    public int backgroundColor = R.color.reader_styleclor1;
    public Boolean Bold = false;
    public int pageWidth;
    public int pageHeight;
    public int lineSpace = 8;

    public int paddingTop;
    public static int verticalSpaceDB = 0;
    public static int horinzontalSpaceDB = 0;
    private TextConfig() {
    }

    public int getPageWidth() {
        return pageWidth;
    }

    public void setPageWidth(int pageWidth) {
        this.pageWidth = pageWidth;
        this.pageWidth = this.pageWidth - DensityUtil.dp2px(horinzontalSpaceDB);
        SPUtils.getInstance(SAVE_NAME).put(C_WIDTH, this.pageWidth);
    }

    public int getPageHeight() {
        return pageHeight;
    }

    public void setPageHeight(int pageHeight) {
        this.pageHeight = pageHeight;
        SPUtils.getInstance(SAVE_NAME).put(C_HEIGHT, pageHeight);
    }

    public void apply(TextView textView) {
        textView.setTextColor(TextConfig.getConfig().getTextColor());
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, TextConfig.getConfig().getTextSize());
    }

    public static TextConfig getConfig() {
        TextConfig textConfig = new TextConfig();
        textConfig.textSize = SPUtils.getInstance(SAVE_NAME).getInt(C_TEXT_SIZE, textConfig.textSize);
        textConfig.textColor = SPUtils.getInstance(SAVE_NAME).getInt(C_TEXT_COLOR, textConfig.textColor);
        textConfig.backgroundColor = SPUtils.getInstance(SAVE_NAME).getInt(C_BACKGROUND_COLOR, textConfig.backgroundColor);
        textConfig.Bold = SPUtils.getInstance(SAVE_NAME).getBoolean(C_BOLD, textConfig.Bold);
        textConfig.pageWidth = SPUtils.getInstance(SAVE_NAME).getInt(C_WIDTH, 0);
        textConfig.pageHeight = SPUtils.getInstance(SAVE_NAME).getInt(C_HEIGHT, 0);
        textConfig.paddingTop = SPUtils.getInstance(SAVE_NAME).getInt(C_PADDING_TOP, 0);
        return textConfig;
    }


    public int maxLine() {
        int padding = DensityUtil.dp2px(verticalSpaceDB);
        int line = (pageHeight - padding) / (textSize + lineSpace);
        paddingTop = (pageHeight - padding - (line * (textSize + lineSpace))) / 2;
        SPUtils.getInstance(SAVE_NAME).put(C_PADDING_TOP, paddingTop);
        return line;
    }

    public Paint getSamplePaint() {
        TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(textSize);
        textPaint.setFakeBoldText(Bold);
        textPaint.setAntiAlias(true);
        return textPaint;
    }

    public int getLineSpace() {
        return lineSpace;
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {

        if (textSize < MIN_TEXT_SIZE) {
            textSize = MIN_TEXT_SIZE;
        } else if (textSize > MAX_TEXT_SIZE) {
            textSize = MAX_TEXT_SIZE;
        }
        this.textSize = textSize;
        SPUtils.getInstance(SAVE_NAME).put(C_TEXT_SIZE, textSize);
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        SPUtils.getInstance(SAVE_NAME).put(C_TEXT_COLOR, this.textColor);
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        SPUtils.getInstance(SAVE_NAME).put(C_BACKGROUND_COLOR, this.backgroundColor);
    }

    public Boolean getBold() {
        return Bold;
    }

    public void setBold(Boolean bold) {
        Bold = bold;
    }

    public void applyColor(TextView tv) {
        tv.setTextColor(tv.getResources().getColor(textColor));
    }
}
