package com.translatealll.anguagesapp.database;

import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomMasterTable;
import androidx.room.RoomOpenHelper;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import java.util.HashMap;
import java.util.HashSet;


public final class RoomDB_Impl extends RoomDB {
    private volatile Downloadedlngs_dao _downloadedlngsDao;

    @Override
    protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
        return configuration.sqliteOpenHelperFactory.create(SupportSQLiteOpenHelper.Configuration.builder(configuration.context).name(configuration.name).callback(new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(8) { // from class: com.video.gttranslator.database.RoomDB_Impl.1
            @Override 
            public void onPostMigrate(SupportSQLiteDatabase _db) {
            }

            @Override 
            public void createAllTables(SupportSQLiteDatabase _db) {
                _db.execSQL("CREATE TABLE IF NOT EXISTS `WordsHistoryTable` (`lngsId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `language1` TEXT, `language2` TEXT, `texttotranslate` TEXT, `translatedtext` TEXT)");
                _db.execSQL("CREATE TABLE IF NOT EXISTS `Chat_table` (`tableid` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `chatname` TEXT, `lang1` TEXT, `lang2` TEXT, `texttotranslate` TEXT, `translatedtext` TEXT, `user` TEXT)");
                _db.execSQL("CREATE TABLE IF NOT EXISTS `DownloadedLngs_Table` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `downloadedlng_name` TEXT, `islang_downloaded` TEXT)");
                _db.execSQL(RoomMasterTable.CREATE_QUERY);
                _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '069b2766c3c226c6d06ab17c35ea13ea')");
            }

            @Override 
            public void dropAllTables(SupportSQLiteDatabase _db) {
                _db.execSQL("DROP TABLE IF EXISTS `WordsHistoryTable`");
                _db.execSQL("DROP TABLE IF EXISTS `Chat_table`");
                _db.execSQL("DROP TABLE IF EXISTS `DownloadedLngs_Table`");
                if (RoomDB_Impl.this.mCallbacks != null) {
                    int size = RoomDB_Impl.this.mCallbacks.size();
                    for (int i = 0; i < size; i++) {
                        ((Callback) RoomDB_Impl.this.mCallbacks.get(i)).onDestructiveMigration(_db);
                    }
                }
            }

            @Override 
            public void onCreate(SupportSQLiteDatabase _db) {
                if (RoomDB_Impl.this.mCallbacks != null) {
                    int size = RoomDB_Impl.this.mCallbacks.size();
                    for (int i = 0; i < size; i++) {
                        ((Callback) RoomDB_Impl.this.mCallbacks.get(i)).onCreate(_db);
                    }
                }
            }

            @Override 
            public void onOpen(SupportSQLiteDatabase _db) {
                RoomDB_Impl.this.mDatabase = _db;
                RoomDB_Impl.this.internalInitInvalidationTracker(_db);
                if (RoomDB_Impl.this.mCallbacks != null) {
                    int size = RoomDB_Impl.this.mCallbacks.size();
                    for (int i = 0; i < size; i++) {
                        ((Callback) RoomDB_Impl.this.mCallbacks.get(i)).onOpen(_db);
                    }
                }
            }

            @Override 
            public void onPreMigrate(SupportSQLiteDatabase _db) {
                DBUtil.dropFtsSyncTriggers(_db);
            }

            @Override 
            public RoomOpenHelper.ValidationResult onValidateSchema(SupportSQLiteDatabase _db) {
                HashMap hashMap = new HashMap(5);
                hashMap.put("lngsId", new TableInfo.Column("lngsId", "INTEGER", true, 1, null, 1));
                hashMap.put("language1", new TableInfo.Column("language1", "TEXT", false, 0, null, 1));
                hashMap.put("language2", new TableInfo.Column("language2", "TEXT", false, 0, null, 1));
                hashMap.put("texttotranslate", new TableInfo.Column("texttotranslate", "TEXT", false, 0, null, 1));
                hashMap.put("translatedtext", new TableInfo.Column("translatedtext", "TEXT", false, 0, null, 1));
                TableInfo tableInfo = new TableInfo("WordsHistoryTable", hashMap, new HashSet(0), new HashSet(0));
                TableInfo read = TableInfo.read(_db, "WordsHistoryTable");
                if (!tableInfo.equals(read)) {
                    return new RoomOpenHelper.ValidationResult(false, "WordsHistoryTable(com.video.gttranslator.database.WordsHistoryTable).\n Expected:\n" + tableInfo + "\n Found:\n" + read);
                }
                HashMap hashMap2 = new HashMap(7);
                hashMap2.put("tableid", new TableInfo.Column("tableid", "INTEGER", true, 1, null, 1));
                hashMap2.put("chatname", new TableInfo.Column("chatname", "TEXT", false, 0, null, 1));
                hashMap2.put("lang1", new TableInfo.Column("lang1", "TEXT", false, 0, null, 1));
                hashMap2.put("lang2", new TableInfo.Column("lang2", "TEXT", false, 0, null, 1));
                hashMap2.put("texttotranslate", new TableInfo.Column("texttotranslate", "TEXT", false, 0, null, 1));
                hashMap2.put("translatedtext", new TableInfo.Column("translatedtext", "TEXT", false, 0, null, 1));
                hashMap2.put("user", new TableInfo.Column("user", "TEXT", false, 0, null, 1));
                TableInfo tableInfo2 = new TableInfo("Chat_table", hashMap2, new HashSet(0), new HashSet(0));
                TableInfo read2 = TableInfo.read(_db, "Chat_table");
                if (!tableInfo2.equals(read2)) {
                    return new RoomOpenHelper.ValidationResult(false, "Chat_table(com.video.gttranslator.database.Chat_Table).\n Expected:\n" + tableInfo2 + "\n Found:\n" + read2);
                }
                HashMap hashMap3 = new HashMap(3);
                hashMap3.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, 1));
                hashMap3.put("downloadedlng_name", new TableInfo.Column("downloadedlng_name", "TEXT", false, 0, null, 1));
                hashMap3.put("islang_downloaded", new TableInfo.Column("islang_downloaded", "TEXT", false, 0, null, 1));
                TableInfo tableInfo3 = new TableInfo("DownloadedLngs_Table", hashMap3, new HashSet(0), new HashSet(0));
                TableInfo read3 = TableInfo.read(_db, "DownloadedLngs_Table");
                if (!tableInfo3.equals(read3)) {
                    return new RoomOpenHelper.ValidationResult(false, "DownloadedLngs_Table(com.video.gttranslator.database.DownloadedLngs_Table).\n Expected:\n" + tableInfo3 + "\n Found:\n" + read3);
                }
                return new RoomOpenHelper.ValidationResult(true, null);
            }
        }, "069b2766c3c226c6d06ab17c35ea13ea", "e6ff379e5bd874adf5341a019ad8a58b")).build());
    }

    @Override 
    protected InvalidationTracker createInvalidationTracker() {
        return new InvalidationTracker(this, new HashMap(0), new HashMap(0), "WordsHistoryTable", "Chat_table", "DownloadedLngs_Table");
    }

    @Override 
    public void clearAllTables() {
        super.assertNotMainThread();
        SupportSQLiteDatabase writableDatabase = super.getOpenHelper().getWritableDatabase();
        try {
            super.beginTransaction();
            writableDatabase.execSQL("DELETE FROM `WordsHistoryTable`");
            writableDatabase.execSQL("DELETE FROM `Chat_table`");
            writableDatabase.execSQL("DELETE FROM `DownloadedLngs_Table`");
            super.setTransactionSuccessful();
        } finally {
            super.endTransaction();
            writableDatabase.query("PRAGMA wal_checkpoint(FULL)").close();
            if (!writableDatabase.inTransaction()) {
                writableDatabase.execSQL("VACUUM");
            }
        }
    }
    
    @Override 
    public Downloadedlngs_dao downloadedlngs_dao() {
        Downloadedlngs_dao downloadedlngs_dao;
        if (this._downloadedlngsDao != null) {
            return this._downloadedlngsDao;
        }
        synchronized (this) {
            if (this._downloadedlngsDao == null) {
                this._downloadedlngsDao = new DownloadedlngsDaoImpl(this);
            }
            downloadedlngs_dao = this._downloadedlngsDao;
        }
        return downloadedlngs_dao;
    }
}
