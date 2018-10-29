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

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import top.iscore.freereader.adapter.TabFragmentAdapter;
import top.iscore.freereader.fragment.BookshelfFragment;
import xcvf.top.readercore.bean.User;


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
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(permissions, 1);
                }
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

    }

}
