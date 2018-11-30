package xcvf.top.readercore.impl;

import android.graphics.Paint;
import android.text.TextUtils;
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


    private String filterNode(String text) {
        if (TextUtils.isEmpty(text)) {
            return "";
        } else {
            return text.replace("&lt;", "").replace("&amp;", "")
                    .replace("nsp;", "").replace("&gt;", "");
        }
    }

    @Override
    public List<IPage> providerPages(Chapter chapter, ArrayList<String> filelist, int maxWidth, int maxLinesPerPage, Paint paint) {


        ArrayList<IPage> pageList = new ArrayList<>();

        try {
            StringBuilder textBuff = new StringBuilder();
            int filesize = filelist == null ? 0 : filelist.size();
            for (int l = 0; l < filesize; l++) {
                Document document = Jsoup.parse(new File(filelist.get(l)), "gbk");
                Element element = document.getElementById("content");
                int size = element.childNodeSize();
                for (int i = 0; i < size; i++) {
                    Node node = element.childNode(i);
                    if (node instanceof TextNode) {
                        TextNode textNode = (TextNode) node;
                        textBuff.append(filterNode(textNode.getWholeText()));
                    } else if (node instanceof Element) {
                        Element element1 = (Element) node;
                        if ("br".equals(element1.tagName())) {
                            textBuff.append("\n");
                        }
                    }
                }
                deleteEndBr(textBuff);
            }
            String content = textBuff.toString().replace("\r\n", "");
            int chapter_total_length = content.length();
            Page page = new Page();
            page.setChapterid(String.valueOf(chapter.chapterid));
            int pageTotalChars = 0;
            while (content.length() > 0) {
                if (page.getLines().size() >= maxLinesPerPage) {
                    //这页已经满了
                    page.setStartPositionInChapter(chapter_total_length - content.length() - pageTotalChars);
                    page.setPageTotalChars(pageTotalChars);
                    page.setIndex(pageList.size() + 1);
                    pageList.add(page);
                    page = new Page();
                    page.setChapterid(String.valueOf(chapter.chapterid));
                    pageTotalChars = 0;
                }
                Line line = (Line) TextBreakUtil.getLine(content, maxWidth, paint);
                pageTotalChars += line.getChars().size();
                page.addLines(line);
                content = content.substring(line.getChars().size());
            }

            if (page.getLines().size() > 0) {
                page.setStartPositionInChapter(chapter_total_length - pageTotalChars);
                page.setPageTotalChars(pageTotalChars);
                page.setIndex(pageList.size() + 1);
                pageList.add(page);
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

    /**
     * 删除最后面的换行符
     *
     * @param textBuff
     */
    private void deleteEndBr(StringBuilder textBuff) {
        if (textBuff.length() > 0) {
            char end_char = textBuff.charAt(textBuff.length() - 1);
            if (end_char == '\n' ||
                    end_char == '\r' ||
                    end_char == '\t' ||
                    end_char == ' ') {
                textBuff.deleteCharAt(textBuff.length() - 1);
                deleteEndBr(textBuff);
            }
        }
    }
}
