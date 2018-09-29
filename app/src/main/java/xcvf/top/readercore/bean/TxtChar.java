package xcvf.top.readercore.bean;

import xcvf.top.readercore.interfaces.IChar;

/**
 * Created by xiaw on 2018/7/11.
 */

public class TxtChar implements IChar {

    char data;
    int type;

    public TxtChar(char data) {
        this.data = data;
    }


    @Override
    public int getType() {
        return type;
    }

    @Override
    public void setType(int type) {
        this.type = type;
    }
    @Override
    public char getData() {
        return data;
    }
}
