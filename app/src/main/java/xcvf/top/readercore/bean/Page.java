package xcvf.top.readercore.bean;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * Created by xiaw on 2018/7/11.
 */
public class Page {
    public static final int OK_PAGE = 0;
    public static final int LOADING_PAGE = -1;
    public static final int ERROR_PAGE = -2;
    //这一页所有的行
    List<Line> lines = new ArrayList<>();
    //当前章节的第几页
    int index;
    int status = OK_PAGE;
    int totalPage;
    String time;
    int pageTotalChars;
    //本页起始的字符在本章节中的位置
    int startPositionInChapter;

    public int chapterid;

    public Page() {
    }

    public int getStatus() {
        return status;
    }

    public Page setStatus(int status) {
        this.status = status;
        return this;
    }

    public String getTime() {
        return time;
    }

    public Page setTime(String time) {
        this.time = time;
        return this;
    }

    public int getPageTotalChars() {
        return pageTotalChars;
    }

    public Page setPageTotalChars(int pageTotalChars) {
        this.pageTotalChars = pageTotalChars;
        return this;
    }

    public Page setLines(List<Line> lines) {
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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Page page = (Page) o;
        return index == page.index &&
                chapterid == page.chapterid;
    }

    @Override
    public int hashCode() {

        return Objects.hash(index, chapterid);
    }

    public int getChapterid() {
        return chapterid;
    }

    public Page setChapterid(int chapterid) {
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


    public int getIndex() {
        return index;
    }


    public int getTotalPage() {
        return this.totalPage;
    }


    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }


    public void addLines(Line line) {
        if (lines == null) {
            lines = new LinkedList<>();
        }
        lines.add(line);
    }


    public List<Line> getLines() {
        return lines;
    }
}
