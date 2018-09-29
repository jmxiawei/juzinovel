package top.iscore.freereader.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * tablayout 和viewpager 配置实用的adapter
 *
 * @author xiaw
 * @date 2016/6/17 0017
 */
public class TabFragmentAdapter extends FragmentPagerAdapter {
    private final ArrayList<String> titles = new ArrayList<>();

    private List<Fragment> fragments = new ArrayList<>();

    public TabFragmentAdapter(List<? extends Fragment> fragments, ArrayList<String> titles, FragmentManager fm) {
        super(fm);
        this.fragments.addAll(fragments);
        if(titles!=null){
            this.titles.addAll(titles);
        }

    }

    public void setTitles(ArrayList<String> titles) {
        this.titles.clear();
        this.titles.addAll(titles);
        notifyDataSetChanged();
    }

    public void setData(List<Fragment> fragments, ArrayList<String> titles) {
        this.fragments.clear();
        this.titles.clear();
        this.fragments.addAll(fragments);
        if(titles!=null){
            this.titles.addAll(titles);
        }

        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (titles != null && position < titles.size()) {
            return titles.get(position);
        }
        return null;

    }
}
