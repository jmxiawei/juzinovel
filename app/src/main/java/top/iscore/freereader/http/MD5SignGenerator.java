package top.iscore.freereader.http;

import com.blankj.utilcode.util.EncryptUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 签名加密
 */
public class MD5SignGenerator implements SignGenerator {
    @Override
    public String sign(HashMap<String, String> params) {
        ArrayList<String> keys = new ArrayList<>();
        for (Map.Entry<String, String> p :
                params.entrySet()) {
            keys.add(p.getKey());
        }
        Collections.sort(keys);
        StringBuilder stringBuilder = new StringBuilder();
        for (String k :
                keys) {
            stringBuilder.append(params.get(k));
        }
        return EncryptUtils.encryptMD5ToString(stringBuilder.toString()).toLowerCase();
    }
}
