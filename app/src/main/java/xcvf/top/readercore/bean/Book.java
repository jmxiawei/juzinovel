package xcvf.top.readercore.bean;

import android.os.Parcel;
import android.os.Parcelable;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Unique;

import java.util.ArrayList;
import java.util.List;

import org.greenrobot.greendao.annotation.Generated;

import xcvf.top.readercore.daos.BookDao;
import xcvf.top.readercore.daos.DBManager;

/**
 * 本地书架
 * Created by xiaw on 2018/6/29.
 */
@Entity
public class Book implements Parcelable {

    @Transient
    public List<Chapter> chapters = new ArrayList<>();

    @Transient
    public List<Book> priorities = new ArrayList<>();

    public int bookid;
    public String cover;

    public String shelfid;
    public String userid;
    public String name;
    public String author;
    public String engine_domain;
    public String cate_name;
    public String desc;
    public String info_url;
    public String read_url;
    public String latest_chapter_name;
    public String latest_chapter_url;
    public String keywords;
    public String extern_bookid;
    public String update_time;

    @Unique
    public String uniqueKey;

    public List<Book> getPriorities() {
        return priorities;
    }

    public Book setPriorities(List<Book> priorities) {
        this.priorities = priorities;
        return this;
    }

    public String getUniqueKey() {
        return uniqueKey;
    }

    public Book setUniqueKey(String uniqueKey) {
        this.uniqueKey = uniqueKey;
        return this;
    }

    public String getUserid() {
        return userid;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public Book setUpdate_time(String update_time) {
        this.update_time = update_time;
        return this;
    }

    public Book setUserid(String userid) {
        this.userid = userid;
        return this;
    }

    public Book(int bookid) {
        this.bookid = bookid;
    }

    public String getShelfid() {
        return shelfid;
    }

    public Book setShelfid(String shelfid) {
        this.shelfid = shelfid;
        return this;
    }

    public int getBookid() {
        return this.bookid;
    }

    public void setBookid(int bookid) {
        this.bookid = bookid;
    }

    public String getCover() {
        return this.cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCate_name() {
        return this.cate_name;
    }

    public void setCate_name(String cate_name) {
        this.cate_name = cate_name;
    }

    public String getDesc() {
        return this.desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getLatest_chapter_name() {
        return this.latest_chapter_name;
    }

    public void setLatest_chapter_name(String latest_chapter_name) {
        this.latest_chapter_name = latest_chapter_name;
    }

    public void save(String userid) {
        setUserid(userid);
        uniqueKey = extern_bookid + "_iscore.top_" + userid;
        DBManager.getDaoSession().getBookDao().insertOrReplace(this);
    }


    private static String getUKey(String userid, int bookid) {
        return bookid + "_iscore.top_" + userid;
    }

    /**
     * @param userid
     * @param bookid
     */
    public static void delete(String userid, int bookid) {
        DBManager.getDaoSession().getBookDao().queryBuilder().where(BookDao.Properties.UniqueKey.eq(getUKey(userid, bookid))
        ).buildDelete().executeDeleteWithoutDetachingEntities();
    }

    public static void save(String userid, List<Book> list) {
        if (list != null) {
            for (Book book : list) {
                book.setUserid(userid);
                book.setUniqueKey(getUKey(userid,book.bookid));
            }
            DBManager.getDaoSession().getBookDao().insertOrReplaceInTx(list);
        }
    }


    public String getLatest_chapter_url() {
        return this.latest_chapter_url;
    }

    public void setLatest_chapter_url(String latest_chapter_url) {
        this.latest_chapter_url = latest_chapter_url;
    }

    public String getKeywords() {
        return this.keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getExtern_bookid() {
        return this.extern_bookid;
    }

    public void setExtern_bookid(String extern_bookid) {
        this.extern_bookid = extern_bookid;
    }

    public Book() {
    }


    public String getEngine_domain() {
        return this.engine_domain;
    }

    public void setEngine_domain(String engine_domain) {
        this.engine_domain = engine_domain;
    }

    public String getInfo_url() {
        return this.info_url;
    }

    public void setInfo_url(String info_url) {
        this.info_url = info_url;
    }

    public String getRead_url() {
        return this.read_url;
    }

    public void setRead_url(String read_url) {
        this.read_url = read_url;
    }

    @Generated(hash = 923203810)
    public Book(int bookid, String cover, String shelfid, String userid, String name, String author, String engine_domain,
            String cate_name, String desc, String info_url, String read_url, String latest_chapter_name, String latest_chapter_url,
            String keywords, String extern_bookid, String update_time, String uniqueKey) {
        this.bookid = bookid;
        this.cover = cover;
        this.shelfid = shelfid;
        this.userid = userid;
        this.name = name;
        this.author = author;
        this.engine_domain = engine_domain;
        this.cate_name = cate_name;
        this.desc = desc;
        this.info_url = info_url;
        this.read_url = read_url;
        this.latest_chapter_name = latest_chapter_name;
        this.latest_chapter_url = latest_chapter_url;
        this.keywords = keywords;
        this.extern_bookid = extern_bookid;
        this.update_time = update_time;
        this.uniqueKey = uniqueKey;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.chapters);
        dest.writeTypedList(this.priorities);
        dest.writeInt(this.bookid);
        dest.writeString(this.cover);
        dest.writeString(this.shelfid);
        dest.writeString(this.userid);
        dest.writeString(this.name);
        dest.writeString(this.author);
        dest.writeString(this.engine_domain);
        dest.writeString(this.cate_name);
        dest.writeString(this.desc);
        dest.writeString(this.info_url);
        dest.writeString(this.read_url);
        dest.writeString(this.latest_chapter_name);
        dest.writeString(this.latest_chapter_url);
        dest.writeString(this.keywords);
        dest.writeString(this.extern_bookid);
        dest.writeString(this.update_time);
        dest.writeString(this.uniqueKey);
    }

    protected Book(Parcel in) {
        this.chapters = in.createTypedArrayList(Chapter.CREATOR);
        this.priorities = in.createTypedArrayList(Book.CREATOR);
        this.bookid = in.readInt();
        this.cover = in.readString();
        this.shelfid = in.readString();
        this.userid = in.readString();
        this.name = in.readString();
        this.author = in.readString();
        this.engine_domain = in.readString();
        this.cate_name = in.readString();
        this.desc = in.readString();
        this.info_url = in.readString();
        this.read_url = in.readString();
        this.latest_chapter_name = in.readString();
        this.latest_chapter_url = in.readString();
        this.keywords = in.readString();
        this.extern_bookid = in.readString();
        this.update_time = in.readString();
        this.uniqueKey = in.readString();
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel source) {
            return new Book(source);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };
}
