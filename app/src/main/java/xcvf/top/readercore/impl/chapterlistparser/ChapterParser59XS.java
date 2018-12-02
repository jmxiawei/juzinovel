package xcvf.top.readercore.impl.chapterlistparser;

import android.content.Context;
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
 * 飘柔文学-章节列表解析
 */
public class ChapterParser59XS extends BaseChapterParser {


    @Override
    public List<Chapter> parser(Context context, Book book, String url) {

        File file = getChapterFile(context, url);

        Document document = null;
        ArrayList<Chapter> pageList = new ArrayList<>();

        try {
            document = Jsoup.parse(file, "gbk");
            Elements divlist = document.getElementsByAttributeValue("class", "chapterlist");
            Element element = null;
            if(divlist !=null && divlist.size()>0){
                 if(divlist.size() ==1){
                     element = divlist.get(0);
                 }else {
                     element = divlist.get(1);
                 }
            }
            if(element == null){
                return  pageList;
            }
            Elements chapterlist = element.getElementsByTag("dd");
            int l = chapterlist.size();
            for (int i = 0; i < l; i++) {
                Chapter chapter = new Chapter();
                Element element1 = chapterlist.get(i);
                String self_page = element1.childNode(0).attributes().get("href");
                if (TextUtils.isEmpty(self_page)
                        || self_page.length() < 6
                        || "#".equals(self_page)) {
                    continue;
                }
                self_page = url + self_page;
                String name = element1.wholeText();
                chapter.setSelf_page(self_page);
                chapter.setChapter_name(name);
                int chapterid = getChapterId(self_page);
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
