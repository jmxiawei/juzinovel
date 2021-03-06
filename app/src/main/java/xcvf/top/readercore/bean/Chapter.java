package xcvf.top.readercore.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Base64;

import com.blankj.utilcode.util.LogUtils;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Unique;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import xcvf.top.readercore.daos.ChapterDao;
import xcvf.top.readercore.daos.DBManager;

import org.greenrobot.greendao.annotation.Generated;

/**
 * 章节
 * Created by xiaw on 2018/6/30.
 */
@Entity
public class Chapter implements Parcelable {

    @Transient
    public static final int STATUS_ERROR = 1;
    @Transient
    public static final int STATUS_OK = 0;

    public String chapter_name;


    public String extern_bookid;
    public int is_fetch;
    public String engine_domain;
    public boolean is_download;

    public int bookid;

    @Id
    @Unique
    public String self_page;

    @Transient
    int status = STATUS_OK;

    public int chapterid;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Chapter chapter = (Chapter) o;
        return chapterid == chapter.chapterid &&
                Objects.equals(extern_bookid, chapter.extern_bookid) &&
                Objects.equals(engine_domain, chapter.engine_domain) &&
                Objects.equals(self_page, chapter.self_page);
    }

    @Override
    public int hashCode() {

        return Objects.hash(extern_bookid, engine_domain, self_page, chapterid);
    }

    public String getDecodedSelfPage() {
        return new String(Base64.decode(self_page, Base64.DEFAULT));
    }

    public int getStatus() {
        return status;
    }

    public Chapter setStatus(int status) {
        this.status = status;
        return this;
    }

    @Override
    public String toString() {
        return "Chapter{" +
                "chapter_name='" + chapter_name + '\'' +
                ", extern_bookid='" + extern_bookid + '\'' +
                ", self_page='" + self_page + '\'' +
                ", chapterid=" + chapterid +
                '}';
    }

    @Transient
    List<Page> pages = new ArrayList<>();

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
        this.self_page = new String(Base64.encode(self_page.getBytes(), Base64.DEFAULT));
        //LogUtils.e("self_page=",self_page);
    }


    public List<Page> getPages() {
        return pages;
    }

    public void setPages(List<Page> pages) {
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
    public static Chapter getNextChapter(String extern_bookid, String chapterid) {
        Chapter chapter = DBManager.getDaoSession().
                getChapterDao().
                queryBuilder().
                where(ChapterDao.Properties.Chapterid.gt(String.valueOf(chapterid))).
                where(ChapterDao.Properties.Extern_bookid.eq(extern_bookid)).
                orderAsc(ChapterDao.Properties.Chapterid).limit(1).build().unique();
        //chapter.self_page = new String(Base64.decode(chapter.self_page, Base64.DEFAULT));
        LogUtils.e(chapter);
        return chapter;
    }


    /**
     * 根绝id获取章节
     *
     * @param chapterid
     * @return
     */
    public static Chapter getChapter(String extern_bookid, String chapterid) {

        Chapter chapter = DBManager.getDaoSession().
                getChapterDao().
                queryBuilder().
                where(ChapterDao.Properties.Chapterid.ge(String.valueOf(chapterid))).
                where(ChapterDao.Properties.Extern_bookid.eq(extern_bookid)).
                orderAsc(ChapterDao.Properties.Chapterid).
                limit(1).build().unique();

        //chapter.self_page = new String(Base64.decode(chapter.self_page, Base64.DEFAULT));
        return chapter;
    }


    /**
     * 根绝id获取章节
     *
     * @param bookid
     * @return
     */
    public static List<Chapter> getAllChapter(int bookid,String extern_bookid,long startid) {
        return DBManager.getDaoSession().
                getChapterDao().
                queryBuilder().
                where(ChapterDao.Properties.Bookid.eq(bookid)).
                where(ChapterDao.Properties.Chapterid.ge(startid)).
                where(ChapterDao.Properties.Extern_bookid.eq(extern_bookid)).build().list();
    }


    /**
     * 获取上一个章节
     *
     * @param chapterid
     * @return
     */
    public static Chapter getPreChapter(String extern_bookid, String chapterid) {
        Chapter chapter = DBManager.getDaoSession().
                getChapterDao().
                queryBuilder().
                where(ChapterDao.Properties.Chapterid.lt(String.valueOf(chapterid))).
                where(ChapterDao.Properties.Extern_bookid.eq(extern_bookid)).
                orderDesc(ChapterDao.Properties.Chapterid).limit(1).build().unique();
        return chapter;
    }

    /**
     * 后面还有多少章节
     *
     * @param extern_bookid
     * @param chapterid
     * @return
     */
    public static int getLeftChapter(String extern_bookid, String chapterid) {
        return (int) DBManager.getDaoSession().getChapterDao().queryBuilder()
                .where(ChapterDao.Properties.Extern_bookid.eq(extern_bookid))
                .where(ChapterDao.Properties.Chapterid.gt(chapterid)).count();
    }

    /**
     * 查询后面的章节
     *
     * @param extern_bookid
     * @param chapterid
     * @return
     */
    public static List<Chapter> getLeftChapter(String extern_bookid, String chapterid, int count) {
        return DBManager.getDaoSession().getChapterDao().queryBuilder()
                .where(ChapterDao.Properties.Extern_bookid.eq(extern_bookid))
                .where(ChapterDao.Properties.Chapterid.gt(chapterid))
                .limit(count)
                .build()
                .list();
    }


    public int getChapterid() {
        return this.chapterid;
    }

    public void setChapterid(int chapterid) {
        this.chapterid = chapterid;
    }

    public Chapter() {
    }

    @Generated(hash = 709787008)
    public Chapter(String chapter_name, String extern_bookid, int is_fetch, String engine_domain, boolean is_download,
            int bookid, String self_page, int chapterid) {
        this.chapter_name = chapter_name;
        this.extern_bookid = extern_bookid;
        this.is_fetch = is_fetch;
        this.engine_domain = engine_domain;
        this.is_download = is_download;
        this.bookid = bookid;
        this.self_page = self_page;
        this.chapterid = chapterid;
    }

    public boolean getIs_download() {
        return this.is_download;
    }

    public void setIs_download(boolean is_download) {
        this.is_download = is_download;
    }

    public int getIs_fetch() {
        return this.is_fetch;
    }

    public void setIs_fetch(int is_fetch) {
        this.is_fetch = is_fetch;
    }

    public String getEngine_domain() {
        return this.engine_domain;
    }

    public void setEngine_domain(String engine_domain) {
        this.engine_domain = engine_domain;
    }


    public String getFullPath(){
        final String self_page = new String(Base64.decode(getSelf_page(), Base64.DEFAULT));
        final String url;
        if (self_page.startsWith("http")) {
            url = self_page;
        } else {
            url = engine_domain + self_page;
        }
        return url;
    }

    public int getBookid() {
        return this.bookid;
    }

    public void setBookid(int bookid) {
        this.bookid = bookid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.chapter_name);
        dest.writeString(this.extern_bookid);
        dest.writeInt(this.is_fetch);
        dest.writeString(this.engine_domain);
        dest.writeByte(this.is_download ? (byte) 1 : (byte) 0);
        dest.writeInt(this.bookid);
        dest.writeString(this.self_page);
        dest.writeInt(this.status);
        dest.writeInt(this.chapterid);
    }

    protected Chapter(Parcel in) {
        this.chapter_name = in.readString();
        this.extern_bookid = in.readString();
        this.is_fetch = in.readInt();
        this.engine_domain = in.readString();
        this.is_download = in.readByte() != 0;
        this.bookid = in.readInt();
        this.self_page = in.readString();
        this.status = in.readInt();
        this.chapterid = in.readInt();
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
