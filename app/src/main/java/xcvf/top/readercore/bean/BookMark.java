package xcvf.top.readercore.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.blankj.utilcode.util.EncryptUtils;
import com.blankj.utilcode.util.LogUtils;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Unique;

import xcvf.top.readercore.daos.BookMarkDao;
import xcvf.top.readercore.daos.DBManager;

/**
 * 书籍标签
 * Created by xiaw on 2018/10/24.
 */
@Entity
public class BookMark implements Parcelable {


    long time_stamp;
    @Unique
    String unique_key;
    int userid;
    int bookid;
    String extern_bookid;
    String chapterid;

    int page;

    public long getTime_stamp() {
        return time_stamp;
    }

    public BookMark setTime_stamp(long time_stamp) {
        this.time_stamp = time_stamp;
        return this;
    }

    public String getExtern_bookid() {
        return extern_bookid;
    }

    public BookMark setExtern_bookid(String extern_bookid) {
        this.extern_bookid = extern_bookid;
        return this;
    }


    public void save() {
        DBManager.getDaoSession().getBookMarkDao().insertOrReplace(this);
    }

    /**
     * 书签根据用户和书籍id来识别唯一
     *
     * @param userid
     * @param bookid
     * @return
     */
    private static String getUniqueKey(int userid, int bookid) {
        return userid + "--free_reader--" + bookid;
    }

    public BookMark(int userid, int bookid) {
        this.userid = userid;
        this.bookid = bookid;
        unique_key = getUniqueKey(userid, bookid);
        LogUtils.e("userid=" + userid + ",bookid=" + bookid + ",unique_key=" + unique_key);
    }

    public static BookMark getMark(Book book, int userid) {
        String uniquekey = getUniqueKey(userid, book.bookid);
        return DBManager.getDaoSession().getBookMarkDao().queryBuilder().where(BookMarkDao.Properties.Unique_key.eq(uniquekey))
                .orderDesc(BookMarkDao.Properties.Time_stamp).limit(1).build().unique();
    }

    public String getChapterid() {
        return chapterid;
    }

    public BookMark setChapterid(String chapterid) {
        this.chapterid = chapterid;
        return this;
    }

    public int getPage() {
        return page;
    }

    public BookMark setPage(int page) {
        this.page = page;
        return this;
    }


    public BookMark() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.time_stamp);
        dest.writeString(this.unique_key);
        dest.writeInt(this.userid);
        dest.writeString(this.extern_bookid);
        dest.writeString(this.chapterid);
        dest.writeInt(this.page);
    }

    public String getUnique_key() {
        return this.unique_key;
    }

    public void setUnique_key(String unique_key) {
        this.unique_key = unique_key;
    }

    public int getUserid() {
        return this.userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getBookid() {
        return this.bookid;
    }

    public void setBookid(int bookid) {
        this.bookid = bookid;
    }

    protected BookMark(Parcel in) {
        this.time_stamp = in.readLong();
        this.unique_key = in.readString();
        this.userid = in.readInt();
        this.extern_bookid = in.readString();
        this.chapterid = in.readString();
        this.page = in.readInt();
    }

    @Generated(hash = 1370582779)
    public BookMark(long time_stamp, String unique_key, int userid, int bookid, String extern_bookid, String chapterid, int page) {
        this.time_stamp = time_stamp;
        this.unique_key = unique_key;
        this.userid = userid;
        this.bookid = bookid;
        this.extern_bookid = extern_bookid;
        this.chapterid = chapterid;
        this.page = page;
    }


    public static final Creator<BookMark> CREATOR = new Creator<BookMark>() {
        @Override
        public BookMark createFromParcel(Parcel source) {
            return new BookMark(source);
        }

        @Override
        public BookMark[] newArray(int size) {
            return new BookMark[size];
        }
    };
}
