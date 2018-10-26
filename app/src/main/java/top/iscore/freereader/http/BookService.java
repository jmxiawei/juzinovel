package top.iscore.freereader.http;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import xcvf.top.readercore.bean.Book;
import xcvf.top.readercore.bean.Chapter;

/**结尾需要带/
 * Created by xiaw on 2018/9/18.
 */

public interface BookService {


    /**
     * 加载书籍章节
     * @param service
     * @param exterBookId
     * @return
     */
    @FormUrlEncoded
    @POST("reader/public/v1/")
    Call<BaseModel<ArrayList<Chapter>>> getChapterList(@Field("service") String service
            ,@Field("extern_bookid") String exterBookId
            ,@Field("startid") int startid);

    /**
     * 加载书籍章节
     * @param service
     * @param exterBookId
     * @return
     */
    @FormUrlEncoded
    @POST("reader/public/v1/")
    Call<BaseModel<ArrayList<Chapter>>> getOneChapter(@Field("service") String service
            ,@Field("extern_bookid") String exterBookId
            ,@Field("startid") int startid);


    /**
     * 加载书架
     * @param service
     * @param userid
     * @return
     */
    @FormUrlEncoded
    @POST("reader/public/v1/")
    Call<BaseModel<ArrayList<Book>>> getBookShelf(@Field("service") String service
            , @Field("userid") String userid
            , @Field("page") int page);




}
