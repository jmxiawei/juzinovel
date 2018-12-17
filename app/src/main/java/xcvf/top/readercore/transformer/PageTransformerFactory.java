package xcvf.top.readercore.transformer;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.SpanUtils;
import com.blankj.utilcode.util.Utils;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import xcvf.top.readercore.bean.AnimItem;

public class PageTransformerFactory {

    private static ConcurrentHashMap<String,AnimItem>  cacheAnimList = new ConcurrentHashMap<>();
    private static AnimItem defaultAnim = null;
    static {

        AnimItem animItem = new AnimItem("hengxiang","横向",new DefaultTransformer());

        cacheAnimList.put(animItem.key,animItem);

        AnimItem animItem1 = new AnimItem("cengdie","层叠",new StackTransformer());
        defaultAnim = animItem1;
        cacheAnimList.put(animItem1.key,animItem1);
    }


    public static ArrayList<AnimItem> getList(){
        return new ArrayList<>(cacheAnimList.values());
    }


    public static AnimItem get(){
      return  cacheAnimList.get(SPUtils.getInstance().getString("anim",defaultAnim.key));
    }


    public static void  put(AnimItem animItem){
        SPUtils.getInstance().put("anim",animItem.key);
    }
}
