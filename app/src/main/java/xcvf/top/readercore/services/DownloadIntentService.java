package xcvf.top.readercore.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import bolts.Continuation;
import bolts.Task;
import xcvf.top.readercore.bean.Chapter;
import xcvf.top.readercore.impl.FileDownloader;
import xcvf.top.readercore.utils.Constant;

/**
 * 下载
 */
public class DownloadIntentService extends IntentService {

    public String PROGRESS = "DownloadIntentService.progress";
    ArrayList<Chapter> chapterList;
    int mCount;
    int finishCount = 0;

    /**
     * 启动一个下载任务
     * @param context
     */
    public static void startDownloadService(Context context,ArrayList<Chapter> chapterList){
        Intent intent = new Intent(context,DownloadIntentService.class);
        intent.putExtra("chapters",chapterList);
        context.startService(intent);
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public DownloadIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable final Intent intent) {

        chapterList = intent.getParcelableArrayListExtra("chapters");
        mCount = chapterList == null ?0:chapterList.size();


        List<Task<String>> taskList = new ArrayList<>();
        for (int i = 0; i <mCount ; i++) {
            final Chapter chapter = chapterList.get(i);
            taskList.add(Task.callInBackground(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    String dest = Constant.getCachePath(getBaseContext(),chapter.self_page);
                    boolean result = FileDownloader.downloadUrl(Constant.buildChapterFilePath(chapter.self_page),dest);
                    return result ? dest:null;
                }
            }));
        }

        Task.whenAnyResult(taskList).continueWith(new Continuation<Task<String>, Object>() {
            @Override
            public Object then(Task<Task<String>> task) throws Exception {
                Task<String> task1 = task.getResult();
                if(task1!=null){
                    String path = task1.getResult();
                    if(!TextUtils.isEmpty(path)){
                        Intent itn = new Intent(PROGRESS);
                        finishCount ++;
                        itn.putExtra("info",finishCount+"/"+mCount);
                        LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(itn);
                    }
                }
                return null;
            }
        },Task.UI_THREAD_EXECUTOR);
    }
}
