package xcvf.top.readercore.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import bolts.Continuation;
import bolts.Task;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import top.iscore.freereader.R;
import top.iscore.freereader.fragment.adapters.BaseRecyclerAdapter;
import top.iscore.freereader.fragment.adapters.CommonViewHolder;
import top.iscore.freereader.fragment.adapters.OnRecyclerViewItemClickListener;
import top.iscore.freereader.fragment.adapters.ViewHolderCreator;
import top.iscore.freereader.mode.Colorful;
import top.iscore.freereader.mode.setter.ViewBackgroundColorSetter;
import top.iscore.freereader.mode.setter.ViewGroupSetter;
import top.iscore.freereader.mvp.presenters.BookSourcePresenter;
import top.iscore.freereader.mvp.view.BookSourceView;
import xcvf.top.readercore.bean.Book;
import xcvf.top.readercore.bean.BookMark;
import xcvf.top.readercore.bean.Chapter;
import xcvf.top.readercore.bean.Mode;
import xcvf.top.readercore.bean.User;
import xcvf.top.readercore.impl.ChapterParserFactory;
import xcvf.top.readercore.interfaces.IChapterParser;
import xcvf.top.readercore.styles.ModeProvider;

/**
 * 换源列表
 */
public class BookSourceDialog extends DialogFragment implements BookSourceView {


    @BindView(R.id.recycler)
    RecyclerView recycler;
    Unbinder unbinder;
    BAdapter mBAdapter;

    Book mBook;
    Chapter mChapter;
    BookSourcePresenter bookSourcePresenter;
    @BindView(R.id.tv_book_name)
    TextView tvBookName;
    User mUser;
    @BindView(R.id.progress_loading)
    ProgressBar progressLoading;

    public void setBook(Book mBook, Chapter chapter) {
        this.mBook = mBook;
        this.mChapter = chapter;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_dialog_source, container, false);
        unbinder = ButterKnife.bind(this, view);
        bookSourcePresenter = new BookSourcePresenter();
        bookSourcePresenter.attachView(this);
        mUser = User.currentUser();
        initView();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        changeMode();
    }

    private void changeMode() {
        new Colorful.Builder(getActivity())
                .setter(new ViewBackgroundColorSetter(getView(), R.attr.colorPrimary))
                .create()
                .setTheme(ModeProvider.getCurrentModeTheme());
    }

    private void initView() {
        tvBookName.setText(mBook.name);
        mBAdapter = new BAdapter();
        recycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recycler.setAdapter(mBAdapter);
        mBAdapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener<Book>() {
            @Override
            public void onRecyclerViewItemClick(final CommonViewHolder holder, int position, final Book item) {
                progressLoading.setVisibility(View.VISIBLE);
                Task.callInBackground(new Callable<Chapter>() {
                    @Override
                    public Chapter call() throws Exception {
                        IChapterParser chapterParser = ChapterParserFactory.getChapterParser(item.engine_domain);
                        if(chapterParser !=null){
                          ArrayList<Chapter>  chapterList = (ArrayList<Chapter>) chapterParser.parser(holder.itemView.getContext(),item,item.read_url);
                          return  findFromList(chapterList,mChapter);
                        }
                        return null;
                    }
                }).continueWith(new Continuation<Chapter, Object>() {
                    @Override
                    public Object then(Task<Chapter> task) throws Exception {
                        progressLoading.setVisibility(View.INVISIBLE);
                        Chapter chapter = task.getResult();
                        if(chapter == null){
                            ToastUtils.showShort("换源失败,请重新选择来源");
                        }else {
                            BookMark bookMark = new BookMark(mUser.getUid(),item.bookid);
                            bookMark.setChapterid(chapter.chapterid+"");
                            bookMark.setBookid(item.bookid);
                            bookMark.setExtern_bookid(item.extern_bookid);
                            bookMark.setPage(1);
                            bookMark.setTime_stamp(System.currentTimeMillis());
                            bookMark.save();
                            dismiss();
                        }

                        return null;
                    }
                },Task.UI_THREAD_EXECUTOR);




            }
        });
        progressLoading.setVisibility(View.VISIBLE);
        bookSourcePresenter.loadBookSource(mBook.name, mBook.author);
    }


    private Chapter findFromList(ArrayList<Chapter> chapterList,Chapter orgin) {
        for (Chapter chapter: chapterList) {
            if(chapter.chapter_name.equals(orgin.chapter_name)){
                 return chapter;
            }
        }
        return null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onLoadAllSource(List<Book> books) {
        mBAdapter.setDataList(books);
        progressLoading.setVisibility(View.GONE);
    }

    private static class BAdapter extends BaseRecyclerAdapter<Book> {

        @Override
        public ViewHolderCreator createViewHolderCreator() {
            return new ViewHolderCreator() {
                @Override
                public CommonViewHolder<Book> createByViewGroupAndType(ViewGroup parent, int viewType, Object... p) {
                    return new CommonViewHolder<Book>(parent.getContext(), parent, R.layout.item_book_source) {
                        @Override
                        public void bindData(Book book, int position) {
                            TextView tv_option = itemView.findViewById(R.id.tv_option);
                            Mode mode = ModeProvider.getCurrentMode();
                            if (mode == Mode.DayMode) {
                                itemView.setBackgroundResource(R.drawable.btn_select_day);
                            } else {
                                itemView.setBackgroundResource(R.drawable.btn_select_night);
                            }
                            tv_option.setText(ChapterParserFactory.getSourceName(book.engine_domain) + "(" + book.engine_domain + ")");
                        }
                    };
                }
            };
        }
    }

}
