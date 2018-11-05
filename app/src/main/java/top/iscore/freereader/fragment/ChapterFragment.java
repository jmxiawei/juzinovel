package top.iscore.freereader.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import top.iscore.freereader.R;
import top.iscore.freereader.adapter.ChapterListAdapter;
import top.iscore.freereader.fragment.adapters.CommonViewHolder;
import top.iscore.freereader.fragment.adapters.OnRecyclerViewItemClickListener;
import top.iscore.freereader.mode.Colorful;
import top.iscore.freereader.mode.setter.TextColorSetter;
import top.iscore.freereader.mode.setter.ViewBackgroundColorSetter;
import top.iscore.freereader.mode.setter.ViewGroupSetter;
import xcvf.top.readercore.bean.Book;
import xcvf.top.readercore.bean.Chapter;
import xcvf.top.readercore.bean.Mode;
import xcvf.top.readercore.styles.ModeProvider;

/**
 * 章节列表
 * Created by xiaw on 2018/9/27.
 */
public class ChapterFragment extends DialogFragment {

    @BindView(R.id.recycler)
    RecyclerView recycler;
    Unbinder unbinder;
    ChapterListAdapter mChapterListAdapter;
    switchChapterListener switchChapterListener;

    Book mbook;
    Chapter chapter;
    @BindView(R.id.tv_book)
    TextView tvBook;

    List<Chapter> allList;

    public void setBook(Book mbook) {
        this.mbook = mbook;
    }


    public void setChapter(List<Chapter> allList, Chapter chapter) {
        this.chapter = chapter;
        this.allList = allList;
    }

    public void setSwitchChapterListener(ChapterFragment.switchChapterListener switchChapterListener) {
        this.switchChapterListener = switchChapterListener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chapter_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        initViews();
        mChapterListAdapter.setCurrentChapter(chapter);
        mChapterListAdapter.setDataList(this.allList);
        int index = this.allList.indexOf(chapter);
        if (index >= 0) {
            if (index > 5) {
                //让当前页显示在中间
                index = index - 5;
            }
            recycler.scrollToPosition(index);
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        changeMode();
    }

    private void changeMode() {

        new Colorful
                .Builder(getActivity())
                .setter(new ViewBackgroundColorSetter(getView(), R.attr.colorPrimary))
                .setter(new TextColorSetter(tvBook, R.attr.text_color))
                .setter(new ViewGroupSetter(recycler, R.attr.colorPrimary)
                        .childViewTextColor(R.id.tv_chapter_name, R.attr.text_color)
                        .childViewTextColor(R.id.tv_download_status, R.attr.text_second_color)
                        .childViewBgColor(R.id.ll_content, R.attr.colorPrimary))
                .create()
                .setTheme(ModeProvider.getCurrentModeTheme());

    }


    public interface switchChapterListener {
        void onChapter(Chapter chapter);
    }

    private void initViews() {
        tvBook.setText(mbook.name);
        recycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mChapterListAdapter = new ChapterListAdapter();
        recycler.setAdapter(mChapterListAdapter);
        mChapterListAdapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener<Chapter>() {
            @Override
            public void onRecyclerViewItemClick(CommonViewHolder holder, int position, Chapter item) {
                if (switchChapterListener != null) {
                    switchChapterListener.onChapter(item);
                }
                dismiss();
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


}
