package xcvf.top.readercore.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by xiaw on 2018/11/1.
 */
@Entity
public class TestDemo {
    String name;
    int key;
    @Generated(hash = 231272051)
    public TestDemo(String name, int key) {
        this.name = name;
        this.key = key;
    }
    @Generated(hash = 248571197)
    public TestDemo() {
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getKey() {
        return this.key;
    }
    public void setKey(int key) {
        this.key = key;
    }
}
