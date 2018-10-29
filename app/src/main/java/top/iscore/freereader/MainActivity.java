package top.iscore.freereader;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import top.iscore.freereader.adapter.TabFragmentAdapter;
import top.iscore.freereader.fragment.BookshelfFragment;
import xcvf.top.readercore.bean.Mode;
import xcvf.top.readercore.bean.User;
import xcvf.top.readercore.impl.FullScreenHandler;
import xcvf.top.readercore.styles.ModeHandler;
import xcvf.top.readercore.styles.ModeProvider;


/**
 * 首页
 */
public class MainActivity extends AppCompatActivity {


    String[] permissions = new String[]{
            Manifest.permission.INTERNET, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tablayout)
    TabLayout tablayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;


    ArrayList<Fragment> fragmentList = new ArrayList<>();
    ArrayList<String> titles = new ArrayList<>();
    ModeHandler modeHandler;
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
        toolbar.inflateMenu(R.menu.main_more);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(permissions, 1);
            }
        }
        toolbar.setTitle(getString(R.string.app_name));
        fragmentList.add(new BookshelfFragment());
        fragmentList.add(new Fragment());
        titles.add("书架");
        titles.add("发现");
        TabFragmentAdapter adapter = new TabFragmentAdapter(fragmentList, titles, getSupportFragmentManager());
        viewpager.setAdapter(adapter);
        tablayout.setupWithViewPager(viewpager);
        TabUtils.setIndicator(this, tablayout, 64, 64, 0, 0);
        modeHandler = new ModeHandler(this,(ViewGroup) findViewById(R.id.activity_content));
        Mode mode = ModeProvider.getCurrentMode();
        modeHandler.apply(mode);
    }


    @Override
    protected void onResume() {
        super.onResume();
        Mode mode = ModeProvider.getCurrentMode();
        modeHandler.apply(mode);
    }
}
