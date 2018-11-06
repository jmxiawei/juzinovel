package xcvf.top.readercore;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.FrameLayout;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.hannesdorfmann.mosby3.mvp.MvpActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import top.iscore.freereader.R;
import top.iscore.freereader.fragment.ChapterFragment;
import top.iscore.freereader.fragment.LoadingFragment;
import top.iscore.freereader.mode.Colorful;
import top.iscore.freereader.mode.setter.ViewBackgroundColorSetter;
import top.iscore.freereader.mvp.presenters.BookReadPresenter;
import top.iscore.freereader.mvp.presenters.BookShelfPresenter;
import top.iscore.freereader.mvp.view.BookReadView;
import xcvf.top.readercore.bean.Book;
import xcvf.top.readercore.bean.BookMark;
import xcvf.top.readercore.bean.Category;
import xcvf.top.readercore.bean.Chapter;
import xcvf.top.readercore.bean.Mode;
import xcvf.top.readercore.bean.Page;
import xcvf.top.readercore.bean.SettingAction;
import xcvf.top.readercore.bean.TextConfig;
import xcvf.top.readercore.bean.User;
import xcvf.top.readercore.impl.ChapterDisplayedImpl;
import xcvf.top.readercore.impl.ChapterProviderImpl;
import xcvf.top.readercore.impl.FullScreenHandler;
import xcvf.top.readercore.interfaces.Area;
import xcvf.top.readercore.interfaces.IAreaClickListener;
import xcvf.top.readercore.interfaces.IChapterListener;
import xcvf.top.readercore.interfaces.IChapterProvider;
import xcvf.top.readercore.interfaces.ILoadChapter;
import xcvf.top.readercore.interfaces.IPage;
import xcvf.top.readercore.interfaces.IPageScrollListener;
import xcvf.top.readercore.interfaces.OnTextConfigChangedListener;
import xcvf.top.readercore.services.DownloadIntentService;
import xcvf.top.readercore.styles.ModeConfig;
import xcvf.top.readercore.styles.ModeProvider;
import xcvf.top.readercore.views.PopDownload;
import xcvf.top.readercore.views.PopFontSetting;
import xcvf.top.readercore.views.ReaderSettingView;
import xcvf.top.readercore.views.ReaderView;

/**
 * 阅读页面
 * Created by xiaw on 2018/6/30.
 */
public class ReaderActivity extends MvpActivity<BookReadView, BookReadPresenter> implements IAreaClickListener, IPageScrollListener, BookReadView {


    Book book;

    @BindView(R.id.readerView)
    ReaderView readerView;


    ChapterDisplayedImpl mChapterDisplayedImpl;
    BookMark mBookMark;
    User mUser;
    @BindView(R.id.setting_view)
    ReaderSettingView settingView;

    ChapterFragment chapterFragment;

    FullScreenHandler fullScreenHandler;

    LoadingFragment mLoadingFragment;

    List<Chapter> mChapterList;

    BookShelfPresenter mBookShelfPresenter;


    /**
     * 是否已经加载过章节列表
     */
    boolean loadedChapter = false;

    boolean isShowSuccess = false;
    @BindView(R.id.activity_content)
    FrameLayout activityContent;

    /**
     * 阅读页面
     *
     * @param activity
     * @param book
     */
    public static void toReadPage(Activity activity, Book book) {
        Intent intent = new Intent(activity, ReaderActivity.class);
        intent.putExtra("book", book);
        activity.startActivity(intent);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);
        ButterKnife.bind(this);
        presenter.attachView(this);
        book = getIntent().getParcelableExtra("book");
        mUser = User.currentUser();
        settingView.setBook(book);
        settingView.setSettingListener(mSettingListener);
        settingView.ActivityonCreate(this);
        if (mUser != null) {
            mBookShelfPresenter = new BookShelfPresenter();
            mBookShelfPresenter.addBookShelf(mUser.getUid(), book.extern_bookid);
        }
        initReadView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.INTERNET}, 1);
                return;
            }
        }
        checkChapters(loadedChapter);
        fullScreenHandler = new FullScreenHandler(this, readerView, settingView);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        settingView.ActivityonDestory(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        fullScreenHandler.hide();
    }


    /**
     * 加载一个章节,失败的章节重新加载
     */
    private ILoadChapter mLoadChapter = new ILoadChapter() {
        @Override
        public void load(int type, Chapter chapter) {

            ChapterProviderImpl.newInstance().getChapter(type, book.extern_bookid, String.valueOf(chapter.chapterid), chapter, new IChapterListener() {
                @Override
                public void onChapter(int code, Chapter srcChapter, Chapter chapter, HashMap<String, Object> params) {
                    mChapterDisplayedImpl.showChapter(false, readerView, 0, IPage.LOADING_PAGE, chapter);
                }
            });
        }
    };

    /**
     * 设置按钮
     */
    private ReaderSettingView.ISettingListener mSettingListener = new ReaderSettingView.ISettingListener() {
        @Override
        public void onSettingChanged(SettingAction action) {
            if (action == SettingAction.ACTION_BACK) {
                finish();
            } else if (action == SettingAction.ACTION_CHAPTER) {
                chapterFragment = new ChapterFragment();
                chapterFragment.setBook(book);
                chapterFragment.setChapter(mChapterList, readerView.getCurrentChapter());
                chapterFragment.setSwitchChapterListener(switchChapterListener);
                chapterFragment.show(getSupportFragmentManager(), "ChapterFragment");
            } else if (action == SettingAction.ACTION_MODE) {
                Mode current = ModeProvider.getCurrentMode();
                //切换日间模式和夜间模式
                Mode dest;
                if (current == Mode.NightMode) {
                    dest = Mode.DayMode;
                } else {
                    dest = Mode.NightMode;
                }
                settingView.changeMode(dest);
                ModeConfig config = ModeProvider.get(dest);
                TextConfig.getConfig().setBackgroundColor(config.getBgResId());
                TextConfig.getConfig().setTextColor(config.getTextColorResId());
                onTextConfigChangedListener.onChanged(TextConfig.TYPE_FONT_COLOR);
                ModeProvider.save(config.getId(), dest);
                fullScreenHandler.check();
            } else if (action == SettingAction.ACTION_FONT) {
                PopFontSetting popFontSetting = new PopFontSetting(getBaseContext());
                popFontSetting.setOnTextConfigChangedListener(onTextConfigChangedListener);
                popFontSetting.showAsDropDown(getWindow().getDecorView());
            } else if (action == SettingAction.ACTION_CACHE) {
                //缓存
                PopDownload popDownload = new PopDownload(getBaseContext(), readerView.getCurrentChapter(), mChapterList);
                popDownload.setOnDownloadCmdListener(onDownloadCmdListener);
                popDownload.showAsDropDown(getWindow().getDecorView());

            }
        }
    };

    /**
     * 开始下载
     */
    private PopDownload.OnDownloadCmdListener onDownloadCmdListener = new PopDownload.OnDownloadCmdListener() {
        @Override
        public void onComd(Chapter chapter, Category category) {

            ArrayList<Chapter> chapters = new ArrayList<>(Chapter.getLeftChapter(chapter.extern_bookid, String.valueOf(chapter.chapterid), category.getIntValue()));
            DownloadIntentService.startDownloadService(getBaseContext(), chapter, chapters);
        }
    };

    /**
     * 字体设置改了
     */
    private OnTextConfigChangedListener onTextConfigChangedListener = new OnTextConfigChangedListener() {
        @Override
        public void onChanged(int type) {
            if (type == TextConfig.TYPE_FONT_COLOR) {
                readerView.onTextConfigChanged();
            } else {
                // TextConfig.TYPE_FONT_SIZE
                Chapter chapter = readerView.getCurrentChapter();
                Page page = readerView.getCurrentPage();
                int start = page.getPageTotalChars();
                mChapterDisplayedImpl.showChapter(true, readerView, start, page.getIndex(), chapter);
            }
        }
    };


    /**
     * 切换章节,直接切换到某个章节
     */
    private ChapterFragment.switchChapterListener switchChapterListener = new ChapterFragment.switchChapterListener() {
        @Override
        public void onChapter(Chapter chapter) {
            mChapterDisplayedImpl.showChapter(true, readerView, 0, Page.LOADING_PAGE, chapter);
        }
    };

    private void checkChapters(final boolean loadedChapter) {
        mBookMark = BookMark.getMark(book, mUser.getUid());
        String chapterid = "0";
        if (mBookMark != null) {
            chapterid = mBookMark.getChapterid();
        }
        int type = IChapterProvider.TYPE_DETAIL;
        if ("0".equals(chapterid)) {
            type = IChapterProvider.TYPE_NEXT;
        }
        ChapterProviderImpl.newInstance().getChapter(type, book.extern_bookid, chapterid, null, new IChapterListener() {
            @Override
            public void onChapter(int code, Chapter srcChapter, Chapter chapter, HashMap<String, Object> params) {
                if (chapter != null) {
                    isShowSuccess = true;
                    mChapterDisplayedImpl.showChapter(false, readerView, 0, mBookMark == null ? IPage.LOADING_PAGE : mBookMark.getPage(), chapter);
                    saveBookMark();
                }
                loadData(!loadedChapter);
            }
        });

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        checkChapters(loadedChapter);
    }

    @NonNull
    @Override
    public BookReadPresenter createPresenter() {
        return new BookReadPresenter();
    }

    private void initReadView() {
        mChapterDisplayedImpl = ChapterDisplayedImpl.newInstance();
        readerView.setAreaClickListener(this);
        readerView.setPageScrollListener(this);
        readerView.setLoadChapter(mLoadChapter);

    }

    @Override
    public void clickArea(Area area) {
        fullScreenHandler.check();
    }

    @Override
    public void onScroll(int current, int total, int nextOrPre) {

        if (nextOrPre == IPageScrollListener.PRE_CHAPTER) {
            //上一章节
            Chapter chapter = readerView.getCurrentChapter();
            ChapterProviderImpl.newInstance().getChapter(ChapterProviderImpl.TYPE_PRE, book.extern_bookid, String.valueOf(chapter.chapterid), chapter, new IChapterListener() {
                @Override
                public void onChapter(int code, Chapter srcChapter, Chapter chapter, HashMap<String, Object> params) {
                    mChapterDisplayedImpl.showChapter(false, readerView, 0, IPage.LOADING_PAGE, chapter);
                }
            });

        } else if (nextOrPre == IPageScrollListener.NEXT_CHAPTER) {
            //下一章节
            Chapter chapter = readerView.getCurrentChapter();
            ChapterProviderImpl.newInstance().getChapter(ChapterProviderImpl.TYPE_NEXT, book.extern_bookid, String.valueOf(chapter.chapterid), chapter, new IChapterListener() {
                @Override
                public void onChapter(int code, Chapter srcChapter, Chapter chapter, HashMap<String, Object> params) {
                    mChapterDisplayedImpl.showChapter(false, readerView, 0, IPage.LOADING_PAGE, chapter);
                }
            });
        }

        saveBookMark();

    }


    /**
     * 更新书签
     */
    private void saveBookMark() {
        Chapter chapter = readerView.getCurrentChapter();
        //保存书签
        if (chapter != null) {
            if (mBookMark == null) {
                mBookMark = new BookMark(mUser.getUid(), book.extern_bookid);
            }
            mBookMark.setExtern_bookid(book.extern_bookid);
            mBookMark.setChapterid(String.valueOf(chapter.chapterid));
            mBookMark.setTime_stamp(System.currentTimeMillis());
            Page page = readerView.getCurrentPage();
            mBookMark.setPage(page.getIndex());
            mBookMark.save();
        }
    }

    @Override
    public void onLoadChapterList(final ArrayList<Chapter> chapters) {
        book.chapters = chapters;
        ChapterProviderImpl.newInstance().saveChapter(book.extern_bookid, chapters, new IChapterListener() {
            @Override
            public void onChapter(int code, Chapter srcChapter, Chapter destChapter, HashMap<String, Object> params) {
                if (mLoadingFragment != null) {
                    mLoadingFragment.dismiss();
                    fullScreenHandler.hide();
                }
                mChapterList = (ArrayList<Chapter>) params.get(ChapterProviderImpl.KEY_CHAPTER_LIST);
                LogUtils.e("[读取章节完成..." + mChapterList.size() + "章]");
                if (!isShowSuccess) {
                    checkChapters(loadedChapter);
                }

            }
        });

    }

    @Override
    public void showLoading(boolean pullToRefresh) {

    }

    @Override
    public void showContent() {

    }

    @Override
    public void showError(Throwable e, boolean pullToRefresh) {

    }

    @Override
    public void setData(Chapter data) {

    }


    @Override
    public void loadData(boolean pullToRefresh) {

        if (loadedChapter) {
            return;
        }

        //章节
        if (pullToRefresh) {
            //显示loading
            mLoadingFragment = LoadingFragment.newOne("正在获取章节...");
            mLoadingFragment.show(getSupportFragmentManager(), "LoadingFragment");
        }
        loadedChapter = true;
        LogUtils.e("start load chapters");
        presenter.loadChapters(book, SPUtils.getInstance().getInt(book.extern_bookid, 0));
    }
}
