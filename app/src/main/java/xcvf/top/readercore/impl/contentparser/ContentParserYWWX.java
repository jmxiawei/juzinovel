package xcvf.top.readercore.impl.contentparser;

import android.text.TextUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.io.File;
import java.util.List;

import xcvf.top.readercore.bean.Chapter;
import xcvf.top.readercore.impl.IChapterContentParser;

public class ContentParserYWWX implements IChapterContentParser {


    protected String filterNode(String text) {
        if (TextUtils.isEmpty(text)) {
            return "";
        } else {
            return text.replace("&lt;", "").replace("&amp;", "")
                    .replace("nsp;", "").replace("&gt;", "");
        }
    }

    @Override
    public String parser(Chapter chapter,List<String> filelist) {

        StringBuilder textBuff = new StringBuilder();
        try {
            int filesize = filelist == null ? 0 : filelist.size();
            for (int l = 0; l < filesize; l++) {
                Document document = Jsoup.parse(new File(filelist.get(l)), "gbk");
                Element element = document.getElementById("content");
                if (element == null) {
                    //ywwx
                    Elements elements = document.getElementsByAttributeValue("class","txtc");
                    if(elements!=null && elements.size()>0){
                        element = elements.get(0);
                    }
                }
                if(element == null){
                    return  null;
                }
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
        }catch (Exception e) {
        }
        return  textBuff.toString().replace("\r\n", "");
    }



    /**
     * 删除最后面的换行符
     *
     * @param textBuff
     */
    protected void deleteEndBr(StringBuilder textBuff) {
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
