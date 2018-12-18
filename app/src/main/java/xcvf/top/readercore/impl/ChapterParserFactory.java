package xcvf.top.readercore.impl;

import java.util.HashMap;

import xcvf.top.readercore.bean.Engine;
import xcvf.top.readercore.impl.chapterlistparser.ChapterParser59XS;
import xcvf.top.readercore.impl.chapterlistparser.ChapterParser7Kankan;
import xcvf.top.readercore.impl.chapterlistparser.ChapterParserPRWX;
import xcvf.top.readercore.impl.chapterlistparser.ChapterParserQingkan9;
import xcvf.top.readercore.impl.chapterlistparser.ChapterParserYWWX;
import xcvf.top.readercore.impl.contentparser.ContentParser59XS;
import xcvf.top.readercore.impl.contentparser.ContentParserPRWX;
import xcvf.top.readercore.impl.contentparser.ContentParserYWWX;
import xcvf.top.readercore.impl.contentparser.DefaulContentParser;
import xcvf.top.readercore.impl.downloader.ChapterDownloader59XS;
import xcvf.top.readercore.impl.downloader.ChapterDownloader7KanKan;
import xcvf.top.readercore.impl.downloader.ChapterDownloaderPRWX;
import xcvf.top.readercore.impl.downloader.ChapterDownloaderQingkan9;
import xcvf.top.readercore.interfaces.ChapterFileDownloader;
import xcvf.top.readercore.interfaces.IChapterContentParser;
import xcvf.top.readercore.interfaces.IChapterParser;

public class ChapterParserFactory {


    private final  static HashMap<String,Engine> engineHashMap = new HashMap<>();

    static {
        engineHashMap.put(ENGINE.KANKAN,new Engine(ENGINE.KANKAN,
                "7看看",new ChapterParser7Kankan(),
                ChapterDownloader7KanKan.newOne(ENGINE.KANKAN),new DefaulContentParser()));

        engineHashMap.put(ENGINE.QINGKAN9,new Engine(ENGINE.QINGKAN9,
                "请看网",new ChapterParserQingkan9(),
                ChapterDownloaderQingkan9.newOne(ENGINE.QINGKAN9),new DefaulContentParser()));

        engineHashMap.put(ENGINE.PRWX,new Engine(ENGINE.PRWX,
                "飘柔文学",new ChapterParserPRWX(),
                ChapterDownloaderPRWX.newOne(ENGINE.PRWX),new ContentParserPRWX()));

        engineHashMap.put(ENGINE.XS59,new Engine(ENGINE.XS59,
                "59小说",new ChapterParser59XS(),
                ChapterDownloader59XS.newOne(ENGINE.XS59),new ContentParser59XS()));

        engineHashMap.put(ENGINE.YWWX,new Engine(ENGINE.YWWX,
                "雅文文学",new ChapterParserYWWX(),
                ChapterDownloaderPRWX.newOne(ENGINE.YWWX),new ContentParserYWWX()));

    }

    /**
     * @param engine
     * @return
     */
    public static IChapterParser getChapterParser(String engine) {

        Engine e = engineHashMap.get(engine);
        if(e!=null){
            return e.getChapterParser();
        }
        return null;
    }

    /**
     * 解析页面文字内容
     * @param engine
     * @return
     */
    public static IChapterContentParser getContentParser(String engine) {
        Engine e = engineHashMap.get(engine);
        if(e!=null){
            return e.getChapterContentParser();
        }
        return null;
    }

    public static ChapterFileDownloader getDownloader(String engine) {
        Engine e = engineHashMap.get(engine);
        if(e!=null){
            return e.getChapterFileDownloader();
        }
        return null;
    }


    public static class ENGINE {
        public static final String XS59 = "http://www.59xs.com";
        public static final String YWWX = "https://www.ywwx.com";
        public static final String KANKAN = "http://www.7kankan.com";
        public static final String PRWX = "https://www.prwx.com";
        public static final String QINGKAN9 = "https://www.qingkan9.com";
    }


    public static final String getSourceName(String domain){
        Engine e = engineHashMap.get(domain);
        if(e!=null){
            return e.getName();
        }
        return null;
    }

}
