package xcvf.top.readercore.bean;

import android.os.Parcel;
import android.os.Parcelable;



import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Unique;

import java.util.ArrayList;
import java.util.List;
import org.greenrobot.greendao.annotation.Generated;

/**本地书架
 * Created by xiaw on 2018/6/29.
 */
@Entity
public class Book  implements Parcelable {

    @Transient
    public List<Chapter> chapters = new ArrayList<>();

    public int bookid;
    public String cover  ;
    @Unique
    public String shelfid ;
    public String userid;
    public String name ;
    public String author ;
    public String cate_name;
    public String desc ;
    public String latest_chapter_name;
    public String latest_chapter_url;
    public String keywords;
    public String extern_bookid;
    public String update_time;


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

    @Generated(hash = 347487954)
    public Book(int bookid, String cover, String shelfid, String userid,
            String name, String author, String cate_name, String desc,
            String latest_chapter_name, String latest_chapter_url, String keywords,
            String extern_bookid, String update_time) {
        this.bookid = bookid;
        this.cover = cover;
        this.shelfid = shelfid;
        this.userid = userid;
        this.name = name;
        this.author = author;
        this.cate_name = cate_name;
        this.desc = desc;
        this.latest_chapter_name = latest_chapter_name;
        this.latest_chapter_url = latest_chapter_url;
        this.keywords = keywords;
        this.extern_bookid = extern_bookid;
        this.update_time = update_time;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.chapters);
        dest.writeInt(this.bookid);
        dest.writeString(this.cover);
        dest.writeString(this.shelfid);
        dest.writeString(this.userid);
        dest.writeString(this.name);
        dest.writeString(this.author);
        dest.writeString(this.cate_name);
        dest.writeString(this.desc);
        dest.writeString(this.latest_chapter_name);
        dest.writeString(this.latest_chapter_url);
        dest.writeString(this.keywords);
        dest.writeString(this.extern_bookid);
    }

    protected Book(Parcel in) {
        this.chapters = in.createTypedArrayList(Chapter.CREATOR);
        this.bookid = in.readInt();
        this.cover = in.readString();
        this.shelfid = in.readString();
        this.userid = in.readString();
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
