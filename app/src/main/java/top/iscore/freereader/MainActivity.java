package top.iscore.freereader;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vector.update_app.UpdateAppManager;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import top.iscore.freereader.adapter.TabFragmentAdapter;
import top.iscore.freereader.fragment.BookshelfFragment;
import top.iscore.freereader.fragment.FinderFragment;
import top.iscore.freereader.mode.Colorful;
import top.iscore.freereader.mode.SwitchModeListener;
import top.iscore.freereader.mode.setter.TabIndicatorSetter;
import top.iscore.freereader.mode.setter.ViewBackgroundColorSetter;
import top.iscore.freereader.update.UpdateAppHttpUtil;
import xcvf.top.readercore.bean.Mode;
import xcvf.top.readercore.bean.User;
import xcvf.top.readercore.styles.ModeProvider;
import xcvf.top.readercore.views.PopMenu;


/**
 * 首页
 */
public class MainActivity extends AppCompatActivity {


    String[] permissions = new String[]{
            Manifest.permission.INTERNET, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    @BindView(R.id.tablayout)
    TabLayout tablayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;


    ArrayList<Fragment> fragmentList = new ArrayList<>();
    ArrayList<String> titles = new ArrayList<>();
    @BindView(R.id.img_more)
    ImageView imgMore;
    @BindView(R.id.ll_toolbar)
    LinearLayout llToolbar;
    @BindView(R.id.activity_content)
    LinearLayout activityContent;
    @BindView(R.id.img_search)
    ImageView imgSearch;
    SwitchModeHandler switchModeListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        User user = new User();
        user.setUid("4");
        user.setAccount("admin");
        user.setNickname("用户007");
        user.save();
        // startActivity(new Intent(this,ReaderActivity.class));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(permissions, 1);
            }
        }
        switchModeListener = new SwitchModeHandler(mSwitchModeListener, this);
        switchModeListener.onCreate();
        fragmentList.add(new BookshelfFragment());
        fragmentList.add(new FinderFragment());
        titles.add("书架");
        titles.add("发现");
        TabFragmentAdapter adapter = new TabFragmentAdapter(fragmentList, titles, getSupportFragmentManager());
        viewpager.setAdapter(adapter);
        tablayout.setupWithViewPager(viewpager);
        TabUtils.setIndicator(this, tablayout, 64, 64, 0, 0);
        checkUpdate();
    }

    private void checkUpdate() {
        HashMap<String, String> params = new HashMap<>();
        params.put("vcode", "1");
        new UpdateAppManager
                .Builder()
                //当前Activity
                .setActivity(this)
                //更新地址
                .setUpdateUrl("http://iscore.top/reader/public/v1/")
                .setParams(params)
                .setThemeColor(getResources().getColor(R.color.colorAccent))
                .setTopPic(R.mipmap.top_8)
                //实现httpManager接口的对象
                .setHttpManager(new UpdateAppHttpUtil())
                .build()
                .update();
    }


    @Override
    protected void onResume() {
        super.onResume();
        updateMode();
    }

    private void updateMode() {
        new Colorful.Builder(this)
                .setter(new ViewBackgroundColorSetter(llToolbar, R.attr.colorAccent))
                .setter(new ViewBackgroundColorSetter(tablayout, R.attr.colorAccent))
                .setter(new TabIndicatorSetter(tablayout, R.attr.colorPrimary))
                .setter(new ViewBackgroundColorSetter(R.id.activity_content, R.attr.colorPrimary))
                .create().setTheme(ModeProvider.getCurrentModeTheme());
    }

    @OnClick({R.id.img_search, R.id.img_more})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_search:
                Intent intent = new Intent(this, SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.img_more:
                PopMenu popMenu = new PopMenu(this);
                popMenu.setSwitchModeListener(mSwitchModeListener);
                popMenu.showAtLocation(llToolbar, Gravity.TOP | Gravity.RIGHT, 120, 110);
                break;
            default:
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        switchModeListener.onDestroy();
    }

    /**
     * 切换日间和夜间模式
     */
    private SwitchModeListener mSwitchModeListener = new SwitchModeListener() {
        @Override
        public void switchMode(Mode mode) {
            updateMode();
        }
    };
}
