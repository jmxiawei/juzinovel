package top.iscore.freereader.http;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import xcvf.top.readercore.bean.Book;
import xcvf.top.readercore.bean.Category;
import xcvf.top.readercore.bean.Chapter;

/**
 * 结尾需要带/
 * Created by xiaw on 2018/9/18.
 */

public interface BookService {


    /**
     * 加载书籍章节
     *
     * @param service
     * @param exterBookId
     * @return
     */
    @FormUrlEncoded
    @POST("reader/public/v1/")
    Call<BaseModel<ArrayList<Chapter>>> getChapterList(@Field("service") String service
            , @Field("extern_bookid") String exterBookId
            , @Field("startid") int startid);

    /**
     * 加载书籍章节
     *
     * @param service
     * @param chapterid
     * @param type      0本章节,1上一章节，2下一章节
     * @return
     */
    @FormUrlEncoded
    @POST("reader/public/v1/")
    Call<BaseModel<Chapter>> getOneChapter(@Field("service") String service
            , @Field("extern_bookid") String extern_bookid
            , @Field("chapterid") String chapterid
            , @Field("type") int type);


    /**
     * 检查更新
     *
     * @param service
     * @param vcode 版本号
     * @return
     */
    @FormUrlEncoded
    @POST("reader/public/v1/")
    Call<BaseModel<String>> update(@Field("service") String service
            , @Field("versionCode") String vcode);


    /**
     * 加载书架
     *
     * @param service
     * @param userid
     * @return
     */
    @FormUrlEncoded
    @POST("reader/public/v1/")
    Call<BaseModel<ArrayList<Book>>> getBookShelf(@Field("service") String service
            , @Field("userid") String userid);

    /**
     * 新增书架
     *
     * @return
     */
    @FormUrlEncoded
    @POST("reader/public/v1/")
    Call<BaseModel<Book>> addBookShelf(@Field("service") String service
            , @Field("userid") String userid
            , @Field("extern_bookid") String extern_bookid);

    /**
     * 删除书架
     *
     * @return
     */
    @FormUrlEncoded
    @POST("reader/public/v1/")
    Call<BaseModel<Book>> deleteBookShelf(@Field("service") String service
            , @Field("userid") String userid, @Field("shelfid") String shelfid);


    /**
     * 搜索书籍
     *
     * @param service
     * @param keyword
     * @return
     */
    @FormUrlEncoded
    @POST("reader/public/v1/")
    Call<BaseModel<ArrayList<Book>>> search(@Field("service") String service
            , @Field("keyword") String keyword, @Field("catename") String catename,
                                            @Field("page") int page);

    /**
     * 书籍分类
     *
     * @param service
     * @return
     */
    @FormUrlEncoded
    @POST("reader/public/v1/")
    Call<BaseModel<ArrayList<Category>>> allcate(@Field("service") String service);

    /**
     * 书籍详情
     *
     * @param service
     * @return
     */
    @FormUrlEncoded
    @POST("reader/public/v1/")
    Call<BaseModel<Book>> detail(@Field("service") String service
            , @Field("extern_bookid") String extern_bookid,@Field("userid") String userid);

    /**
     * 书籍详情
     *
     * @param service
     * @return
     */
    @FormUrlEncoded
    @POST("reader/public/v1/")
    Call<BaseModel<Book>> addReadLog(@Field("service") String service
            , @Field("extern_bookid") String extern_bookid,
                                     @Field("chapterid") String chapterid,
                                     @Field("userid") String userid);

}
