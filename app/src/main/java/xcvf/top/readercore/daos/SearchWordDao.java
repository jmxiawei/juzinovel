package xcvf.top.readercore.daos;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import xcvf.top.readercore.bean.SearchWord;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "SEARCH_WORD".
*/
public class SearchWordDao extends AbstractDao<SearchWord, Void> {

    public static final String TABLENAME = "SEARCH_WORD";

    /**
     * Properties of entity SearchWord.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Word = new Property(0, String.class, "word", false, "WORD");
        public final static Property Time_stamp = new Property(1, long.class, "time_stamp", false, "TIME_STAMP");
    }


    public SearchWordDao(DaoConfig config) {
        super(config);
    }
    
    public SearchWordDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"SEARCH_WORD\" (" + //
                "\"WORD\" TEXT UNIQUE ," + // 0: word
                "\"TIME_STAMP\" INTEGER NOT NULL );"); // 1: time_stamp
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"SEARCH_WORD\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, SearchWord entity) {
        stmt.clearBindings();
 
        String word = entity.getWord();
        if (word != null) {
            stmt.bindString(1, word);
        }
        stmt.bindLong(2, entity.getTime_stamp());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, SearchWord entity) {
        stmt.clearBindings();
 
        String word = entity.getWord();
        if (word != null) {
            stmt.bindString(1, word);
        }
        stmt.bindLong(2, entity.getTime_stamp());
    }

    @Override
    public Void readKey(Cursor cursor, int offset) {
        return null;
    }    

    @Override
    public SearchWord readEntity(Cursor cursor, int offset) {
        SearchWord entity = new SearchWord( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // word
            cursor.getLong(offset + 1) // time_stamp
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, SearchWord entity, int offset) {
        entity.setWord(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setTime_stamp(cursor.getLong(offset + 1));
     }
    
    @Override
    protected final Void updateKeyAfterInsert(SearchWord entity, long rowId) {
        // Unsupported or missing PK type
        return null;
    }
    
    @Override
    public Void getKey(SearchWord entity) {
        return null;
    }

    @Override
    public boolean hasKey(SearchWord entity) {
        // TODO
        return false;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
