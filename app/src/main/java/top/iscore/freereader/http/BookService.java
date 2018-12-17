package top.iscore.freereader.http;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import xcvf.top.readercore.bean.Book;
import xcvf.top.readercore.bean.BookCate;
import xcvf.top.readercore.bean.Category;
import xcvf.top.readercore.bean.Chapter;
import xcvf.top.readercore.bean.Rank;
import xcvf.top.readercore.bean.User;

/**
 * 结尾需要带/
 * Created by xiaw on 2018/9/18.
 */

public interface BookService {



    /**
     * 检查更新
     *
     * @param service
     * @param vcode   版本号
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
            , @Field("bookid") int bookid);

    /**
     * 删除书架
     *
     * @return
     */
    @FormUrlEncoded
    @POST("reader/public/v1/")
    Call<BaseModel<Book>> deleteBookShelf(@Field("service") String service
            , @Field("userid") String userid, @Field("shelfid") String shelfid, @Field("bookid") int bookid);


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
            , @Field("keyword") String keyword,@Field("type") int type,
                                            @Field("ranklistid") int ranklistid,
                                            @Field("page") int page);

    /**
     * 书籍分类
     *
     * @param service
     * @return
     */
    @FormUrlEncoded
    @POST("reader/public/v1/")
    Call<BaseModel<ArrayList<BookCate>>> allcate(@Field("service") String service);


    /**
     * 排行榜 Book.ranklist
     *
     * @param service
     * @return
     */
    @FormUrlEncoded
    @POST("reader/public/v1/")
    Call<BaseModel<ArrayList<Rank>>> rankList(@Field("service") String service);


    /**
     * 换源列表
     *
     * @param service
     * @return
     */
    @FormUrlEncoded
    @POST("reader/public/v1/")
    Call<BaseModel<ArrayList<Book>>> listSource(@Field("service") String service
            , @Field("name") String name, @Field("author") String author);

    /**
     * 书籍详情
     *
     * @param service
     * @return
     */
    @FormUrlEncoded
    @POST("reader/public/v1/")
    Call<BaseModel<Book>> detail(@Field("service") String service
            , @Field("bookid") int bookid, @Field("userid") String userid);

    /**
     * 新增阅读记录
     *
     * @param service
     * @return
     */
    @FormUrlEncoded
    @POST("reader/public/v1/")
    Call<BaseModel<Book>> addReadLog(@Field("service") String service
            , @Field("bookid") int bookid,
                                     @Field("chapterid") String chapterid,
                                     @Field("userid") String userid);


    /**
     * 新增阅读记录
     *
     * @param service
     * @return
     */
    @FormUrlEncoded
    @POST("reader/public/v1/")
    Call<BaseModel<Book>> addBookMarker(@Field("service") String service
                    , @Field("extern_bookid") String extern_bookid
                    , @Field("bookid") int bookid,
                                        @Field("chapterid") String chapterid,
                                        @Field("chapter_name") String chapter_name,
                                        @Field("page") int page,
                                        @Field("userid") int userid,@Field("engine_domain") String engine_domain,@Field("read_url") String read_url);


    /**
     * 登录
     *
     * @param service
     * @return
     */
    @FormUrlEncoded
    @POST("reader/public/v1/")
    Call<BaseModel<User>> login(@Field("service") String service,
                                @Field("account") String account,
                                @Field("pwd") String pwd,
                                @Field("avatar") String avatar,
                                @Field("nickname") String nickname,
                                @Field("gender") String gender);


    /**
     * 崩溃日志
     *
     * @param service
     * @return
     */
    @FormUrlEncoded
    @POST("reader/public/v1/")
    Call<BaseModel<Object>> crashLog(@Field("service") String service,
                                @Field("crashlog") String crashlog,
                                @Field("versionname") String versionname,
                                @Field("vcode") String vcode);


}
