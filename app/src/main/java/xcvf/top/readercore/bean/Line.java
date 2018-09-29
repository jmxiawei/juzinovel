package xcvf.top.readercore.bean;

import java.util.LinkedList;
import java.util.List;

import xcvf.top.readercore.interfaces.IChar;
import xcvf.top.readercore.interfaces.ILine;

/**
 * Created by xiaw on 2018/7/11.
 */
public class Line implements ILine {

    List<IChar> chars = new LinkedList<>();

    @Override
    public void addChar(IChar iChar) {
        chars.add(iChar);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("  ");
        int size = chars.size();
        for (int i = 0; i < size; i++) {
            builder.append(chars.get(i).getData()
            );
        }
        return builder.toString();
    }

    @Override
    public List<IChar> getChars() {
        return chars;
    }
}
