package xcvf.top.readercore.impl.chapterlistparser;

import android.content.Context;
import android.text.TextUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import xcvf.top.readercore.bean.Book;
import xcvf.top.readercore.bean.Chapter;

/**
 * 雅文文学-章节列表解析
 */
public class ChapterParserYWWX extends BaseChapterParser {

    @Override
    public List<Chapter> parser(Context context, Book book, String url) {

        File file = getChapterFile(context, url);

        Document document = null;
        ArrayList<Chapter> pageList = new ArrayList<>();

        try {
            document = Jsoup.parse(file, "gbk");
            Elements divlist = document.getElementsByAttributeValue("class", "bookUpdate borderH l");
            Element element = divlist.get(0);
            Elements chapterlist = element.getElementsByTag("li");
            int l = chapterlist.size();
            for (int i = 0; i < l; i++) {
                Chapter chapter = getEmptyChapter(book);
                Element element1 = chapterlist.get(i);
                Node node = element1.childNodeSize() == 0 ?null:element1.childNode(0);
                if(node == null){
                    continue;
                }
                String c_url = node.attributes().get("href");
                if (TextUtils.isEmpty(c_url)) {
                    continue;
                }
                String name = element1.wholeText();
                chapter.setSelf_page(c_url);
                chapter.setChapter_name(name);
                int chapterid = getChapterId(c_url);
                if (chapterid > 0) {
                    chapter.setChapterid(chapterid);
                } else {
                    continue;
                }
                if(checkAddToList(chapter)){
                    pageList.add(chapter);
                }
            }
        } catch (Exception e) {
        }
        return pageList;
    }
}
