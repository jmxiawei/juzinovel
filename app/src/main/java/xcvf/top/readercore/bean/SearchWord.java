package xcvf.top.readercore.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

import java.util.List;

import xcvf.top.readercore.daos.DBManager;
import xcvf.top.readercore.daos.SearchWordDao;

@Entity
public class SearchWord {

    @Unique
    String word;
    long time_stamp;
    @Generated(hash = 306589094)
    public SearchWord(String word, long time_stamp) {
        this.word = word;
        this.time_stamp = time_stamp;
    }
    @Generated(hash = 407254878)
    public SearchWord() {
    }
    public String getWord() {
        return this.word;
    }
    public void setWord(String word) {
        this.word = word;
    }
    public long getTime_stamp() {
        return this.time_stamp;
    }
    public void setTime_stamp(long time_stamp) {
        this.time_stamp = time_stamp;
    }


    public void save(){
        DBManager.getDaoSession().getSearchWordDao().insertOrReplace(this);
    }


    public  static void deleteAll(){
        DBManager.getDaoSession().getSearchWordDao().queryBuilder().buildDelete().executeDeleteWithoutDetachingEntities();
    }

    public static List<SearchWord> getList(){
        return DBManager.getDaoSession().getSearchWordDao().queryBuilder().orderDesc(SearchWordDao.Properties.Time_stamp).limit(20).list();
    }
}
