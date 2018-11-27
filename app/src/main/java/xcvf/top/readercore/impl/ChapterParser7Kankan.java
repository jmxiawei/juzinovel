package xcvf.top.readercore.impl;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import xcvf.top.readercore.bean.Book;
import xcvf.top.readercore.bean.Chapter;

/**
 * 章节一个小时下载一次
 */
public class ChapterParser7Kankan extends BaseChapterParser {


    private int getChapterId(String url) {
        if (TextUtils.isEmpty(url)) {
            return 0;
        } else {
            String[] strings = url.split("/");
            if (strings.length > 0) {
                try {
                    return Integer.parseInt(strings[strings.length - 1]);
                } catch (Exception e) {
                    return 0;
                }
            }
        }
        return 0;
    }

    @Override
    public List<Chapter> parser(Context context, Book book, String url) {

        File file = getChapterFile(context, url);

        Document document = null;
        ArrayList<Chapter> pageList = new ArrayList<>();

        try {
            document = Jsoup.parse(file, "gbk");
            Elements divlist = document.getElementsByAttributeValue("class", "uclist");
            Element element = divlist.get(0);
            Elements chapterlist = element.getElementsByTag("dd");
            int l = chapterlist.size();
            for (int i = 0; i < l; i++) {
                Chapter chapter = new Chapter();
                Element element1 = chapterlist.get(i);
                String c_url = element1.childNode(0).attributes().get("href");
                String name = element1.wholeText();
                chapter.setSelf_page(c_url);
                chapter.setChapter_name(name);
                int chapterid = getChapterId(c_url);
                if (chapterid > 0) {
                    chapter.setChapterid(chapterid);
                } else {
                    continue;
                }
                chapter.setEngine_domain(book.engine_domain);
                chapter.setExtern_bookid(book.extern_bookid);
                pageList.add(chapter);
            }
        } catch (Exception e) {
        }
        return pageList;
    }
}
