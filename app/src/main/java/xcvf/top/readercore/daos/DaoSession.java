package xcvf.top.readercore.daos;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import xcvf.top.readercore.bean.Chapter;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig testDemoDaoConfig;
    private final DaoConfig greenDaoConfig;
    private final DaoConfig chapterDaoConfig;

    private final TestDemoDao testDemoDao;
    private final GreenDao greenDao;
    private final ChapterDao chapterDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        testDemoDaoConfig = daoConfigMap.get(TestDemoDao.class).clone();
        testDemoDaoConfig.initIdentityScope(type);

        greenDaoConfig = daoConfigMap.get(GreenDao.class).clone();
        greenDaoConfig.initIdentityScope(type);

        chapterDaoConfig = daoConfigMap.get(ChapterDao.class).clone();
        chapterDaoConfig.initIdentityScope(type);

        testDemoDao = new TestDemoDao(testDemoDaoConfig, this);
        greenDao = new GreenDao(greenDaoConfig, this);
        chapterDao = new ChapterDao(chapterDaoConfig, this);

        registerDao(TestDemo.class, testDemoDao);
        registerDao(Green.class, greenDao);
        registerDao(Chapter.class, chapterDao);
    }
    
    public void clear() {
        testDemoDaoConfig.clearIdentityScope();
        greenDaoConfig.clearIdentityScope();
        chapterDaoConfig.clearIdentityScope();
    }

    public TestDemoDao getTestDemoDao() {
        return testDemoDao;
    }

    public GreenDao getGreenDao() {
        return greenDao;
    }

    public ChapterDao getChapterDao() {
        return chapterDao;
    }

}