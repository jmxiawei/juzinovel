package xcvf.top.readercore.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.blankj.utilcode.util.EncryptUtils;
import com.blankj.utilcode.util.Utils;
import com.orm.SugarRecord;
import com.orm.dsl.Column;
import com.orm.dsl.Unique;

import java.util.List;

/**
 * 书籍标签
 * Created by xiaw on 2018/10/24.
 */
public class BookMark extends SugarRecord implements Parcelable {


    @Column(name = "time_stamp")
    long time_stamp;

    @Unique
    @Column(name = "unique_key")
    String unique_key;

    @Column(name = "userid")
    String userid;
    @Column(name = "extern_bookid")
    String extern_bookid;
    @Column(name = "chapterid")
    String chapterid;
    @Column(name = "page")
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


    public static void saveMark(BookMark mark) {
        BookMark.update(mark);
    }

    public BookMark(String userid, String extern_bookid) {
        this.userid = userid;
        this.extern_bookid = extern_bookid;
        unique_key = EncryptUtils.encryptMD5ToString((userid + extern_bookid));
    }

    public static BookMark getMark(Book book, String userid) {
        String uniquekey = EncryptUtils.encryptMD5ToString((userid + book.extern_bookid));
        List<BookMark> bookMarks = BookMark.find(BookMark.class, " unique_key = ? ", uniquekey);
        if (bookMarks != null && bookMarks.size() > 0) {
            return bookMarks.get(0);
        }
        return null;
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

    protected BookMark(Parcel in) {
        this.time_stamp = in.readLong();
        this.unique_key = in.readString();
        this.userid = in.readString();
        this.extern_bookid = in.readString();
        this.chapterid = in.readString();
        this.page = in.readInt();
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
