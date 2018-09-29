package xcvf.top.readercore.bean;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.TypedValue;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.scwang.smartrefresh.layout.util.DensityUtil;
import com.scwang.smartrefresh.layout.util.DesignUtil;

import top.iscore.freereader.App;
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


    public static final int MAX_TEXT_SIZE = 150;
    public static final int MIN_TEXT_SIZE = 50;
    public int textSize = MIN_TEXT_SIZE;
    public int textColor = Color.BLACK;
    public int backgroundColor = Color.WHITE;
    public Boolean Bold = false;
    public int pageWidth;
    public int pageHeight;
    public int lineSpace = 8;

    private TextConfig() {
    }

    public int getPageWidth() {
        return pageWidth;
    }

    public void setPageWidth(int pageWidth) {
        this.pageWidth = pageWidth;
        this.pageWidth = this.pageWidth - DensityUtil.dp2px(32);
        SPUtils.getInstance(SAVE_NAME).put(C_WIDTH, this.pageWidth);
    }

    public int getPageHeight() {
        return pageHeight;
    }

    public void setPageHeight(int pageHeght) {
        this.pageHeight = pageHeght;
        SPUtils.getInstance(SAVE_NAME).put(C_HEIGHT, pageHeght);
    }

    public void apply(TextView textView) {
        textView.setTextColor(TextConfig.getConfig().getTextColor());
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, TextConfig.getConfig().getTextSize());
    }

    public static TextConfig getConfig() {
        TextConfig textConfig = new TextConfig();
        textConfig.textSize = SPUtils.getInstance(SAVE_NAME).getInt(C_TEXT_SIZE, textConfig.textSize);
        textConfig.lineSpace = textConfig.textSize / 6;
        textConfig.textColor = SPUtils.getInstance(SAVE_NAME).getInt(C_TEXT_COLOR, textConfig.textColor);
        textConfig.backgroundColor = SPUtils.getInstance(SAVE_NAME).getInt(C_BACKGROUND_COLOR, textConfig.backgroundColor);
        textConfig.Bold = SPUtils.getInstance(SAVE_NAME).getBoolean(C_BOLD, textConfig.Bold);
        textConfig.pageWidth = SPUtils.getInstance(SAVE_NAME).getInt(C_WIDTH, 0);
        textConfig.pageHeight = SPUtils.getInstance(SAVE_NAME).getInt(C_HEIGHT, 0);

        return textConfig;
    }


    public int maxLine() {
        return (pageHeight - DensityUtil.dp2px(72)) / (textSize + lineSpace);
    }

    public Paint getSamplePaint() {
        Paint textPaint = new Paint();
        textPaint.setTextSize(textSize);
        textPaint.setFakeBoldText(Bold);
        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setColor(textColor);
        textPaint.setAntiAlias(true);
        return textPaint;
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
        lineSpace = this.textSize / 6;
        this.textSize = textSize;
        SPUtils.getInstance(SAVE_NAME).put(C_TEXT_SIZE, textSize);
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        SPUtils.getInstance(SAVE_NAME).put(C_TEXT_COLOR, textSize);
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        SPUtils.getInstance(SAVE_NAME).put(C_BACKGROUND_COLOR, textSize);
    }

    public Boolean getBold() {
        return Bold;
    }

    public void setBold(Boolean bold) {
        Bold = bold;
    }

    public void applyColor(TextView tv) {
        tv.setTextColor(TextConfig.getConfig().getTextColor());
    }
}
