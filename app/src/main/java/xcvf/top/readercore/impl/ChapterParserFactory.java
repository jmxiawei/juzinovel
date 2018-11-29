package xcvf.top.readercore.impl;

import xcvf.top.readercore.interfaces.ChapterFileDownloader;
import xcvf.top.readercore.interfaces.IChapterParser;

public class ChapterParserFactory {

    /**
     * @param engine
     * @return
     */
    public static IChapterParser getChapterParser(String engine) {

        switch (engine) {
            case ENGINE.BIQUGE:
                return new ChapterParserBiquge();
            case ENGINE.KANKAN:
                return new ChapterParser7Kankan();
            case ENGINE.QINGKAN9:
            case ENGINE.QINGKAN9_HTTP:
                return new ChapterParserQingkan9();
            default:
                break;
        }
        return null;
    }


    public static ChapterFileDownloader getDownloader(String engine) {
        switch (engine) {
            case ENGINE.BIQUGE:
                return ChapterDownloaderBiquge.newOne(engine);
            case ENGINE.KANKAN:
                return ChapterDownloader7KanKan.newOne(engine);
            case ENGINE.QINGKAN9:
            case ENGINE.QINGKAN9_HTTP:
                return ChapterDownloaderQingkan9.newOne(engine);
            default:
                break;
        }
        return null;
    }

    public static class ENGINE {
        public static final String BIQUGE = "http://www.biquge.com.tw";
        public static final String KANKAN = "http://www.7kankan.com";
        public static final String QINGKAN9 = "https://www.qingkan9.com";
        public static final String QINGKAN9_HTTP = "http://www.qingkan9.com";
    }


    public static final String getSourceName(String domain){
        if(ENGINE.BIQUGE.equals(domain)){
            return "笔趣阁";
        }else  if(ENGINE.KANKAN.equals(domain)){
            return "7看看";
        }else {
            return "请看网";
        }
    }

}
