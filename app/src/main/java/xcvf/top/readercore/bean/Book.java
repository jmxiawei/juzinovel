package xcvf.top.readercore.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;
import com.orm.dsl.Unique;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaw on 2018/6/29.
 */

public class Book  extends SugarRecord implements Parcelable {

    @Ignore
    public List<Chapter> chapters = new ArrayList<>();

    public int bookid;
    public String cover  ;
    public String name ;
    public String author ;
    public String cate_name;
    public String desc ;
    public String latest_chapter_name;
    public String latest_chapter_url;
    public String keywords;
    @Unique
    public String extern_bookid;

    public Book(int bookid) {
        this.bookid = bookid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.bookid);
        dest.writeString(this.cover);
        dest.writeString(this.name);
        dest.writeString(this.author);
        dest.writeString(this.cate_name);
        dest.writeString(this.desc);
        dest.writeString(this.latest_chapter_name);
        dest.writeString(this.latest_chapter_url);
        dest.writeString(this.keywords);
        dest.writeString(this.extern_bookid);
    }

    public Book() {
    }

    protected Book(Parcel in) {
        this.bookid = in.readInt();
        this.cover = in.readString();
        this.name = in.readString();
        this.author = in.readString();
        this.cate_name = in.readString();
        this.desc = in.readString();
        this.latest_chapter_name = in.readString();
        this.latest_chapter_url = in.readString();
        this.keywords = in.readString();
        this.extern_bookid = in.readString();
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
