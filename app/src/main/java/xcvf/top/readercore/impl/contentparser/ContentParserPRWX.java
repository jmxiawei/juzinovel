package xcvf.top.readercore.impl.contentparser;

import com.blankj.utilcode.util.LogUtils;

import org.jsoup.Jsoup;
import org.jsoup.helper.DataUtil;
import org.jsoup.internal.ConstrainableInputStream;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import xcvf.top.readercore.bean.Chapter;

public class ContentParserPRWX extends DefaulContentParser {


    @Override
    public String parser(Chapter chapter,List<String> filelist) {

        StringBuilder textBuff = new StringBuilder();
        try {
            int filesize = filelist == null ? 0 : filelist.size();
            for (int l = 0; l < filesize; l++) {
                StringBuffer stringBuffer = new StringBuffer();
                stringBuff
                Jsoup.clean(new String(DataUtil.readToByteBuffer(, Whitelist.basic());

                Document document = Jsoup.parse(), "gbk");
                document.cl

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
