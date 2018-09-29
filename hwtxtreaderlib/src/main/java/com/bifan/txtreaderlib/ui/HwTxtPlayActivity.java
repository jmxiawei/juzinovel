package com.bifan.txtreaderlib.ui;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bifan.txtreaderlib.Constant;
import com.bifan.txtreaderlib.R;
import com.bifan.txtreaderlib.bean.TxtMsg;
import com.bifan.txtreaderlib.interfaces.ICenterAreaClickListener;
import com.bifan.txtreaderlib.interfaces.IChapter;
import com.bifan.txtreaderlib.interfaces.ILoadListener;
import com.bifan.txtreaderlib.interfaces.IPageChangeListener;
import com.bifan.txtreaderlib.interfaces.IPageEdgeListener;
import com.bifan.txtreaderlib.interfaces.ISliderListener;
import com.bifan.txtreaderlib.interfaces.ITextSelectListener;
import com.bifan.txtreaderlib.main.TxtConfig;
import com.bifan.txtreaderlib.main.TxtReaderView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import java.io.File;
import java.io.IOException;

/**
 * Created by bifan-wei
 * on 2017/12/8.
 */

public class HwTxtPlayActivity extends AppCompatActivity {
    private Handler mHandler;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewLayout());
        init();
        loadStr("13803.html");
        //loadFile();
        registerListener();
    }

    protected int getContentViewLayout() {
        return R.layout.activity_hwtxtpaly;
    }

    /**
     * @param context  上下文
     * @param FilePath 文本文件路径
     */
    public static void loadTxtFile(Context context, String FilePath) {
        loadTxtFile(context, FilePath, null);
    }

    /**
     * @param context  上下文
     * @param FilePath 文本文件路径
     * @param FileName 显示的书籍或者文件名称
     */
    public static void loadTxtFile(Context context, String FilePath, String FileName) {
        Intent intent = new Intent();
        intent.putExtra("FilePath", FilePath);
        intent.putExtra("FileName", FileName);
        intent.setClass(context, HwTxtPlayActivity.class);
        context.startActivity(intent);
    }

    protected View mTopDecoration, mBottomDecoration;
    protected TextView mChapterNameText;
    protected TextView mChapterMenuText;
    protected TextView mProgressText;
    protected TextView mSettingText;
    protected TextView mSelectedText;
    protected TxtReaderView mTxtReaderView;
    protected View mTopMenu;
    protected View mBottomMenu;
    protected View mCoverView;
    protected View ClipboardView;
    protected String CurrentSelectedText;

    protected ChapterList mChapterListPop;
    protected MenuHolder mMenuHolder = new MenuHolder();

    protected void init() {
        mHandler = new Handler();
        mTopDecoration = findViewById(R.id.activity_hwtxtplay_top);
        mBottomDecoration = findViewById(R.id.activity_hwtxtplay_bottom);
        mTxtReaderView = (TxtReaderView) findViewById(R.id.activity_hwtxtplay_readerView);
        mChapterNameText = (TextView) findViewById(R.id.activity_hwtxtplay_chaptername);
        mChapterMenuText = (TextView) findViewById(R.id.activity_hwtxtplay_chapter_menutext);
        mProgressText = (TextView) findViewById(R.id.activity_hwtxtplay_progress_text);
        mSettingText = (TextView) findViewById(R.id.activity_hwtxtplay_setting_text);
        mTopMenu = findViewById(R.id.activity_hwtxtplay_menu_top);
        mBottomMenu = findViewById(R.id.activity_hwtxtplay_menu_bottom);
        mCoverView = findViewById(R.id.activity_hwtxtplay_cover);
        ClipboardView = findViewById(R.id.activity_hwtxtplay_Clipboar);
        mSelectedText = (TextView) findViewById(R.id.activity_hwtxtplay_selected_text);

        mMenuHolder.mTitle = (TextView) findViewById(R.id.txtreadr_menu_title);
        mMenuHolder.mPreChapter = (TextView) findViewById(R.id.txtreadr_menu_chapter_pre);
        mMenuHolder.mNextChapter = (TextView) findViewById(R.id.txtreadr_menu_chapter_next);
        mMenuHolder.mSeekBar = (SeekBar) findViewById(R.id.txtreadr_menu_seekbar);
        mMenuHolder.mTextSizeDel = (TextView) findViewById(R.id.txtreadr_menu_textsize_del);
        mMenuHolder.mTextSize = (TextView) findViewById(R.id.txtreadr_menu_textsize);
        mMenuHolder.mTextSizeAdd = (TextView) findViewById(R.id.txtreadr_menu_textsize_add);
        mMenuHolder.mBoldSelectedLayout = findViewById(R.id.txtreadr_menu_textsetting1_bold);
        mMenuHolder.mBoldSelectedPic = (ImageView) findViewById(R.id.txtreadr_menu_textsetting1_boldpic);
        mMenuHolder.mNormalSelectedLayout = findViewById(R.id.txtreadr_menu_textsetting1_normal);
        mMenuHolder.mNormalSelectedPic = (ImageView) findViewById(R.id.txtreadr_menu_textsetting1_normalpic);
        mMenuHolder.mCoverSelectedLayout = findViewById(R.id.txtreadr_menu_textsetting2_cover);
        mMenuHolder.mCoverSelectedPic = (ImageView) findViewById(R.id.txtreadr_menu_textsetting2_coverpic);
        mMenuHolder.mTranslateSelectedLayout = findViewById(R.id.txtreadr_menu_textsetting2_translate);
        mMenuHolder.mTranslateSelectedPc = (ImageView) findViewById(R.id.txtreadr_menu_textsetting2_translatepic);
        mMenuHolder.mTextSize = (TextView) findViewById(R.id.txtreadr_menu_textsize);

        mMenuHolder.mStyle1 = findViewById(R.id.hwtxtreader_menu_style1);
        mMenuHolder.mStyle2 = findViewById(R.id.hwtxtreader_menu_style2);
        mMenuHolder.mStyle3 = findViewById(R.id.hwtxtreader_menu_style3);
        mMenuHolder.mStyle4 = findViewById(R.id.hwtxtreader_menu_style4);
        mMenuHolder.mStyle5 = findViewById(R.id.hwtxtreader_menu_style5);


        mTxtReaderView.setOnPageEdgeListener(new IPageEdgeListener() {
            @Override
            public void onCurrentFirstPage() {

            }

            @Override
            public void onCurrentLastPage() {
                //加载下一个章节
                loadStr("13804.html");
            }
        });

    }

    private final int[] StyleTextColors = new int[]{
            Color.parseColor("#4a453a"),
            Color.parseColor("#505550"),
            Color.parseColor("#453e33"),
            Color.parseColor("#8f8e88"),
            Color.parseColor("#27576c")
    };



    /**
     * @param txtMsg
     */
    protected void onLoadDataFail(TxtMsg txtMsg) {
        //加载失败信息
        toast(txtMsg + "");
    }

    /**
     *
     */
    protected void onLoadDataSuccess() {
        setBookName("测试");
        initWhenLoadDone();
    }

    private void loadStr(String filename) {
        try {
            Document document = Jsoup.parse(new File(Constant.getChapterPath(filename)), "utf-8");
            Element element = document.getElementById("content");
            String testText;
            String text = element.text();
            String wtext = element.ownText();
            int size = element.childNodeSize();
            StringBuilder textBuff = new StringBuilder();
            for (int i = 0; i < size; i++) {
                Node node = element.childNode(i);
                if (node instanceof TextNode) {
                    TextNode textNode = (TextNode) node;
                    textBuff.append(textNode.text());
                } else if (node instanceof Element) {
                    Element element1 = (Element) node;
                    if ("br".equals(element1.tagName())) {
                        textBuff.append("\n");
                    }
                }
            }
            Log.e("@@@@@", text);
            Log.e("@@@@@", wtext);
            testText = textBuff.toString();
            Log.e("@@@@@", testText);


            mTxtReaderView.loadText(testText, new ILoadListener() {
                @Override
                public void onSuccess() {
                    setBookName("test with str");
                    initWhenLoadDone();
                }

                @Override
                public void onFail(TxtMsg txtMsg) {
                    //加载失败信息
                    toast(txtMsg + "");
                }

                @Override
                public void onMessage(String message) {
                    //加载过程信息
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }



    }

    protected void initWhenLoadDone() {

        mMenuHolder.mTextSize.setText(mTxtReaderView.getTextSize() + "");
        mTopDecoration.setBackgroundColor(mTxtReaderView.getBackgroundColor());
        mBottomDecoration.setBackgroundColor(mTxtReaderView.getBackgroundColor());
        //mTxtReaderView.setLeftSlider(new MuiLetSlider());//修改左滑动条
        //mTxtReaderView.setRightSlider(new MuiRightSlider());//修改右滑动条
        //字体初始化
        onTextSettingUi(mTxtReaderView.getTxtReaderContext().getTxtConfig().Bold);
        //翻页初始化
        onPageSwitchSettingUi(mTxtReaderView.getTxtReaderContext().getTxtConfig().SwitchByTranslate);
        //保存的翻页模式
        if (mTxtReaderView.getTxtReaderContext().getTxtConfig().SwitchByTranslate) {
            mTxtReaderView.setPageSwitchByTranslate();
        } else {
            mTxtReaderView.setPageSwitchByCover();
        }
        //章节初始化
        if (mTxtReaderView.getChapters() != null && mTxtReaderView.getChapters().size() > 0) {
            WindowManager m = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics metrics = new DisplayMetrics();
            m.getDefaultDisplay().getMetrics(metrics);
            int ViewHeight = metrics.heightPixels - mTopDecoration.getHeight();
            mChapterListPop = new ChapterList(this, ViewHeight, mTxtReaderView.getChapters(), mTxtReaderView.getTxtReaderContext().getParagraphData().getCharNum());
            mChapterListPop.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    IChapter chapter = (IChapter) mChapterListPop.getAdapter().getItem(i);
                    mChapterListPop.dismiss();
                    mTxtReaderView.loadFromProgress(chapter.getStartParagraphIndex(), 0);
                }
            });
            mChapterListPop.setBackGroundColor(mTxtReaderView.getBackgroundColor());
        } else {
            Gone(mChapterMenuText);
        }
    }

    protected void registerListener() {
        mTopMenu.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
        mBottomMenu.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
        mCoverView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Gone(mTopMenu, mBottomMenu, mCoverView);
                return true;
            }
        });

        mSettingText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Show(mTopMenu, mBottomMenu, mCoverView);
            }
        });

        mChapterMenuText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mChapterListPop.isShowing()) {
                    mChapterListPop.showAsDropDown(mTopDecoration);
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            IChapter currentChapter = mTxtReaderView.getCurrentChapter();
                            if (currentChapter != null) {
                                mChapterListPop.setCurrentIndex(currentChapter.getIndex());
                                mChapterListPop.notifyDataSetChanged();
                            }
                        }
                    }, 300);
                } else {
                    mChapterListPop.dismiss();
                }
            }
        });

        mMenuHolder.mSeekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    mTxtReaderView.loadFromProgress(mMenuHolder.mSeekBar.getProgress());
                }
                return false;
            }
        });
        mTxtReaderView.setOnCenterAreaClickListener(new ICenterAreaClickListener() {
            @Override
            public boolean onCenterClick(float widthPercentInView) {
                mSettingText.performClick();
                return true;
            }

            @Override
            public boolean onOutSideCenterClick(float widthPercentInView) {
                if (mBottomMenu.getVisibility() == View.VISIBLE) {
                    mSettingText.performClick();
                    return true;
                }
                return false;
            }
        });

        mTxtReaderView.setPageChangeListener(new IPageChangeListener() {
            @Override
            public void onCurrentPage(float progress) {
                int p = (int) (progress * 1000);
                mProgressText.setText(((float) p / 10) + "%");
                mMenuHolder.mSeekBar.setProgress((int) (progress * 100));
                IChapter currentChapter = mTxtReaderView.getCurrentChapter();
                if (currentChapter != null) {
                    mChapterNameText.setText((currentChapter.getTitle() + "").trim());
                } else {
                    mChapterNameText.setText("无章节");
                }
            }
        });

        //OnTextSelectListener
        mTxtReaderView.setOnTextSelectListener(new ITextSelectListener() {
            @Override
            public void onTextChanging(String selectText) {
                onCurrentSelectedText(selectText);
            }

            @Override
            public void onTextSelected(String selectText) {
                onCurrentSelectedText(selectText);
            }
        });

        mTxtReaderView.setOnSliderListener(new ISliderListener() {
            @Override
            public void onShowSlider(String currentSelectedText) {
                onCurrentSelectedText(currentSelectedText);
                Show(ClipboardView);
            }

            @Override
            public void onReleaseSlider() {
                Gone(ClipboardView);
            }
        });

        mTopMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mChapterListPop.isShowing()) {
                    mChapterListPop.dismiss();
                }
            }
        });


        mMenuHolder.mPreChapter.setOnClickListener(new ChapterChangeClickListener(true));
        mMenuHolder.mNextChapter.setOnClickListener(new ChapterChangeClickListener(false));
        mMenuHolder.mTextSizeAdd.setOnClickListener(new TextChangeClickListener(true));
        mMenuHolder.mTextSizeDel.setOnClickListener(new TextChangeClickListener(false));
        mMenuHolder.mStyle1.setOnClickListener(new StyleChangeClickListener(ContextCompat.getColor(this, R.color.hwtxtreader_styleclor1), StyleTextColors[0]));
        mMenuHolder.mStyle2.setOnClickListener(new StyleChangeClickListener(ContextCompat.getColor(this, R.color.hwtxtreader_styleclor2), StyleTextColors[1]));
        mMenuHolder.mStyle3.setOnClickListener(new StyleChangeClickListener(ContextCompat.getColor(this, R.color.hwtxtreader_styleclor3), StyleTextColors[2]));
        mMenuHolder.mStyle4.setOnClickListener(new StyleChangeClickListener(ContextCompat.getColor(this, R.color.hwtxtreader_styleclor4), StyleTextColors[3]));
        mMenuHolder.mStyle5.setOnClickListener(new StyleChangeClickListener(ContextCompat.getColor(this, R.color.hwtxtreader_styleclor5), StyleTextColors[4]));
        mMenuHolder.mBoldSelectedLayout.setOnClickListener(new TextSettingClickListener(true));
        mMenuHolder.mNormalSelectedLayout.setOnClickListener(new TextSettingClickListener(false));
        mMenuHolder.mTranslateSelectedLayout.setOnClickListener(new SwitchSettingClickListener(true));
        mMenuHolder.mCoverSelectedLayout.setOnClickListener(new SwitchSettingClickListener(false));


    }


    private void onCurrentSelectedText(String SelectedText) {
        mSelectedText.setText("选中" + (SelectedText + "").length() + "个文字");
        CurrentSelectedText = SelectedText;
    }

    private void onTextSettingUi(Boolean isBold) {
        if (isBold) {
            mMenuHolder.mBoldSelectedPic.setBackgroundResource(R.drawable.ic_selected);
            mMenuHolder.mNormalSelectedPic.setBackgroundResource(R.drawable.ic_unselected);
        } else {
            mMenuHolder.mBoldSelectedPic.setBackgroundResource(R.drawable.ic_unselected);
            mMenuHolder.mNormalSelectedPic.setBackgroundResource(R.drawable.ic_selected);
        }
    }

    private void onPageSwitchSettingUi(Boolean isTranslate) {
        if (isTranslate) {
            mMenuHolder.mTranslateSelectedPc.setBackgroundResource(R.drawable.ic_selected);
            mMenuHolder.mCoverSelectedPic.setBackgroundResource(R.drawable.ic_unselected);
        } else {
            mMenuHolder.mTranslateSelectedPc.setBackgroundResource(R.drawable.ic_unselected);
            mMenuHolder.mCoverSelectedPic.setBackgroundResource(R.drawable.ic_selected);
        }
    }

    private class TextSettingClickListener implements View.OnClickListener {
        private Boolean Bold = false;

        public TextSettingClickListener(Boolean bold) {
            Bold = bold;
        }

        @Override
        public void onClick(View view) {
            mTxtReaderView.setTextBold(Bold);
            onTextSettingUi(Bold);
        }
    }

    private class SwitchSettingClickListener implements View.OnClickListener {
        private Boolean isSwitchTranslate = false;

        public SwitchSettingClickListener(Boolean pre) {
            isSwitchTranslate = pre;
        }

        @Override
        public void onClick(View view) {
            if (!isSwitchTranslate) {
                mTxtReaderView.setPageSwitchByCover();
            } else {
                mTxtReaderView.setPageSwitchByTranslate();
            }
            onPageSwitchSettingUi(isSwitchTranslate);
        }
    }


    private class ChapterChangeClickListener implements View.OnClickListener {
        private Boolean Pre = false;

        public ChapterChangeClickListener(Boolean pre) {
            Pre = pre;
        }

        @Override
        public void onClick(View view) {
            if (Pre) {
                mTxtReaderView.jumpToPreChapter();
            } else {
                mTxtReaderView.jumpToNextChapter();
            }
        }
    }

    private class TextChangeClickListener implements View.OnClickListener {
        private Boolean Add = false;

        public TextChangeClickListener(Boolean pre) {
            Add = pre;
        }

        @Override
        public void onClick(View view) {
            int textSize = mTxtReaderView.getTextSize();
            if (Add) {
                if (textSize + 2 <= TxtConfig.MAX_TEXT_SIZE) {
                    mTxtReaderView.setTextSize(textSize + 2);
                    mMenuHolder.mTextSize.setText(textSize + 2 + "");
                }
            } else {
                if (textSize - 2 >= TxtConfig.MIN_TEXT_SIZE) {
                    mTxtReaderView.setTextSize(textSize - 2);
                    mMenuHolder.mTextSize.setText(textSize - 2 + "");
                }
            }

        }
    }

    private class StyleChangeClickListener implements View.OnClickListener {
        private int BgColor;
        private int TextColor;

        public StyleChangeClickListener(int bgColor, int textColor) {
            BgColor = bgColor;
            TextColor = textColor;
        }

        @Override
        public void onClick(View view) {
            mTxtReaderView.setStyle(BgColor, TextColor);
            mTopDecoration.setBackgroundColor(BgColor);
            mBottomDecoration.setBackgroundColor(BgColor);
            if (mChapterListPop != null) {
                mChapterListPop.setBackGroundColor(BgColor);
            }
        }
    }

    protected void setBookName(String name) {
        mMenuHolder.mTitle.setText(name);
    }

    protected void Show(View... views) {
        for (View v : views) {
            v.setVisibility(View.VISIBLE);
        }
    }

    protected void Gone(View... views) {
        for (View v : views) {
            v.setVisibility(View.GONE);
        }
    }


    private Toast t;

    protected void toast(final String msg) {
        if (t != null) {
            t.cancel();
        }
        t = Toast.makeText(HwTxtPlayActivity.this, msg, Toast.LENGTH_SHORT);
        t.show();
    }

    protected class MenuHolder {
        public TextView mTitle;
        public TextView mPreChapter;
        public TextView mNextChapter;
        public SeekBar mSeekBar;
        public TextView mTextSizeDel;
        public TextView mTextSizeAdd;
        public TextView mTextSize;
        public View mBoldSelectedLayout;
        public ImageView mBoldSelectedPic;
        public View mNormalSelectedLayout;
        public ImageView mNormalSelectedPic;
        public View mCoverSelectedLayout;
        public ImageView mCoverSelectedPic;
        public View mTranslateSelectedLayout;
        public ImageView mTranslateSelectedPc;
        public View mStyle1;
        public View mStyle2;
        public View mStyle3;
        public View mStyle4;
        public View mStyle5;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        exist();
    }

    public void BackClick(View view) {
        finish();
    }

    public void onCopyText(View view) {
        if (!TextUtils.isEmpty(CurrentSelectedText)) {
            toast("已经复制到粘贴板");
            ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            cm.setText(CurrentSelectedText + "");
        }
        onCurrentSelectedText("");
        mTxtReaderView.releaseSelectedState();
        Gone(ClipboardView);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        exist();
    }

    boolean hasExisted = false;

    protected void exist() {
        if (!hasExisted) {
            hasExisted = true;
            if (mTxtReaderView != null) {
                mTxtReaderView.saveCurrentProgress();
            }
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mTxtReaderView != null) {
                        mTxtReaderView.getTxtReaderContext().Clear();
                        mTxtReaderView = null;
                    }
                    if (mHandler != null) {
                        mHandler.removeCallbacksAndMessages(null);
                        mHandler = null;
                    }
                    if (mChapterListPop != null) {
                        if (mChapterListPop.isShowing()) {
                            mChapterListPop.dismiss();
                        }
                        mChapterListPop.onDestroy();
                        mChapterListPop = null;
                    }
                    mMenuHolder = null;
                }
            }, 300);

        }
    }
}
