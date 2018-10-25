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

    public int bookid=5;
    public String cover  = "http://www.biquge.com.tw/files/article/image/16/16288/16288s.jpg";
    public String name = "斗罗大陆III龙王传说";
    public String author = "唐家三少";
    public String cate_name = "玄幻小说";
    public String desc ="    伴随着魂导科技的进步，斗罗大陆上的人类征服了海洋，又发现了两片大陆。魂兽也随着人类魂师的猎杀无度走向灭亡，沉睡无数年的魂兽之王在星斗大森林最后的净土苏醒，它要带领仅存的族人，向人类复仇！     唐舞麟立志要成为一名强大的魂师，可当武魂觉醒时，苏醒的，却是……     旷世之才，龙王之争，我们的龙王传说，将由此开始";
    public String latest_chapter_name="一千八百五十一章 海神斗罗";
    public String latest_chapter_url;
    public String keywords;
    @Unique
    public String extern_bookid="16_16288";


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
