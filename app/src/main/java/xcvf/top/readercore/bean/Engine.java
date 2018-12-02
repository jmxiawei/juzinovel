package xcvf.top.readercore.bean;

import xcvf.top.readercore.impl.IChapterContentParser;
import xcvf.top.readercore.interfaces.ChapterFileDownloader;
import xcvf.top.readercore.interfaces.IChapterParser;

public class Engine {
    String domain;
    String name;
    IChapterParser chapterParser;
    ChapterFileDownloader chapterFileDownloader;
    IChapterContentParser chapterContentParser;

    public IChapterContentParser getChapterContentParser() {
        return chapterContentParser;
    }

    public void setChapterContentParser(IChapterContentParser chapterContentParser) {
        this.chapterContentParser = chapterContentParser;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public IChapterParser getChapterParser() {
        return chapterParser;
    }

    public void setChapterParser(IChapterParser chapterParser) {
        this.chapterParser = chapterParser;
    }

    public ChapterFileDownloader getChapterFileDownloader() {
        return chapterFileDownloader;
    }

    public void setChapterFileDownloader(ChapterFileDownloader chapterFileDownloader) {
        this.chapterFileDownloader = chapterFileDownloader;
    }

    public Engine(String domain, String name,
                  IChapterParser chapterParser,
                  ChapterFileDownloader chapterFileDownloader,
                  IChapterContentParser chapterContentParser) {
        this.domain = domain;
        this.name = name;
        this.chapterParser = chapterParser;
        this.chapterFileDownloader = chapterFileDownloader;
        this.chapterContentParser = chapterContentParser;
    }
}
