package top.iscore.freereader.http;

import java.util.HashMap;

public interface SignGenerator {

    /**
     * 参数加密
     *
     * @param params
     * @return
     */
     String sign(HashMap<String, String> params);

}
