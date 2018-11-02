package xcvf.top.readercore.impl;

import android.graphics.Paint;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import xcvf.top.readercore.bean.Chapter;
import xcvf.top.readercore.bean.Line;
import xcvf.top.readercore.bean.Page;
import xcvf.top.readercore.interfaces.IPage;
import xcvf.top.readercore.interfaces.IPageProvider;
import xcvf.top.readercore.utils.Constant;
import xcvf.top.readercore.utils.TextBreakUtil;

/**
 * 将一个章节转换为每页的数据
 * Created by xiaw on 2018/9/27.
 */
public class HtmlPageProvider implements IPageProvider {

    public static HtmlPageProvider newInstance() {
        return new HtmlPageProvider();
    }

    @Override
    public List<IPage> providerPages(Chapter chapter, String path, int maxWidth, int maxLinesPerPage, Paint paint) {

        Document document = null;
        ArrayList<IPage> pageList = new ArrayList<>();

        try {
            document = Jsoup.parse(new File(path), "utf-8");
            Element element = document.getElementById("content");
            int size = element.childNodeSize();
            StringBuilder textBuff = new StringBuilder();
            for (int i = 0; i < size; i++) {
                Node node = element.childNode(i);
                if (node instanceof TextNode) {
                    TextNode textNode = (TextNode) node;
                    textBuff.append(textNode.getWholeText());
                } else if (node instanceof Element) {
                    Element element1 = (Element) node;
                    if ("br".equals(element1.tagName())) {
                        textBuff.append("\n");
                    }
                }
            }

            String content = textBuff.toString();
            //LogUtils.e(content);
            Page page = null;
            while (content.length() > 0) {
                Line line = (Line) TextBreakUtil.getLine(content, maxWidth, paint);
                if (page == null) {
                    page = new Page();
                    page.setChapterid(String.valueOf(chapter.chapterid));
                }
                if (page.getLines().size() >= maxLinesPerPage) {
                    //这页已经满了
                    pageList.add(page);
                    page.setIndex(pageList.size());
                    page = new Page();
                    page.setChapterid(String.valueOf(chapter.chapterid));
                }
                page.addLines(line);
                content = content.substring(line.getChars().size());
            }

            if (page != null) {
                pageList.add(page);
                page.setIndex(pageList.size());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        int size = pageList.size();

        for (IPage page : pageList) {
            page.setTotalPage(size);
        }
        return pageList;
    }

}
