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


    public Page() {
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
