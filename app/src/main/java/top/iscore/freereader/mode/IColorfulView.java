package top.iscore.freereader.mode;

import android.content.res.Resources;


//可以切换日间和黑夜模式
public interface IColorfulView {

    void applyBackgroundColor(Resources.Theme theme,int resId);

    void applyTextColor(Resources.Theme theme,int resId);

    void appyBackgroundDrawable(Resources.Theme theme,int resId);

}
