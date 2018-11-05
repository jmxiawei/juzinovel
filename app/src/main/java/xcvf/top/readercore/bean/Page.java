package xcvf.top.readercore.bean;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import xcvf.top.readercore.interfaces.ILine;
import xcvf.top.readercore.interfaces.IPage;

/**
 * Created by xiaw on 2018/7/11.
 */
public class Page implements IPage {

    //这一页所有的行
    List<ILine> lines = new ArrayList<>();
    //当前章节的第几页
    int index;

    int totalPage;

    int pageTotalChars;
    //本页起始的字符在本章节中的位置
    int startPositionInChapter;

    public String chapterid;
    public Page() {
    }

    public int getPageTotalChars() {
        return pageTotalChars;
    }

    public Page setPageTotalChars(int pageTotalChars) {
        this.pageTotalChars = pageTotalChars;
        return this;
    }

    public Page setLines(List<ILine> lines) {
        this.lines = lines;
        return this;
    }

    public int getStartPositionInChapter() {
        return startPositionInChapter;
    }

    public Page setStartPositionInChapter(int startPositionInChapter) {
        this.startPositionInChapter = startPositionInChapter;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Page page = (Page) o;

        if (index != page.index) return false;
        return chapterid != null ? chapterid.equals(page.chapterid) : page.chapterid == null;
    }

    @Override
    public int hashCode() {
        int result = index;
        result = 31 * result + (chapterid != null ? chapterid.hashCode() : 0);
        return result;
    }

    public String getChapterid() {
        return chapterid;
    }

    public Page setChapterid(String chapterid) {
        this.chapterid = chapterid;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        int size = lines.size();
        for (int i = 0; i < size; i++) {
            stringBuilder.append(lines.get(i).toString());
        }


        return stringBuilder.toString();
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public int getTotalPage() {
        return  this.totalPage;
    }

    @Override
    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    @Override
    public void addLines(ILine line) {
        if (lines == null) {
            lines = new LinkedList<>();
        }
        lines.add(line);
    }

    @Override
    public List<ILine> getLines() {
        return lines;
    }
}
