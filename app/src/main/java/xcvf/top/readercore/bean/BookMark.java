package xcvf.top.readercore.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.blankj.utilcode.util.EncryptUtils;


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
public class BookMark  implements Parcelable {


    long time_stamp;
    @Unique
    String unique_key;
    String userid;
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


    public  void save() {
        DBManager.getDaoSession().getBookMarkDao().insertOrReplace(this);
    }

    public BookMark(String userid, String extern_bookid) {
        this.userid = userid;
        this.extern_bookid = extern_bookid;
        unique_key = EncryptUtils.encryptMD5ToString((userid + extern_bookid));
    }

    public static BookMark getMark(Book book, String userid) {
        String uniquekey = EncryptUtils.encryptMD5ToString((userid + book.extern_bookid));
        return DBManager.getDaoSession().getBookMarkDao().queryBuilder().where(BookMarkDao.Properties.Unique_key.eq(uniquekey))
                .limit(1).build().unique();
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
        dest.writeString(this.userid);
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

    public String getUserid() {
        return this.userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    protected BookMark(Parcel in) {
        this.time_stamp = in.readLong();
        this.unique_key = in.readString();
        this.userid = in.readString();
        this.extern_bookid = in.readString();
        this.chapterid = in.readString();
        this.page = in.readInt();
    }

    @Generated(hash = 173078373)
    public BookMark(long time_stamp, String unique_key, String userid, String extern_bookid,
            String chapterid, int page) {
        this.time_stamp = time_stamp;
        this.unique_key = unique_key;
        this.userid = userid;
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
