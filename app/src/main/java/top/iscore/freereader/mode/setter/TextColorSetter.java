package top.iscore.freereader.mode.setter;

import android.content.res.Resources.Theme;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;

/**
 * TextView 文本颜色Setter
 *
 * @author mrsimple
 */
public class TextColorSetter extends ViewSetter {

    public TextColorSetter(TextView textView, int resId) {
        super(textView, resId);
    }

    public TextColorSetter(int viewId, int resId) {
        super(viewId, resId);
    }

    @Override
    public void setValue(Theme newTheme, int themeId) {
        if (mView == null) {
            return;
        }
        LogUtils.e("设置字体颜色:" + mView.getId());
        ((TextView) mView).setTextColor(getColor(newTheme));
    }

}
