package xcvf.top.readercore.impl;

import android.content.Context;
import android.os.SystemClock;
import android.text.TextUtils;

import com.blankj.utilcode.util.LogUtils;
import com.vector.update_app.utils.Md5Util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import xcvf.top.readercore.utils.Constant;


/**
 * 下载文件
 */
public class ChapterDownloaderQingkan9 extends BaseChapterFileDownloader {


    private ChapterDownloaderQingkan9(String engine) {
        super(engine);
    }

    public static BaseChapterFileDownloader newOne(String engine) {
        return new ChapterDownloaderQingkan9(engine);
    }

    /**
     * 请看9 一个章节会拆分成2个页面
     *
     * @param firstPath
     * @return
     */
    private ArrayList<String> getTotalPage(String firstPath) {
        ArrayList<String> lsit = new ArrayList<>();
        long current = SystemClock.elapsedRealtime();
        try {
            Document document = Jsoup.parse(new File(firstPath), "gbk");
            Elements elements = document.getElementsByAttributeValue("class", "text");
            if (elements != null && elements.size() > 0) {
                Element element = elements.get(0);
                Elements aelements = element.getElementsByTag("a");
                int aSize = aelements.size();
                for (int i = 0; i < aSize; i++) {
                    Element element1 = aelements.get(i);
                    String c_url = element1.attributes().get("href");
                    if (!lsit.contains(c_url) && !TextUtils.isEmpty(c_url)) {
                        lsit.add(c_url);
                    }

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        LogUtils.e("解析章节数耗时 %.2f,", (SystemClock.elapsedRealtime() - current) / 1000.0f);
        return lsit;
    }

    @Override
    public ArrayList<String> download(Context context, String chapter_url) {
        String path = Constant.getCachePath(context, Md5Util.bytes2Hex(chapter_url.getBytes()));
        boolean ok = downloadUrl(chapter_url, path, buildHeader());
        if (ok) {
            ArrayList<String> list = new ArrayList<>();
            list.add(path);
            ArrayList<String> chapter_urls = getTotalPage(path);
            for (String url : chapter_urls) {
                String local = Constant.getCachePath(context, Md5Util.bytes2Hex(url.getBytes()));
                boolean ok1 = downloadUrl(url, local, buildHeader());
                if (ok1) {
                    list.add(local);
                }
            }
            return list;
        }
        return null;
    }
}
