package xcvf.top.readercore.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Unique;

import java.util.ArrayList;
import java.util.List;

import xcvf.top.readercore.daos.ChapterDao;
import xcvf.top.readercore.daos.DBManager;
import xcvf.top.readercore.interfaces.IPage;

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

    public boolean is_download;
    public String self_page;

    @Transient
    int status = STATUS_OK;

    @Unique
    public int chapterid;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Chapter chapter = (Chapter) o;

        if (chapterid != chapter.chapterid) return false;
        if (extern_bookid != null ? !extern_bookid.equals(chapter.extern_bookid) : chapter.extern_bookid != null)
            return false;
        return self_page != null ? self_page.equals(chapter.self_page) : chapter.self_page == null;
    }

    @Override
    public int hashCode() {
        int result = extern_bookid != null ? extern_bookid.hashCode() : 0;
        result = 31 * result + (self_page != null ? self_page.hashCode() : 0);
        result = 31 * result + chapterid;
        return result;
    }

    public int getStatus() {
        return status;
    }

    public Chapter setStatus(int status) {
        this.status = status;
        return this;
    }

    @Transient
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
    public static Chapter getNextChapter(String bookid, String chapterid) {
        return DBManager.getDaoSession().
                getChapterDao().
                queryBuilder().
                where(ChapterDao.Properties.Chapterid.gt(String.valueOf(chapterid))).
                where(ChapterDao.Properties.Extern_bookid.eq(bookid)).
                orderAsc(ChapterDao.Properties.Chapterid).limit(1).build().unique();
    }


    /**
     * 根绝id获取章节
     *
     * @param chapterid
     * @return
     */
    public static Chapter getChapter(String bookid, String chapterid) {
        if (TextUtils.isEmpty(chapterid)) {
            return null;
        }
        return DBManager.getDaoSession().
                getChapterDao().
                queryBuilder().
                where(ChapterDao.Properties.Chapterid.eq(String.valueOf(chapterid))).
                where(ChapterDao.Properties.Extern_bookid.eq(bookid)).
                orderAsc(ChapterDao.Properties.Chapterid).limit(1).build().unique();

    }

    /**
     * 获取上一个章节
     *
     * @param chapterid
     * @return
     */
    public static Chapter getPreChapter(String bookid, String chapterid) {
        return DBManager.getDaoSession().
                getChapterDao().
                queryBuilder().
                where(ChapterDao.Properties.Chapterid.lt(String.valueOf(chapterid))).
                where(ChapterDao.Properties.Extern_bookid.eq(bookid)).
                orderAsc(ChapterDao.Properties.Chapterid).limit(1).build().unique();
    }

    /**
     * 后面还有多少章节
     *
     * @param extern_bookid
     * @param chappterid
     * @return
     */
    public static int getLeftChapter(String extern_bookid, String chappterid) {
        return (int) DBManager.getDaoSession().getChapterDao().queryBuilder()
                .where(ChapterDao.Properties.Extern_bookid.eq(extern_bookid))
                .where(ChapterDao.Properties.Chapterid.gt(chappterid)).count();
    }

    /**
     * 查询后面的章节
     *
     * @param extern_bookid
     * @param chappterid
     * @return
     */
    public static List<Chapter> getLeftChapter(String extern_bookid, String chappterid, int count) {
        return DBManager.getDaoSession().getChapterDao().queryBuilder()
                .where(ChapterDao.Properties.Extern_bookid.eq(extern_bookid))
                .where(ChapterDao.Properties.Chapterid.gt(chappterid))
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

    @Generated(hash = 1812299664)
    public Chapter(String chapter_name, String extern_bookid, boolean is_download, String self_page, int chapterid) {
        this.chapter_name = chapter_name;
        this.extern_bookid = extern_bookid;
        this.is_download = is_download;
        this.self_page = self_page;
        this.chapterid = chapterid;
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
        dest.writeInt(this.status);
        dest.writeInt(this.chapterid);
    }

    public boolean getIs_download() {
        return this.is_download;
    }

    public void setIs_download(boolean is_download) {
        this.is_download = is_download;
    }

    protected Chapter(Parcel in) {
        this.chapter_name = in.readString();
        this.extern_bookid = in.readString();
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
