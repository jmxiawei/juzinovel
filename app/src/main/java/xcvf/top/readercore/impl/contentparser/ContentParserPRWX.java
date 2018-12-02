package xcvf.top.readercore.impl.contentparser;

import com.blankj.utilcode.util.LogUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.util.List;

import xcvf.top.readercore.bean.Chapter;

public class ContentParserPRWX extends DefaulContentParser {


    @Override
    public String parser(Chapter chapter,List<String> filelist) {

        StringBuilder textBuff = new StringBuilder();
        try {
            int filesize = filelist == null ? 0 : filelist.size();
            for (int l = 0; l < filesize; l++) {
                Document document = Jsoup.parse(new File(filelist.get(l)), "gbk");
                Elements element = document.getAllElements();
                int size = element.size();
                boolean start = false;
                for (int i = 0; i < size ; i++) {
                    Element element1 = element.get(i);
                    if(element1.tagName().equals("table")){
                        start = true;
                    }
                    LogUtils.e(element1.wholeText());
                }
//                int size = element.childNodeSize();
//                for (int i = 0; i < size; i++) {
//                    Node node = element.childNode(i);
//                    if (node instanceof TextNode) {
//                        TextNode textNode = (TextNode) node;
//                        textBuff.append(filterNode(textNode.getWholeText()));
//                    } else if (node instanceof Element) {
//                        Element element1 = (Element) node;
//                        if ("br".equals(element1.tagName())) {
//                            textBuff.append("\n");
//                        }
//                    }
//                }
                deleteEndBr(textBuff);
            }
        }catch (Exception e) {
        }
        return  textBuff.toString().replace("\r\n", "");
    }



}
