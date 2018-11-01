package xcvf.top.readercore.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by xiaw on 2018/11/1.
 */
@Entity
public class Green {
    String name;

    @Generated(hash = 1386817425)
    public Green(String name) {
        this.name = name;
    }

    @Generated(hash = 1622677047)
    public Green() {
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
