package xcvf.top.readercore.daos;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import xcvf.top.readercore.bean.Book;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "BOOK".
*/
public class BookDao extends AbstractDao<Book, Void> {

    public static final String TABLENAME = "BOOK";

    /**
     * Properties of entity Book.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Bookid = new Property(0, int.class, "bookid", false, "BOOKID");
        public final static Property Cover = new Property(1, String.class, "cover", false, "COVER");
        public final static Property Shelfid = new Property(2, String.class, "shelfid", false, "SHELFID");
        public final static Property Userid = new Property(3, String.class, "userid", false, "USERID");
        public final static Property Name = new Property(4, String.class, "name", false, "NAME");
        public final static Property Author = new Property(5, String.class, "author", false, "AUTHOR");
        public final static Property Cate_name = new Property(6, String.class, "cate_name", false, "CATE_NAME");
        public final static Property Desc = new Property(7, String.class, "desc", false, "DESC");
        public final static Property Latest_chapter_name = new Property(8, String.class, "latest_chapter_name", false, "LATEST_CHAPTER_NAME");
        public final static Property Latest_chapter_url = new Property(9, String.class, "latest_chapter_url", false, "LATEST_CHAPTER_URL");
        public final static Property Keywords = new Property(10, String.class, "keywords", false, "KEYWORDS");
        public final static Property Extern_bookid = new Property(11, String.class, "extern_bookid", false, "EXTERN_BOOKID");
        public final static Property Update_time = new Property(12, String.class, "update_time", false, "UPDATE_TIME");
    }


    public BookDao(DaoConfig config) {
        super(config);
    }
    
    public BookDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"BOOK\" (" + //
                "\"BOOKID\" INTEGER NOT NULL ," + // 0: bookid
                "\"COVER\" TEXT," + // 1: cover
                "\"SHELFID\" TEXT UNIQUE ," + // 2: shelfid
                "\"USERID\" TEXT," + // 3: userid
                "\"NAME\" TEXT," + // 4: name
                "\"AUTHOR\" TEXT," + // 5: author
                "\"CATE_NAME\" TEXT," + // 6: cate_name
                "\"DESC\" TEXT," + // 7: desc
                "\"LATEST_CHAPTER_NAME\" TEXT," + // 8: latest_chapter_name
                "\"LATEST_CHAPTER_URL\" TEXT," + // 9: latest_chapter_url
                "\"KEYWORDS\" TEXT," + // 10: keywords
                "\"EXTERN_BOOKID\" TEXT," + // 11: extern_bookid
                "\"UPDATE_TIME\" TEXT);"); // 12: update_time
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"BOOK\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Book entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getBookid());
 
        String cover = entity.getCover();
        if (cover != null) {
            stmt.bindString(2, cover);
        }
 
        String shelfid = entity.getShelfid();
        if (shelfid != null) {
            stmt.bindString(3, shelfid);
        }
 
        String userid = entity.getUserid();
        if (userid != null) {
            stmt.bindString(4, userid);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(5, name);
        }
 
        String author = entity.getAuthor();
        if (author != null) {
            stmt.bindString(6, author);
        }
 
        String cate_name = entity.getCate_name();
        if (cate_name != null) {
            stmt.bindString(7, cate_name);
        }
 
        String desc = entity.getDesc();
        if (desc != null) {
            stmt.bindString(8, desc);
        }
 
        String latest_chapter_name = entity.getLatest_chapter_name();
        if (latest_chapter_name != null) {
            stmt.bindString(9, latest_chapter_name);
        }
 
        String latest_chapter_url = entity.getLatest_chapter_url();
        if (latest_chapter_url != null) {
            stmt.bindString(10, latest_chapter_url);
        }
 
        String keywords = entity.getKeywords();
        if (keywords != null) {
            stmt.bindString(11, keywords);
        }
 
        String extern_bookid = entity.getExtern_bookid();
        if (extern_bookid != null) {
            stmt.bindString(12, extern_bookid);
        }
 
        String update_time = entity.getUpdate_time();
        if (update_time != null) {
            stmt.bindString(13, update_time);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Book entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getBookid());
 
        String cover = entity.getCover();
        if (cover != null) {
            stmt.bindString(2, cover);
        }
 
        String shelfid = entity.getShelfid();
        if (shelfid != null) {
            stmt.bindString(3, shelfid);
        }
 
        String userid = entity.getUserid();
        if (userid != null) {
            stmt.bindString(4, userid);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(5, name);
        }
 
        String author = entity.getAuthor();
        if (author != null) {
            stmt.bindString(6, author);
        }
 
        String cate_name = entity.getCate_name();
        if (cate_name != null) {
            stmt.bindString(7, cate_name);
        }
 
        String desc = entity.getDesc();
        if (desc != null) {
            stmt.bindString(8, desc);
        }
 
        String latest_chapter_name = entity.getLatest_chapter_name();
        if (latest_chapter_name != null) {
            stmt.bindString(9, latest_chapter_name);
        }
 
        String latest_chapter_url = entity.getLatest_chapter_url();
        if (latest_chapter_url != null) {
            stmt.bindString(10, latest_chapter_url);
        }
 
        String keywords = entity.getKeywords();
        if (keywords != null) {
            stmt.bindString(11, keywords);
        }
 
        String extern_bookid = entity.getExtern_bookid();
        if (extern_bookid != null) {
            stmt.bindString(12, extern_bookid);
        }
 
        String update_time = entity.getUpdate_time();
        if (update_time != null) {
            stmt.bindString(13, update_time);
        }
    }

    @Override
    public Void readKey(Cursor cursor, int offset) {
        return null;
    }    

    @Override
    public Book readEntity(Cursor cursor, int offset) {
        Book entity = new Book( //
            cursor.getInt(offset + 0), // bookid
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // cover
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // shelfid
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // userid
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // name
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // author
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // cate_name
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // desc
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // latest_chapter_name
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // latest_chapter_url
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // keywords
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // extern_bookid
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12) // update_time
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Book entity, int offset) {
        entity.setBookid(cursor.getInt(offset + 0));
        entity.setCover(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setShelfid(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setUserid(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setName(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setAuthor(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setCate_name(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setDesc(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setLatest_chapter_name(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setLatest_chapter_url(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setKeywords(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setExtern_bookid(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setUpdate_time(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
     }
    
    @Override
    protected final Void updateKeyAfterInsert(Book entity, long rowId) {
        // Unsupported or missing PK type
        return null;
    }
    
    @Override
    public Void getKey(Book entity) {
        return null;
    }

    @Override
    public boolean hasKey(Book entity) {
        // TODO
        return false;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}