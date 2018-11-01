package xcvf.top.readercore.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Unique;

import java.util.ArrayList;
import java.util.List;

import xcvf.top.readercore.interfaces.IPage;

/**
 * 章节
 * Created by xiaw on 2018/6/30.
 */
@Entity
public class Chapter  implements Parcelable {



    public String chapter_name;


    public String extern_bookid;


    public String self_page;


    public int is_fetch;


    public String engine_domain;


    public String extra_info;


    public String fetch_code;


    @Unique
    public int chapterid;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Chapter chapter = (Chapter) o;

        if (extern_bookid != null ? !extern_bookid.equals(chapter.extern_bookid) : chapter.extern_bookid != null)
            return false;
        return self_page != null ? self_page.equals(chapter.self_page) : chapter.self_page == null;
    }

    @Override
    public int hashCode() {
        int result = extern_bookid != null ? extern_bookid.hashCode() : 0;
        result = 31 * result + (self_page != null ? self_page.hashCode() : 0);
        return result;
    }


    @Ignore
    List<IPage> pages = new ArrayList<>();

    public String getChapter_name() {
        return chapter_name;
    }

    public void setChapter_name(String chapter_name) {
        this.chapter_name = chapter_name;
    }

    public String getExtern_bookid() {
        return extern_bookid;
    }

    public void setExtern_bookid(String extern_bookid) {
        this.extern_bookid = extern_bookid;
    }

    public String getSelf_page() {
        return self_page;
    }

    public void setSelf_page(String self_page) {
        this.self_page = self_page;
    }

    public int getIs_fetch() {
        return is_fetch;
    }

    public void setIs_fetch(int is_fetch) {
        this.is_fetch = is_fetch;
    }

    public String getEngine_domain() {
        return engine_domain;
    }

    public void setEngine_domain(String engine_domain) {
        this.engine_domain = engine_domain;
    }

    public String getExtra_info() {
        return extra_info;
    }

    public void setExtra_info(String extra_info) {
        this.extra_info = extra_info;
    }

    public String getFetch_code() {
        return fetch_code;
    }

    public void setFetch_code(String fetch_code) {
        this.fetch_code = fetch_code;
    }


    public List<IPage> getPages() {
        return pages;
    }

    public void setPages(List<IPage> pages) {
        this.pages.clear();
        if (pages != null) {
            this.pages.addAll(pages);
        }
    }

    /**
     * 获取下一个章节
     *
     * @param chapterid
     * @return
     */
    public static Chapter getNextChapter(String bookid,String chapterid) {
        List<Chapter> chapters = Chapter.find(Chapter.class, " chapterid >  ?  and extern_bookid = ? ", new String[]{String.valueOf(chapterid),bookid}, null, " chapterid ASC ", " 1 ");
        if (chapters != null && chapters.size() > 0) {
            return chapters.get(0);
        }
        return null;
    }


    /**
     * 根绝id获取章节
     *
     * @param chapterid
     * @return
     */
    public static Chapter getChapter(String bookid,String chapterid) {
        if (TextUtils.isEmpty(chapterid)) {
            return null;
        }
        List<Chapter> chapters = Chapter.find(Chapter.class, " chapterid =  ? and extern_bookid = ?", chapterid,bookid);
        if (chapters != null && chapters.size() > 0) {
            return chapters.get(0);
        }
        return null;
    }

    /**
     * 获取上一个章节
     *
     * @param chapterid
     * @return
     */
    public static Chapter getPreChapter(String bookid,String chapterid) {
        List<Chapter> chapters = Chapter.find(Chapter.class, " chapterid <  ? and extern_booid = ? ", new String[]{String.valueOf(chapterid),bookid}, null, " chapterid DESC ", " 1 ");
        if (chapters != null && chapters.size() > 0) {
            return chapters.get(0);
        }
        return null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.chapter_name);
        dest.writeString(this.extern_bookid);
        dest.writeString(this.self_page);
        dest.writeInt(this.is_fetch);
        dest.writeString(this.engine_domain);
        dest.writeString(this.extra_info);
        dest.writeString(this.fetch_code);
    }

    public Chapter() {
    }

    protected Chapter(Parcel in) {
        this.chapter_name = in.readString();
        this.extern_bookid = in.readString();
        this.self_page = in.readString();
        this.is_fetch = in.readInt();
        this.engine_domain = in.readString();
        this.extra_info = in.readString();
        this.fetch_code = in.readString();


    }

    public static final Creator<Chapter> CREATOR = new Creator<Chapter>() {
        @Override
        public Chapter createFromParcel(Parcel source) {
            return new Chapter(source);
        }

        @Override
        public Chapter[] newArray(int size) {
            return new Chapter[size];
        }
    };
}
