package xcvf.top.readercore.bean;

import xcvf.top.readercore.interfaces.IChar;

/**
 * Created by xiaw on 2018/7/11.
 */

public class TxtChar  {

    char data;
    int type;
    float width;

    public float getWidth() {
        return width;
    }

    public TxtChar(char data) {
        this.data = data;
    }

    public TxtChar setWidth(float width) {
        this.width = width;
        return this;
    }


    public int getType() {
        return type;
    }


    public void setType(int type) {
        this.type = type;
    }

    public char getData() {
        return data;
    }
}
