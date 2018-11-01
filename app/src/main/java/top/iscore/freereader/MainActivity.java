package top.iscore.freereader;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import top.iscore.freereader.adapter.TabFragmentAdapter;
import top.iscore.freereader.fragment.BookshelfFragment;
import top.iscore.freereader.fragment.FinderFragment;
import top.iscore.freereader.mode.Colorful;
import top.iscore.freereader.mode.setter.TabIndicatorSetter;
import top.iscore.freereader.mode.setter.ViewBackgroundColorSetter;
import xcvf.top.readercore.bean.User;
import xcvf.top.readercore.styles.ModeProvider;


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
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.img_more)
    ImageView imgMore;
    @BindView(R.id.ll_toolbar)
    LinearLayout llToolbar;
    @BindView(R.id.activity_content)
    LinearLayout activityContent;
    @BindView(R.id.img_search)
    ImageView imgSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        User user = new User();
        user.setUid("4");
        user.setAccount("admin");
        user.save();
        // startActivity(new Intent(this,ReaderActivity.class));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(permissions, 1);
            }
        }

        fragmentList.add(new BookshelfFragment());
        fragmentList.add(new FinderFragment());
        titles.add("书架");
        titles.add("发现");
        TabFragmentAdapter adapter = new TabFragmentAdapter(fragmentList, titles, getSupportFragmentManager());
        viewpager.setAdapter(adapter);
        tablayout.setupWithViewPager(viewpager);
        TabUtils.setIndicator(this, tablayout, 64, 64, 0, 0);

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
                break;
            case R.id.img_more:
                break;
            default:
                break;
        }
    }
}
