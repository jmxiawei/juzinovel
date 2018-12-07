package xcvf.top.readercore.bean;

import java.util.LinkedList;
import java.util.List;

import xcvf.top.readercore.interfaces.IChar;

/**
 * Created by xiaw on 2018/7/11.
 */
public class Line {

    List<TxtChar> chars = new LinkedList<>();

    public void addChar(TxtChar iChar) {
        chars.add(iChar);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        int size = chars.size();
        for (int i = 0; i < size; i++) {
            builder.append(chars.get(i).getData());
        }
        return builder.toString();
    }

    public List<TxtChar> getChars() {
        return chars;
    }
}
