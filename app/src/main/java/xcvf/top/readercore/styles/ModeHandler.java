package xcvf.top.readercore.styles;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import top.iscore.freereader.R;
import top.iscore.freereader.mode.Colorful;
import top.iscore.freereader.mode.IBackgroundColorView;
import top.iscore.freereader.mode.IDarkBackgroundColorView;
import top.iscore.freereader.mode.IPrimaryBackgroundColorView;
import top.iscore.freereader.mode.IRecyclerView;
import top.iscore.freereader.mode.ITextColorView;
import top.iscore.freereader.mode.setter.TextColorSetter;
import top.iscore.freereader.mode.setter.ViewBackgroundColorSetter;
import top.iscore.freereader.mode.setter.ViewGroupSetter;
import xcvf.top.readercore.bean.Mode;

/**
 * 切换模式
 * Created by xiaw on 2018/10/29.
 */
public class ModeHandler {


    Activity mActivity;

    public ModeHandler(Activity mActivity) {
        this.mActivity = mActivity;
    }

    public void apply(Mode mode) {
        Colorful.Builder builder = new Colorful.Builder(mActivity);
        ViewGroup view = (ViewGroup) mActivity.findViewById(R.id.activity_content);
        childViews(builder, view);
        builder.create().setTheme(mode == Mode.DayMode ? R.style.DayTheme : R.style.NightTheme);
    }


    private void childViews(Colorful.Builder builder, ViewGroup view) {
        int size = view.getChildCount();
        for (int i = 0; i < size; i++) {
            View childView = view.getChildAt(i);

            if (childView instanceof ITextColorView) {
                builder.setter(new TextColorSetter((TextView) childView, R.attr.text_color));
            } else if (childView instanceof IPrimaryBackgroundColorView) {
                builder.setter(new ViewBackgroundColorSetter(childView, R.attr.colorPrimary));
            } else if (childView instanceof IRecyclerView) {
                builder.setter(new ViewGroupSetter((ViewGroup) childView, R.attr.text_color));
            } else if (childView instanceof IBackgroundColorView) {
                builder.setter(new ViewBackgroundColorSetter(childView, R.attr.colorAccent));
            } else if(childView instanceof IDarkBackgroundColorView){
                builder.setter(new ViewBackgroundColorSetter(childView, R.attr.bg_dark));
            }else {
                if (childView instanceof ViewGroup) {
                    childViews(builder, (ViewGroup) childView);
                }
            }
        }
    }
}



