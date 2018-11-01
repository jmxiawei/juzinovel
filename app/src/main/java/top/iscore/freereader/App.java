package top.iscore.freereader;

import android.content.Context;

import com.blankj.utilcode.util.Utils;
import com.facebook.stetho.Stetho;
import com.orm.SugarApp;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

import xcvf.top.readercore.bean.TextConfig;

/**
 * Created by xiaw on 2018/6/11.
 */

public class App extends SugarApp {


    public static String baseUrl = "http://iscore.top/";
    public static String oss_domain = "http://img.xcvf.top/book";

    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.bg_dark_color, android.R.color.white);//全局设置主题颜色
                return new ClassicsHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setDrawableSize(20);
            }
        });
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
        Stetho.initializeWithDefaults(this);
        TextConfig.initSpace(this);

    }


//    /**
//     * 设置各个视图与颜色属性的关联
//     */
//    private void setupColorful() {
//        ViewGroupSetter listViewSetter = new ViewGroupSetter(mNewsListView);
//        // 绑定ListView的Item View中的news_title视图，在换肤时修改它的text_color属性
//        listViewSetter.childViewTextColor(R.id.news_title, R.attr.text_color);
//
//        // 构建Colorful对象来绑定View与属性的对象关系
//        mColorful = new Colorful.Builder(this)
//                .backgroundDrawable(R.id.root_view, R.attr.root_view_bg)
//                // 设置view的背景图片
//                .backgroundColor(R.id.change_btn, R.attr.btn_bg)
//                // 设置背景色
//                .textColor(R.id.textview, R.attr.text_color)
//                .setter(listViewSetter) // 手动设置setter
//                .create(); // 设置文本颜色
//    }
//
//    /**
//     * 切换主题
//     */
//    private void changeThemeWithColorful() {
//        if (!isNight) {
//            mColorful.setTheme(R.style.NightTheme);
//        } else {
//            mColorful.setTheme(R.style.DayTheme);
//        }
//        isNight = !isNight;
//    }
}
