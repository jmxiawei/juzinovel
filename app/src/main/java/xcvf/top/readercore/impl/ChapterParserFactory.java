package xcvf.top.readercore.impl;

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
                return new ChapterParserQingkan9();
            default:
                break;
        }
        return null;
    }


    public static class ENGINE {
        public static final String BIQUGE = "http://www.biquge.com.tw";
        public static final String KANKAN = "http://www.7kankan.com";
        public static final String QINGKAN9 = "http://www.qingkan9.com";

    }

}
