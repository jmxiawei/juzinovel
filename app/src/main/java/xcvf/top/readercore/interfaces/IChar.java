package xcvf.top.readercore.interfaces;

/**字符
 * Created by xiaw on 2018/7/11.
 */
public interface IChar {

    int TYPE_TXT  = 0;
    int TYPE_EN = 1;
    int TYPE_NUM = 2;
    int getType();

    void setType(int type);

    char getData();

}
