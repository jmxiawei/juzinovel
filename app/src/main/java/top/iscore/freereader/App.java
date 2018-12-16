package top.iscore.freereader;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

import com.blankj.utilcode.util.CrashUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.SpanUtils;
import com.blankj.utilcode.util.Utils;
import com.facebook.stetho.Stetho;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

import bolts.Task;
import xcvf.top.readercore.bean.TextConfig;
import xcvf.top.readercore.daos.DBManager;

/**
 * Created by xiaw on 2018/6/11.
 */

public class App extends Application {


    public static String baseUrl = "http://iscore.top/";
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
        boolean log = BuildConfig.DEBUG;
        LogUtils.getConfig().setLogSwitch(log);
        Stetho.initializeWithDefaults(this);
        TextConfig.initSpace(this);
        boolean launched = SPUtils.getInstance().getBoolean("launched",false);

        if(!launched){
            int width = ScreenUtils.getScreenWidth();
            TextConfig.getConfig().setTextSize(width/24);
            SPUtils.getInstance().put("launched",true);
        }
        DBManager.init(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        CrashUtils.init(new CrashUtils.OnCrashListener() {
            @Override
            public void onCrash(String crashInfo, Throwable e) {
                LogUtils.e(crashInfo);
            }
        });
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
