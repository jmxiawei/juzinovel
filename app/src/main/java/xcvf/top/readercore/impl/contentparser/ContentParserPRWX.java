package xcvf.top.readercore.impl.contentparser;


import org.jsoup.Jsoup;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;


import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import xcvf.top.readercore.bean.Chapter;

public class ContentParserPRWX extends DefaulContentParser {

    static Pattern PATTERN = Pattern.compile("(\r?\n(\\s*\r?\n){2,})");
    @Override
    public String parser(Chapter chapter, List<String> filelist) {

        StringBuilder textBuff = new StringBuilder();
        try {
            int filesize = filelist == null ? 0 : filelist.size();
            for (int l = 0; l < filesize; l++) {

                String clean = Jsoup.clean(
                        new String(ConstrainableInputStream.wrap(
                                new FileInputStream(new File(filelist.get(l))),
                                32768, 0).readToByteBuffer(0).array(), "GBK")
                        , Whitelist.basic());
                Document document = Jsoup.parse(clean);
                Elements elements = document.getElementsByTag("body");
                if (elements != null && elements.size() > 0) {
                    Element body = elements.get(0);
                    boolean start = false;
                    int nodelistsize = body.childNodes().size();
                    for (int i = 0; i < nodelistsize; i++) {
                        Node node = body.childNode(i);
                        if (node instanceof TextNode && start) {
                            TextNode textNode = (TextNode) node;
                            textBuff.append(filterNode(textNode.getWholeText()));
                        } else if (node instanceof Element) {
                            Element element1 = (Element) node;
                            if ("br".equals(element1.tagName()) && start) {
                                textBuff.append("\n");
                            } else if ("a".equals(element1.tagName()) && "返回书页".equals(element1.text())) {
                                start = true;
                                //跳过后面的一个空格
                                i++;
                            }else if(start && "a".equals(element1.tagName())
                                    && element1.wholeText().contains("上一章")){
                                break;
                            }
                        }
                    }
                }
                deleteEndBr(textBuff);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return replaceLineBlanks(textBuff.toString().replace("\r\n", ""));
    }

    /**
     * 将字符串中的连续的多个换行缩减成一个换行
     * @param str  要处理的内容
     * @return	返回的结果
     */
    public static String replaceLineBlanks(String str) {
        String result = "";
        if (str != null) {
            Matcher m = PATTERN.matcher(str);
            result = m.replaceAll("\n\n");
        }
        return result;
    }


}
