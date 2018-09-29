package xcvf.top.readercore.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

import java.util.ArrayList;
import java.util.List;

import xcvf.top.readercore.interfaces.IPage;

/**
 * 章节
 * Created by xiaw on 2018/6/30.
 */
public class Chapter extends SugarRecord implements Parcelable {

    public String chapter_name;
    public String extern_bookid;
    public String self_page;
    public int is_fetch;
    public String engine_domain;
    public String extra_info;
    public String fetch_code;
    public String content;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
        dest.writeString(this.content);
        dest.writeList(this.pages);
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
        this.content = in.readString();
        this.pages = new ArrayList<IPage>();
        in.readList(this.pages, IPage.class.getClassLoader());
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
