package xcvf.top.readercore.bean;

/**
 * 模式
 * Created by xiaw on 2018/10/29.
 */
public enum Mode {

    DayMode(0),//日间模式
    NightMode(1),;//夜间模式

    int value;

    Mode(int value) {
        this.value = value;
    }

    public static Mode from(int value) {
        return value == 0 ? DayMode : NightMode;
    }

    public int toInt() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(this.value);
    }
}
