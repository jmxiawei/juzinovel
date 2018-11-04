package xcvf.top.readercore.bean;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class Config {

    String name;
    String value;
    @Generated(hash = 364718396)
    public Config(String name, String value) {
        this.name = name;
        this.value = value;
    }
    @Generated(hash = 589037648)
    public Config() {
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getValue() {
        return this.value;
    }
    public void setValue(String value) {
        this.value = value;
    }

}
