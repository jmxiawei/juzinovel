package xcvf.top.readercore;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.concurrent.Callable;

import bolts.Continuation;
import bolts.Task;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import top.iscore.freereader.R;
import top.iscore.freereader.adapter.ChapterListAdapter;
import top.iscore.freereader.fragment.adapters.CommonViewHolder;
import top.iscore.freereader.fragment.adapters.OnReachBottomListener;
import top.iscore.freereader.fragment.adapters.OnRecyclerViewItemClickListener;
import xcvf.top.readercore.bean.Book;
import xcvf.top.readercore.bean.Chapter;

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
    static final int page_size = 100;
    int page = 1;
    boolean hasMore = true;
    boolean isLoading = false;
    @BindView(R.id.tv_book)
    TextView tvBook;

    public void setBook(Book mbook) {
        this.mbook = mbook;
    }


    public void setChapter(Chapter chapter) {
        this.chapter = chapter;
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
        showChapter();
        return view;
    }

    private void showChapter() {

        isLoading = true;
        if (!hasMore) {
            return;
        }
        Task.callInBackground(new Callable<List<Chapter>>() {
            @Override
            public List<Chapter> call() throws Exception {
                int start = (page - 1) * page_size;
                return Chapter.find(Chapter.class, " extern_bookid = ? ", new String[]{mbook.extern_bookid}, null, " chapterid ASC ", start + ", " + page_size + " ");
            }
        }).continueWith(new Continuation<List<Chapter>, Object>() {
            @Override
            public Object then(Task<List<Chapter>> task) throws Exception {

                List<Chapter> chapters = task.getResult();
                if (chapters.size() < page_size) {
                    hasMore = false;
                }
                mChapterListAdapter.setCurrentChapter(chapter);
                if (page == 1) {
                    mChapterListAdapter.setDataList(chapters);
                } else {
                    mChapterListAdapter.appendDataList(chapters);
                }
                page++;
                isLoading = false;
                return null;
            }
        }, Task.UI_THREAD_EXECUTOR);

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
        mChapterListAdapter.setReachBottomListener(new OnReachBottomListener() {
            @Override
            public void onReachBottom() {
                if (!isLoading && hasMore) {
                    showChapter();
                }

            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


}
