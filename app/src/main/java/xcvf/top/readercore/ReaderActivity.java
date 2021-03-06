package xcvf.top.readercore;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Pair;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;

import com.blankj.utilcode.util.LogUtils;
import com.hannesdorfmann.mosby3.mvp.MvpActivity;
import com.scwang.smartrefresh.layout.listener.OnStateChangedListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;

import bolts.Task;
import butterknife.BindView;
import butterknife.ButterKnife;
import top.iscore.freereader.BaseActivity;
import top.iscore.freereader.BookDetailActivity;
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
import xcvf.top.readercore.interfaces.IChangeSourceListener;
import xcvf.top.readercore.interfaces.IChapterListener;
import xcvf.top.readercore.interfaces.IChapterProvider;
import xcvf.top.readercore.interfaces.ILoadChapter;
import xcvf.top.readercore.interfaces.IPageScrollListener;
import xcvf.top.readercore.interfaces.OnTextConfigChangedListener;
import xcvf.top.readercore.services.DownloadIntentService;
import xcvf.top.readercore.styles.ModeConfig;
import xcvf.top.readercore.styles.ModeProvider;
import xcvf.top.readercore.transformer.PageTransformerFactory;
import xcvf.top.readercore.views.BookSourceDialog;
import xcvf.top.readercore.views.ContentDialog;
import xcvf.top.readercore.views.PopDownload;
import xcvf.top.readercore.views.PopFontSetting;
import xcvf.top.readercore.views.ReaderSettingView;
import xcvf.top.readercore.views.ReaderView;

/**
 * 阅读页面
 * Created by xiaw on 2018/6/30.
 */
public class ReaderActivity extends BaseActivity<BookReadView, BookReadPresenter> implements
        IAreaClickListener, IPageScrollListener, BookReadView, FullScreenHandler.OnSettingViewStateChanged {


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
        activity.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(activity).toBundle());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置contentFeature,可使用切换动画
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        Transition fade = TransitionInflater.from(this).inflateTransition(android.R.transition.fade);
        getWindow().setEnterTransition(fade);

        setContentView(R.layout.activity_reader);
        ButterKnife.bind(this);
        presenter.attachView(this);
        mBookShelfPresenter = new BookShelfPresenter();
        settingView.setSettingListener(mSettingListener);
        settingView.ActivityonCreate(this);
        initReadView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.INTERNET}, 1);
                return;
            }
        }
        fullScreenHandler = new FullScreenHandler(this, readerView, settingView);
        fullScreenHandler.setOnSettingViewStateChanged(this);
        onNewIntent(getIntent());
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent == null) {
            return;
        }
        book = intent.getParcelableExtra("book");
        mUser = User.currentUser();
        settingView.setBook(book);
        loadedChapter = false;
        isShowSuccess = false;
        mChapterList = null;
        book.chapters = null;
        checkChapters(loadedChapter);
    }

    @Override
    public void onBackPressed() {
        if (TextUtils.isEmpty(book.shelfid) && !book.findFromLocal()) {
            ContentDialog dialog = new ContentDialog();
            dialog.setTitle("添书").setContent("你还没关注本书，是否将本书加入书架?").setNegativeListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            }).setPositiveListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mBookShelfPresenter.addBookShelf(mUser.getUid(), book.bookid, book);
                    finish();
                }
            }).show(getSupportFragmentManager(), "ContentDialog");
        } else {
            super.onBackPressed();
        }

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
        changeMode();
    }

    private void changeMode() {
        new Colorful.Builder(this)
                .setter(new ViewBackgroundColorSetter(settingView.findViewById(R.id.ll_setting_top), R.attr.bg_dark))
                .setter(new ViewBackgroundColorSetter(settingView.findViewById(R.id.ll_setting_bottom), R.attr.bg_dark))
                .create().setTheme(ModeProvider.getCurrentModeTheme());
    }


    /**
     * 加载一个章节,失败的章节重新加载
     */
    private ILoadChapter mLoadChapter = new ILoadChapter() {
        @Override
        public void load(int type, Chapter chapter) {
            mLoadingFragment = LoadingFragment.newOne(chapter.chapter_name, 1000);
            mLoadingFragment.show(getSupportFragmentManager(), "LoadingFragment");
            ChapterProviderImpl.newInstance().getChapter(type, book.extern_bookid, String.valueOf(chapter.chapterid), chapter, new IChapterListener() {
                @Override
                public void onChapter(int code, Chapter srcChapter, Chapter chapter, HashMap<String, Object> params) {
                    if (mLoadingFragment != null) {
                        mLoadingFragment.dismiss();
                    }
                    mChapterDisplayedImpl.showChapter(false, readerView, 0, Page.LOADING_PAGE, chapter);
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
                onBackPressed();
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
                changeMode();
            } else if (action == SettingAction.ACTION_FONT) {
                Context act = ReaderActivity.this;
                PopFontSetting popFontSetting = new PopFontSetting(act);
                popFontSetting.setOnTextConfigChangedListener(onTextConfigChangedListener);
                popFontSetting.showAsDropDown(getWindow().getDecorView());
            } else if (action == SettingAction.ACTION_CACHE) {
                //缓存
                PopDownload popDownload = new PopDownload(getBaseContext(), readerView.getCurrentChapter(), mChapterList);
                popDownload.setOnDownloadCmdListener(onDownloadCmdListener);
                popDownload.showAsDropDown(getWindow().getDecorView());

            } else if (action == SettingAction.ACTION_DETAIL) {
                BookDetailActivity.toBookDetail(ReaderActivity.this, book.bookid);
            } else if (action == SettingAction.ACTION_CHANGE) {
                BookSourceDialog bookSourceDialog = new BookSourceDialog();
                bookSourceDialog.setBook(book, readerView.getCurrentChapter(), mChangeSourceListener);
                bookSourceDialog.show(getSupportFragmentManager(), "BookSourceDialog");
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
            DownloadIntentService.startDownloadService(getApplicationContext(), book, chapter, chapters);
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
            } else if (type == TextConfig.TYPE_FONT_SIZE) {
                // TextConfig.TYPE_FONT_SIZE
                Chapter chapter = readerView.getCurrentChapter();
                Page page = readerView.getCurrentPage();
                int start = page.getPageTotalChars();
                mChapterDisplayedImpl.showChapter(true, readerView, start, page.getIndex(), chapter);
            } else if (type == TextConfig.TYPE_PAGE_ANIM) {
                readerView.setPageTransformer(PageTransformerFactory.get().pageTransformer);
            }
        }
    };

    /**
     * 切换来源
     */
    private IChangeSourceListener mChangeSourceListener = new IChangeSourceListener() {
        @Override
        public void onChangeSource(Book bk, BookMark bookMark) {
            book.setExtern_bookid(bk.extern_bookid);
            book.engine_domain = bk.engine_domain;
            book.read_url = bk.read_url;
            mChapterList = bk.chapters;
            checkChapters(false);
        }
    };

    /**
     * 切换章节,直接切换到某个章节
     */
    private ChapterFragment.switchChapterListener switchChapterListener = new ChapterFragment.switchChapterListener() {
        @Override
        public void onChapter(Chapter chapter) {
            mLoadingFragment = LoadingFragment.newOne(chapter.chapter_name);
            mLoadingFragment.show(getSupportFragmentManager(), "LoadingFragment");
            mChapterDisplayedImpl.showChapter(true, readerView, 0, Page.LOADING_PAGE, chapter);
        }
    };

    /**
     * 阅读历史，本地>服务端
     *
     * @param loadedChapter
     */
    private void checkChapters(final boolean loadedChapter) {
        mBookMark = BookMark.getMark(book, mUser.getUid());
        String chapterid = "0";
        if (mBookMark != null) {
            chapterid = mBookMark.getChapterid();
        } else {
            if (!TextUtils.isEmpty(book.chapter_name)) {
                //服务端阅读历史记录
                mBookMark = new BookMark(mUser.getUid(), book.bookid);
                mBookMark.setPage(book.page);
                mBookMark.setChapterid(book.chapterid);
                mBookMark.setExtern_bookid(book.extern_bookid);
                chapterid = book.chapterid;
            }
        }
        if (TextUtils.isEmpty(chapterid)) {
            chapterid = "0";
        }
        int type = IChapterProvider.TYPE_DETAIL;
        if ("0".equals(chapterid)) {
            type = IChapterProvider.TYPE_NEXT;
        }
        ChapterProviderImpl.newInstance().getChapter(type, book.extern_bookid, chapterid, null, new IChapterListener() {
            @Override
            public void onChapter(int code, Chapter srcChapter, Chapter chapter, HashMap<String, Object> params) {
                if (chapter == null && mChapterList != null && mChapterList.size() > 0) {
                    chapter = mChapterList.get(0);
                }
                if (chapter != null) {
                    isShowSuccess = true;
                    mChapterDisplayedImpl.showChapter(true, readerView, 0, mBookMark == null ? Page.LOADING_PAGE : mBookMark.getPage(), chapter);
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
        readerView.setShowChapterSuccess(mshowChapter);

    }

    /**
     * 显示章节成功，隐藏loading
     */
    private ILoadChapter mshowChapter = new ILoadChapter() {
        @Override
        public void load(int type, Chapter chapter) {
            if (mLoadingFragment != null) {
                mLoadingFragment.dismiss();
            }
        }
    };

    @Override
    public void clickArea(Area area) {
        if (area == Area.CENTER) {
            fullScreenHandler.check();
        } else if (area == Area.LEFT) {
            readerView.scrollPrePage();
        } else if (area == Area.RIGHT) {
            readerView.scrollNextPage();
        }
    }

    @Override
    public void onScroll(int current, int total, int nextOrPre) {

        if (nextOrPre == IPageScrollListener.PRE_CHAPTER) {
            //上一章节
            Chapter chapter = readerView.getCurrentChapter();
            if (chapter != null) {
                ChapterProviderImpl.newInstance().getChapter(ChapterProviderImpl.TYPE_PRE, book.extern_bookid, String.valueOf(chapter.chapterid), chapter, new IChapterListener() {
                    @Override
                    public void onChapter(int code, Chapter srcChapter, Chapter chapter, HashMap<String, Object> params) {
                        mChapterDisplayedImpl.showChapter(false, readerView, 0, Page.LOADING_PAGE, chapter);
                    }
                });
            }
        } else if (nextOrPre == IPageScrollListener.NEXT_CHAPTER) {
            //下一章节
            Chapter chapter = readerView.getCurrentChapter();
            if (chapter != null) {
                ChapterProviderImpl.newInstance().getChapter(ChapterProviderImpl.TYPE_NEXT, book.extern_bookid, String.valueOf(chapter.chapterid), chapter, new IChapterListener() {
                    @Override
                    public void onChapter(int code, Chapter srcChapter, Chapter chapter, HashMap<String, Object> params) {
                        mChapterDisplayedImpl.showChapter(false, readerView, 0, Page.LOADING_PAGE, chapter);
                    }
                });
            }
        }
        saveBookMark();
        fullScreenHandler.hide();
    }


    /**
     * 更新书签
     */
    private void saveBookMark() {

        Task.callInBackground(new Callable<String>() {
            @Override
            public String call() throws Exception {
                Chapter chapter = readerView.getCurrentChapter();
                //保存书签
                if (chapter != null) {
                    if (mBookMark == null) {
                        mBookMark = new BookMark(mUser.getUid(), book.bookid);
                    }
                    mBookMark.setExtern_bookid(book.extern_bookid);
                    mBookMark.setChapterid(String.valueOf(chapter.chapterid));
                    mBookMark.setTime_stamp(System.currentTimeMillis());
                    Page page = readerView.getCurrentPage();
                    mBookMark.setPage(page.getIndex());
                    mBookMark.save();
                    if (mUser.getUid() > 0 && page.getStatus() == Page.OK_PAGE) {
                        mBookShelfPresenter.addBookMarker(mUser.getUid(),
                                book.bookid,
                                book.extern_bookid,
                                chapter.chapter_name,
                                "" + chapter.chapterid,
                                page.getIndex(), book.engine_domain, book.read_url);
                    }
                }
                return null;
            }
        });
    }

    @Override
    public void onLoadChapterList(final ArrayList<Chapter> chapters) {
        book.chapters = chapters;
        mChapterList = chapters;
        LogUtils.e("[读取章节完成..." + mChapterList.size() + "章]");
        if (mLoadingFragment != null) {
            mLoadingFragment.dismiss();
            fullScreenHandler.hide();
        }

        if (mChapterList.size() == 0) {
            //加载章节失败
            ContentDialog contentDialog = new ContentDialog();
            contentDialog.setTitle("加载章节失败").setContent("加载书籍章节失败，建议重新加载或者切换来源").setNegativeListener("重新加载", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadedChapter = false;
                    checkChapters(false);
                }
            }).setPositiveListener("切换来源", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSettingListener.onSettingChanged(SettingAction.ACTION_CHANGE);
                }
            }).show(getSupportFragmentManager(), "ContentDialog");
        } else {
            if (!isShowSuccess) {
                mLoadingFragment = LoadingFragment.newOne("读取章节内容...");
                mLoadingFragment.show(getSupportFragmentManager(), "LoadingFragment");
                checkChapters(loadedChapter);
            }
        }

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
        presenter.loadChapters(this, book);
    }


    /**
     * 设置的时候不使用翻页动画
     * @param state
     */
    @Override
    public void onStateChanged(int state) {
        if (state == FullScreenHandler.OnSettingViewStateChanged.STATE_VISIABLE) {
            readerView.setPageTransformer(null);
        } else if (state == FullScreenHandler.OnSettingViewStateChanged.STATE_GONE) {
            readerView.setPageTransformer(PageTransformerFactory.get().pageTransformer);
        }
    }
}
