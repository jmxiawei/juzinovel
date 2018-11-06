package xcvf.top.readercore.views;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import top.iscore.freereader.R;
import top.iscore.freereader.fragment.adapters.BaseRecyclerAdapter;
import top.iscore.freereader.fragment.adapters.CommonViewHolder;
import top.iscore.freereader.fragment.adapters.OnRecyclerViewItemClickListener;
import top.iscore.freereader.fragment.adapters.ViewHolderCreator;
import xcvf.top.readercore.bean.Category;
import xcvf.top.readercore.bean.Chapter;
import xcvf.top.readercore.bean.Config;
import xcvf.top.readercore.bean.Mode;
import xcvf.top.readercore.bean.TextConfig;
import xcvf.top.readercore.styles.ModeConfig;
import xcvf.top.readercore.styles.ModeProvider;

/**
 * 下载选择下载多少章节
 * Created by xiaw on 2018/11/1.
 */
public class PopDownload extends PopupWindow {


    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.ll_content)
    LinearLayout llContent;
    DAdapter mDAdapter;
    Chapter chapter;
    OnDownloadCmdListener onDownloadCmdListener;
    int mLeftCount = 0;
    public PopDownload setOnDownloadCmdListener(OnDownloadCmdListener onDownloadCmdListener) {
        this.onDownloadCmdListener = onDownloadCmdListener;
        return this;
    }

    public PopDownload(Context context, Chapter chapter,List<Chapter> chapters) {
        super(context);
        this.chapter = chapter;
        int size = chapters == null?0:chapters.size();
        if(size>0){
            int index = chapters.indexOf(chapter);
            mLeftCount = size - index - 1;
        }

        View view = LayoutInflater.from(context).inflate(R.layout.layout_download_select_amount, null);
        setContentView(view);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        setBackgroundDrawable(new ColorDrawable(0x66000000));
        setOutsideTouchable(true);
        setTouchable(true);
        ButterKnife.bind(this, view);
        llContent.setBackgroundResource(R.drawable.bg_menu_nightmode);
        initStyle();
        getContentView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void initStyle() {
        mDAdapter = new DAdapter();
        recycler.setLayoutManager(new LinearLayoutManager(getContentView().getContext(), LinearLayoutManager.VERTICAL, false));
        recycler.setAdapter(mDAdapter);
        mDAdapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener<Category>() {
            @Override
            public void onRecyclerViewItemClick(CommonViewHolder holder, int position, Category item) {
                if (onDownloadCmdListener != null) {
                    onDownloadCmdListener.onComd(chapter, item);
                }
                dismiss();
            }
        });
        initOptions();
    }

    /**
     * 下载 监听
     */
    public interface OnDownloadCmdListener {
        void onComd(Chapter chapter, Category category);
    }


    private void initOptions() {
        List<Category> categories = new ArrayList<>();
        int left = mLeftCount;
                //Chapter.getLeftChapter(this.chapter.extern_bookid, this.chapter.chapterid + "");
        int start = 50;
        while (checkAmount(start, left, categories)) {
            start *= 2;
        }
        mDAdapter.setDataList(categories);

    }

    /**
     * @param count
     * @param categories
     * @return can continue
     */
    private boolean checkAmount(int count, int left, List<Category> categories) {

        if (count >= left) {
            Category category = addAmount(left);
            categories.add(category);
            return false;
        } else {
            Category category = addAmount(count);
            categories.add(category);
            return true;
        }
    }

    private Category addAmount(int count) {
        Category category = new Category();
        category.setName("后" + count + "章");
        category.setIntValue(count);
        return category;
    }


    /**
     * 选择数量
     */
    private static final class DAdapter extends BaseRecyclerAdapter<Category> {

        @Override
        public ViewHolderCreator createViewHolderCreator() {
            return new ViewHolderCreator() {
                @Override
                public CommonViewHolder<Category> createByViewGroupAndType(ViewGroup parent, int viewType, Object... p) {
                    return new CommonViewHolder<Category>(parent.getContext(), parent, R.layout.item_download_amount) {
                        @Override
                        public void bindData(Category category, int position) {
                            TextView tv = itemView.findViewById(R.id.tv_text);
                            tv.setText(category.getName());
                        }
                    };
                }
            };
        }
    }


}
