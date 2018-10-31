package top.iscore.freereader.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import top.iscore.freereader.R;
import top.iscore.freereader.fragment.adapters.CommonViewHolder;
import top.iscore.freereader.mode.Colorful;
import top.iscore.freereader.mode.setter.ViewBackgroundColorSetter;
import top.iscore.freereader.mode.setter.ViewGroupSetter;
import xcvf.top.readercore.bean.Category;
import xcvf.top.readercore.bean.Mode;
import xcvf.top.readercore.styles.ModeProvider;

/**
 * 发现页面
 */
public class FinderFragment extends BaseListFragment<Category> {


    private ArrayList<Category> categories = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_finder,container,false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initRecyclerView(view);
        initCateogry();
    }

    private void initCateogry() {

        categories.clear();;
        categories.add(new Category(0,"分类",R.mipmap.ic_category));
        getAdapter().setDataList(categories);
    }


    @Override
    public void onResume() {
        super.onResume();
        changeMode();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            changeMode();
        }
    }

    private void changeMode() {
        new Colorful.Builder(getActivity())
                .setter(new ViewBackgroundColorSetter(getView(),R.attr.colorPrimary))
                .setter(new ViewGroupSetter(mRecycleView,R.attr.colorPrimary)
                            .childViewTextColor(R.id.tv,R.attr.text_color)).
                create().setTheme(ModeProvider.getCurrentModeTheme());
    }

    @Override
    public CommonViewHolder getHolderCreateByViewGroupAndType(ViewGroup parent, int viewType, Object... p) {
        return new CommonViewHolder<Category>(parent.getContext(),parent, R.layout.item_finder_cateogry) {
            @Override
            public void bindData(Category category, int position) {
                TextView tv = itemView.findViewById(R.id.tv);
                ImageView img_cate = itemView.findViewById(R.id.img_cate);
                tv.setText(category.getName());
                img_cate.setImageResource(category.getResid());
            }
        };
    }
}
