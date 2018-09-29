package xcvf.top.readercore.interfaces;

/**下载监听
 * Created by xiaw on 2018/9/28.
 */
public interface DownloadListener {

    //status 0 成功  1 失败
    void onDownload(int status,String path);
}
