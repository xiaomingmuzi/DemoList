package com.lixm.chat.database;


import com.lixm.chat.application.APP;

import org.xutils.DbManager;
import org.xutils.x;

/**
 * User: LXM
 * Date: 2016-03-10
 * Time: 16:44
 * Detail:数据库辅助类
 */
public class DBHelper {
    private static DbManager.DaoConfig daoConfig;
    private static DbManager db=null;//提供获取唯一的一个DbUtils对象的方法

    public synchronized static DbManager getDbManager(){
        if (db==null)
            db= x.getDb(daoConfig);
        return db;
    }

    public static void init(APP app){
        x.Ext.init(app);
        daoConfig=new DbManager.DaoConfig()
                .setDbName("com.lixm.chat")
                .setDbVersion(1)
                .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                    @Override
                    public void onUpgrade(DbManager db, int oldVersion, int newVersion) {

                    }
                });
    }
}
