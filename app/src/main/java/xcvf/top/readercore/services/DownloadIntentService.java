package xcvf.top.readercore.services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.LocalBroadcastManager;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import bolts.Task;
import top.iscore.freereader.R;
import top.iscore.freereader.mvp.presenters.BookReadPresenter;
import top.iscore.freereader.mvp.presenters.BookShelfPresenter;
import xcvf.top.readercore.bean.Book;
import xcvf.top.readercore.bean.Chapter;
import xcvf.top.readercore.daos.DBManager;
import xcvf.top.readercore.daos.DaoSession;
import xcvf.top.readercore.impl.ChapterParserFactory;
import xcvf.top.readercore.impl.FileDownloader;
import xcvf.top.readercore.impl.downloader.BaseChapterFileDownloader;
import xcvf.top.readercore.impl.path.PathGeneratorFactory;
import xcvf.top.readercore.interfaces.ChapterFileDownloader;
import xcvf.top.readercore.interfaces.IChapterParser;
import xcvf.top.readercore.utils.Constant;

/**
 * 下载
 */
public class DownloadIntentService extends IntentService {

    private static final int NOTIFICATION_ID_03 = 3;
    public static String PROGRESS = "DownloadIntentService.progress";
    ArrayList<Chapter> chapterList;
    int mCount;
    int finishCount = 0;
    Chapter chapter;
    Book mBook;
    long startId;
    BookReadPresenter bookReadPresenter;
    //Notification.Builder mNotificationBuilder;
    //NotificationManager mNotificationManager;

    /**
     * 启动一个下载任务
     *
     * @param context
     */
    public static void startDownloadService(Context context, Book book, Chapter chapter, ArrayList<Chapter> chapterList) {
        Intent intent = new Intent(context, DownloadIntentService.class);
        intent.putExtra("chapters", chapterList);
        intent.putExtra("chapter", chapter);
        intent.putExtra("book", book);
        context.startService(intent);
        ToastUtils.showShort(book.name + "已加入缓存列表！");
    }


    /**
     * 启动一个下载全本
     *
     * @param context
     */
    public static void startDownloadService(Context context, Book book, long startId) {
        Intent intent = new Intent(context, DownloadIntentService.class);
        intent.putExtra("book", book);
        intent.putExtra("startId", startId);
        context.startService(intent);
        ToastUtils.showShort(book.name + "已加入缓存列表！");
    }


    public DownloadIntentService() {
        super("DownloadIntentService");
    }


    @Override
    protected void onHandleIntent(@Nullable final Intent intent) {
        if (intent == null) {
            return;
        }
        //mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        //showProgressNotification02();
        LogUtils.e("onHandleIntent" + hashCode());
        mBook = intent.getParcelableExtra("book");
        startId = intent.getLongExtra("startId", 0);
        chapterList = intent.getParcelableArrayListExtra("chapters");
        chapter = intent.getParcelableExtra("chapter");
        if (mBook != null) {
            bookReadPresenter = new BookReadPresenter();
            if (chapterList == null || chapterList.size() == 0) {
                chapterList = bookReadPresenter.loadChaptersSync(mBook, startId);
                if (chapterList == null || chapterList.size() == 0) {
                    IChapterParser chapterParser = ChapterParserFactory.getChapterParser(mBook.engine_domain);
                    if (chapterParser != null) {
                        chapterList = (ArrayList<Chapter>) chapterParser.parser(this, mBook, mBook.read_url);
                    }
                }
            }
            if (chapter == null) {
                if (chapterList != null && chapterList.size() > 0) {
                    chapter = chapterList.get(0);
                }
            }
        }

        DaoSession session = DBManager.getDaoMaster().newSession();
        mCount = chapterList == null ? 0 : chapterList.size();
        for (int i = 0; i < mCount; i++) {
            final Chapter chapter = chapterList.get(i);
            List<String> files = null;
            ChapterFileDownloader downloader = ChapterParserFactory.getDownloader(chapter.engine_domain);
            if (downloader != null && !BaseChapterFileDownloader.isInTask(chapter.getFullPath())) {
                files = downloader.download(getApplicationContext(), chapter.getFullPath());
            }
            if (files != null && files.size() > 0) {
                chapter.setIs_download(true);
                session.getChapterDao().insertOrReplace(chapter);
                Intent itn = new Intent(PROGRESS);
                finishCount++;
                String msg = "正在下载: " + finishCount + "/" + mCount;
                LogUtils.e(msg);
//                mNotificationBuilder.setProgress(100,i,false);
//                mNotificationBuilder.setContentText(String.format("已下载%d%%",i));
//                mNotificationManager.notify(NOTIFICATION_ID_03, mNotificationBuilder.build());

                itn.putExtra("info", msg);
                itn.putExtra("bookid", chapter.extern_bookid);
                itn.putExtra("finish", finishCount == mCount ? 1 : 0);
                LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(itn);
            }

        }
    }


//
//    // 通知渠道组的id.
//    private static String group_download_id = "group_download_id";
//    //用户可见的通知渠道组名称
//    private static String group_download_name = "My group_download_name01";
//
//    /**
//     * 创建通知渠道
//     * @param channel_id 渠道id
//     * @param channel_name 渠道名称
//     * @param channel_desc 渠道描述
//     * @param importance 渠道优先级
//     * @param group_id 渠道组，若没有渠道组，则传null
//     */
//    @RequiresApi(api = 26)
//    private void createNotificationChannel(Context context, String channel_id, String channel_name, String channel_desc, int importance, String group_id) {
//        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        //配置通知渠道id,渠道名称（用户可以看到），渠道优先级
//        NotificationChannel mChannel = new NotificationChannel(channel_id, channel_name, importance);
//        //配置通知渠道的描述
//        mChannel.setDescription(channel_desc);
//        //配置通知出现时的闪灯（如果 android 设备支持的话）
//        //mChannel.enableLights(true);
//        //mChannel.setLightColor(Color.RED);
//        //配置通知出现时的震动（如果 android 设备支持的话）
//        //mChannel.enableVibration(true);
//        //mChannel.setVibrationPattern(new long[]{100, 200, 100, 200});
//        //配置渠道组
//        if (group_id != null) {
//            mChannel.setGroup(group_id);//设置渠道组
//        }
//        //在NotificationManager中创建该通知渠道
//        manager.createNotificationChannel(mChannel);
//    }
//
//    @RequiresApi(api = 26)
//    private void createNotificationChannelGroup() {
//        NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
//        manager.createNotificationChannelGroup(new NotificationChannelGroup(group_download_id, group_download_name));
//    }
//
//
//    /**
//     * 带有确定进度条的通知
//     */
//    private void showProgressNotification02() {
//
//
//        createNotificationChannelGroup();//创建渠道组
//        createNotificationChannel(group_download_id, group_download_name, "渠道描述", NotificationManager.IMPORTANCE_HIGH, group_id);
//
//        mNotificationBuilder= new Notification.Builder(getApplicationContext(), group_download_id)
//                .setContentTitle("带有进度条的通知")
//                .setContentText("正在等待下载")
//                .setWhen(System.currentTimeMillis())
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_round))
//                .setProgress(100, 0, false)
//                .setAutoCancel(true);
//        mNotificationManager.notify(1, mNotificationBuilder.build());
//    }
}
