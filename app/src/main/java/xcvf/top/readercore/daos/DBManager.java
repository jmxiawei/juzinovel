package xcvf.top.readercore.daos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;

/**
 * Created by xiaw on 2018/11/2.
 */

public class DBManager {
    // 是否加密
    private static final String DB_NAME = "freereader.db";
    private static DBManager mDbManager;
    private static DaoMaster.DevOpenHelper mDevOpenHelper;
    private static DaoMaster mDaoMaster;
    private static DaoSession mDaoSession;

    private static Context mContext;


    public static final void init(Context context) {
        mContext = context;
        int version = SPUtils.getInstance().getInt("dbversion");
        int newVersion = DaoMaster.SCHEMA_VERSION;
        if (newVersion != version) {
            //数据库升级了
            SPUtils.getInstance().clear();
            SPUtils.getInstance().put("dbversion", newVersion);
            LogUtils.e("数据库升级了!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        }
    }

    private DBManager() {
        // 初始化数据库信息
        mDevOpenHelper = new DaoMaster.DevOpenHelper(mContext, DB_NAME);
        getDaoMaster();
        getDaoSession();
    }

    public static DBManager getInstance() {
        if (null == mDbManager) {
            synchronized (DBManager.class) {
                if (null == mDbManager) {
                    mDbManager = new DBManager();
                }
            }
        }
        return mDbManager;
    }

    /**
     * @desc 获取可读数据库
     * @autor Tiany
     * @time 2016/8/13
     **/
    public static SQLiteDatabase getReadableDatabase() {
        if (null == mDevOpenHelper) {
            getInstance();
        }
        return mDevOpenHelper.getReadableDatabase();
    }

    /**
     * @desc 获取可写数据库
     * @autor Tiany
     * @time 2016/8/13
     **/
    public static SQLiteDatabase getWritableDatabase() {
        if (null == mDevOpenHelper) {
            getInstance();
        }
        return mDevOpenHelper.getWritableDatabase();
    }

    /**
     * @desc 获取DaoMaster
     * @autor Tiany
     * @time 2016/8/13
     **/
    public static DaoMaster getDaoMaster() {
        if (null == mDaoMaster) {
            synchronized (DBManager.class) {
                if (null == mDaoMaster) {
                    mDaoMaster = new DaoMaster(getWritableDatabase());
                }
            }
        }
        return mDaoMaster;
    }

    /**
     * @desc 获取DaoSession
     * @autor Tiany
     * @time 2016/8/13
     **/
    public static DaoSession getDaoSession() {
        if (null == mDaoSession) {
            synchronized (DBManager.class) {
                mDaoSession = getDaoMaster().newSession();
            }
        }

        return mDaoSession;
    }
}
