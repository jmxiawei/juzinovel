package xcvf.top.readercore.views;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import top.iscore.freereader.R;
import xcvf.top.readercore.bean.Book;
import xcvf.top.readercore.bean.Mode;
import xcvf.top.readercore.bean.SettingAction;
import xcvf.top.readercore.services.DownloadIntentService;

/**
 * 设置界面
 */
public class ReaderSettingView extends FrameLayout {

    //返回
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_book)
    TextView tvBook;
    @BindView(R.id.ll_setting_top)
    LinearLayout llSettingTop;
    @BindView(R.id.iv_mode)
    ImageView ivMode;
    @BindView(R.id.tv_mode)
    TextView tvMode;
    @BindView(R.id.iv_font)
    ImageView ivFont;
    @BindView(R.id.tv_font)
    TextView tvFont;
    @BindView(R.id.iv_cache)
    ImageView ivCache;
    @BindView(R.id.tv_cache)
    TextView tvCache;
    @BindView(R.id.iv_list)
    ImageView ivList;
    @BindView(R.id.tv_list)
    TextView tvList;
    @BindView(R.id.ll_setting_bottom)
    LinearLayout llSettingBottom;

    Book mBook;

    @BindView(R.id.ll_mode)
    LinearLayout llMode;
    @BindView(R.id.ll_font)
    LinearLayout llFont;
    @BindView(R.id.ll_cache)
    LinearLayout llCache;
    @BindView(R.id.ll_chapter_list)
    LinearLayout llChapterList;

    ISettingListener settingListener;
    @BindView(R.id.tv_download)
    TextView tvDownload;

    public void setSettingListener(ISettingListener settingListener) {
        this.settingListener = settingListener;
    }

    public ReaderSettingView(@NonNull Context context) {
        this(context, null);
    }

    public ReaderSettingView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ReaderSettingView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.layout_read_setting, this, true);
        ButterKnife.bind(this, this);
        initView();
    }


    public void ActivityonCreate(Activity activity) {
        IntentFilter filter = new IntentFilter(DownloadIntentService.PROGRESS);
        LocalBroadcastManager.getInstance(activity).registerReceiver(downloadReceive, filter);
    }

    public void ActivityonDestory(Activity activity) {
        LocalBroadcastManager.getInstance(activity).unregisterReceiver(downloadReceive);
    }


    /**
     * 接收下载进度
     */
    BroadcastReceiver downloadReceive = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String bookid = intent.getStringExtra("bookid");
            final String info = intent.getStringExtra("info");
            final int finish = intent.getIntExtra("finish", 0);
            LogUtils.e("bookid="+bookid+",info="+info+",finish="+finish+",mybook="+mBook.extern_bookid);
            if (mBook.extern_bookid.equals(bookid)) {
                post(new Runnable() {
                    @Override
                    public void run() {
                        if (finish == 1) {
                            tvDownload.setVisibility(GONE);
                        } else {
                            tvDownload.setVisibility(VISIBLE);
                        }
                        tvDownload.setText(info);
                    }
                });
            }
        }
    };

    public Book getBook() {
        return mBook;
    }

    public void setBook(Book mBook) {
        this.mBook = mBook;
        tvBook.setText(mBook.name);
    }

    private void initView() {

    }

    /**
     * 要切换成哪个模式
     *
     * @param mode
     */
    public void changeMode(Mode mode) {
        if (mode == Mode.DayMode) {
            ivMode.setImageResource(R.mipmap.ic_mode_day);
            tvMode.setText("日间");
        } else {
            ivMode.setImageResource(R.mipmap.ic_mode_night);
            tvMode.setText("夜间");
        }
    }

    @OnClick({R.id.iv_back, R.id.ll_mode, R.id.ll_font, R.id.ll_cache, R.id.ll_chapter_list})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                if (settingListener != null) {
                    settingListener.onSettingChanged(SettingAction.ACTION_BACK);
                }
                break;
            case R.id.ll_mode:
                if (settingListener != null) {
                    settingListener.onSettingChanged(SettingAction.ACTION_MODE);
                }
                break;
            case R.id.ll_font:
                if (settingListener != null) {
                    settingListener.onSettingChanged(SettingAction.ACTION_FONT);
                }
                break;
            case R.id.ll_cache:
                if (settingListener != null) {
                    settingListener.onSettingChanged(SettingAction.ACTION_CACHE);
                }
                break;
            case R.id.ll_chapter_list:
                if (settingListener != null) {
                    settingListener.onSettingChanged(SettingAction.ACTION_CHAPTER);
                }
                break;
            default:
                break;
        }
    }


    public interface ISettingListener {
        void onSettingChanged(SettingAction action);
    }
}
