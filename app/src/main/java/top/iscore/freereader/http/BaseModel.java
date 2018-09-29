package top.iscore.freereader.http;

/**
 * Created by xiaw on 2018/9/18.
 */

public class BaseModel<T> {

    int code;
    T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
