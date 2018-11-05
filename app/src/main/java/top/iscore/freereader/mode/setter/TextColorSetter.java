package top.iscore.freereader.mode.setter;

import android.content.res.Resources.Theme;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;

import top.iscore.freereader.R;

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
        ((TextView) mView).setTextColor(getColor(newTheme));
    }

}
