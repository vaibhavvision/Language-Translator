package com.translatealll.anguagesapp.database;

import android.annotation.SuppressLint;
import android.database.Cursor;

import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public final class DownloadedlngsDaoImpl implements Downloadedlngs_dao {
    private final EntityDeletionOrUpdateAdapter<ChatTable> __deletionAdapterOfChat_Table;
    private final EntityDeletionOrUpdateAdapter<WordsHistoryTable> __deletionAdapterOfWordsHistoryTable;
    private final EntityInsertionAdapter<ChatTable> __insertionAdapterOfChat_Table;
    private final EntityInsertionAdapter<DownloadedLngsTable> __insertionAdapterOfDownloadedLngs_Table;
    private final EntityInsertionAdapter<WordsHistoryTable> __insertionAdapterOfWordsHistoryTable;
    private final SharedSQLiteStatement __preparedStmtOfDelete;
    private final SharedSQLiteStatement __preparedStmtOfUpdateLngStatus;
    private RoomDatabase __db;

    @SuppressLint("RestrictedApi")
    public DownloadedlngsDaoImpl(RoomDatabase __db) {
        this.__db = __db;
        __insertionAdapterOfDownloadedLngs_Table = new EntityInsertionAdapter<DownloadedLngsTable>(__db) {
            @Override
            public String createQuery() {
                return "INSERT OR REPLACE INTO `DownloadedLngs_Table` (`id`,`downloadedlng_name`,`islang_downloaded`) VALUES (nullif(?, 0),?,?)";
            }

            @Override
            public void bind(SupportSQLiteStatement stmt, DownloadedLngsTable value) {
                stmt.bindLong(1, value.f555id);
                if (value.getDownloadedlng_name() == null) {
                    stmt.bindNull(2);
                } else {
                    stmt.bindString(2, value.getDownloadedlng_name());
                }
                if (value.getIslang_downloaded() == null) {
                    stmt.bindNull(3);
                } else {
                    stmt.bindString(3, value.getIslang_downloaded());
                }
            }
        };
        __insertionAdapterOfWordsHistoryTable = new EntityInsertionAdapter<WordsHistoryTable>(__db) {
            @Override
            public String createQuery() {
                return "INSERT OR REPLACE INTO `WordsHistoryTable` (`lngsId`,`language1`,`language2`,`texttotranslate`,`translatedtext`) VALUES (nullif(?, 0),?,?,?,?)";
            }

            @Override
            public void bind(SupportSQLiteStatement stmt, WordsHistoryTable value) {
                stmt.bindLong(1, value.lngsId);
                if (value.getLanguage1() == null) {
                    stmt.bindNull(2);
                } else {
                    stmt.bindString(2, value.getLanguage1());
                }
                if (value.getLanguage2() == null) {
                    stmt.bindNull(3);
                } else {
                    stmt.bindString(3, value.getLanguage2());
                }
                if (value.getTexttotranslate() == null) {
                    stmt.bindNull(4);
                } else {
                    stmt.bindString(4, value.getTexttotranslate());
                }
                if (value.getTranslatedtext() == null) {
                    stmt.bindNull(5);
                } else {
                    stmt.bindString(5, value.getTranslatedtext());
                }
            }
        };
        __insertionAdapterOfChat_Table = new EntityInsertionAdapter<ChatTable>(__db) {
            @Override
            public String createQuery() {
                return "INSERT OR REPLACE INTO `Chat_table` (`tableid`,`chatname`,`lang1`,`lang2`,`texttotranslate`,`translatedtext`,`user`) VALUES (nullif(?, 0),?,?,?,?,?,?)";
            }

            @Override // androidx.room.EntityInsertionAdapter
            public void bind(SupportSQLiteStatement stmt, ChatTable value) {
                stmt.bindLong(1, value.tableid);
                if (value.getChatname() == null) {
                    stmt.bindNull(2);
                } else {
                    stmt.bindString(2, value.getChatname());
                }
                if (value.getLang1() == null) {
                    stmt.bindNull(3);
                } else {
                    stmt.bindString(3, value.getLang1());
                }
                if (value.getLang2() == null) {
                    stmt.bindNull(4);
                } else {
                    stmt.bindString(4, value.getLang2());
                }
                if (value.getTexttotranslate() == null) {
                    stmt.bindNull(5);
                } else {
                    stmt.bindString(5, value.getTexttotranslate());
                }
                if (value.getTranslatedtext() == null) {
                    stmt.bindNull(6);
                } else {
                    stmt.bindString(6, value.getTranslatedtext());
                }
                if (value.getUser() == null) {
                    stmt.bindNull(7);
                } else {
                    stmt.bindString(7, value.getUser());
                }
            }
        };
        __deletionAdapterOfChat_Table = new EntityDeletionOrUpdateAdapter<ChatTable>(__db) {
            @Override
            public String createQuery() {
                return "DELETE FROM `Chat_table` WHERE `tableid` = ?";
            }

            @Override
            public void bind(SupportSQLiteStatement stmt, ChatTable value) {
                stmt.bindLong(1, value.tableid);
            }
        };
        __deletionAdapterOfWordsHistoryTable = new EntityDeletionOrUpdateAdapter<WordsHistoryTable>(__db) {
            @Override
            public String createQuery() {
                return "DELETE FROM `WordsHistoryTable` WHERE `lngsId` = ?";
            }

            @Override
            public void bind(SupportSQLiteStatement stmt, WordsHistoryTable value) {
                stmt.bindLong(1, value.lngsId);
            }
        };
        __preparedStmtOfUpdateLngStatus = new SharedSQLiteStatement(__db) {
            @Override
            public String createQuery() {
                return "UPDATE DownloadedLngs_Table SET islang_downloaded =? WHERE downloadedlng_name LIKE ?";
            }
        };
        __preparedStmtOfDelete = new SharedSQLiteStatement(__db) {
            @Override
            public String createQuery() {
                return "DELETE FROM chat_table WHERE chatname LIKE ?";
            }
        };
    }

    public static List<Class<?>> getRequiredConverters() {
        return Collections.emptyList();
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void InsertDownloaded_lngs(final DownloadedLngsTable downloadedLngs_table) {
        __db.assertNotSuspendingTransaction();
        __db.beginTransaction();
        try {
            __insertionAdapterOfDownloadedLngs_Table.insert(downloadedLngs_table);
            __db.setTransactionSuccessful();
        } finally {
            __db.endTransaction();
        }
    }

    @SuppressLint("RestrictedApi")
    @Override
    public long insert_lngs(final WordsHistoryTable qr) {
        __db.assertNotSuspendingTransaction();
        __db.beginTransaction();
        try {
            long insertAndReturnId = __insertionAdapterOfWordsHistoryTable.insertAndReturnId(qr);
            __db.setTransactionSuccessful();
            return insertAndReturnId;
        } finally {
            __db.endTransaction();
        }
    }

    @SuppressLint("RestrictedApi")
    @Override
    public long insert_chat(final ChatTable ct) {
        __db.assertNotSuspendingTransaction();
        __db.beginTransaction();
        try {
            @SuppressLint("RestrictedApi") long insertAndReturnId = __insertionAdapterOfChat_Table.insertAndReturnId(ct);
            __db.setTransactionSuccessful();
            return insertAndReturnId;
        } finally {
            __db.endTransaction();
        }
    }

    @SuppressLint("RestrictedApi")
    @Override
    public int deletechat(final ChatTable ct) {
        __db.assertNotSuspendingTransaction();
        __db.beginTransaction();
        try {
            int handle = __deletionAdapterOfChat_Table.handle(ct);
            __db.setTransactionSuccessful();
            return handle;
        } finally {
            __db.endTransaction();
        }
    }

    @SuppressLint("RestrictedApi")
    @Override
    public int deleteword(final WordsHistoryTable lt) {
        __db.assertNotSuspendingTransaction();
        __db.beginTransaction();
        try {
            int handle = __deletionAdapterOfWordsHistoryTable.handle(lt);
            __db.setTransactionSuccessful();
            return handle;
        } finally {
            __db.endTransaction();
        }
    }

    @SuppressLint("RestrictedApi")
    @Override
    public int UpdateLngStatus(final String status, final String downloadedlng_name) {
        __db.assertNotSuspendingTransaction();
        SupportSQLiteStatement acquire = __preparedStmtOfUpdateLngStatus.acquire();
        if (status == null) {
            acquire.bindNull(1);
        } else {
            acquire.bindString(1, status);
        }
        if (downloadedlng_name == null) {
            acquire.bindNull(2);
        } else {
            acquire.bindString(2, downloadedlng_name);
        }
        __db.beginTransaction();
        try {
            int executeUpdateDelete = acquire.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return executeUpdateDelete;
        } finally {
            __db.endTransaction();
            __preparedStmtOfUpdateLngStatus.release(acquire);
        }
    }

    @SuppressLint("RestrictedApi")
    @Override
    public int delete(final String item) {
        __db.assertNotSuspendingTransaction();
        SupportSQLiteStatement acquire = __preparedStmtOfDelete.acquire();
        if (item == null) {
            acquire.bindNull(1);
        } else {
            acquire.bindString(1, item);
        }
        __db.beginTransaction();
        try {
            int executeUpdateDelete = acquire.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return executeUpdateDelete;
        } finally {
            __db.endTransaction();
            __preparedStmtOfDelete.release(acquire);
        }
    }

    @SuppressLint("RestrictedApi")
    @Override
    public List<DownloadedLngsTable> SelectDownloadedLngs() {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT * FROM DownloadedLngs_Table", 0);
        __db.assertNotSuspendingTransaction();
        @SuppressLint("RestrictedApi") Cursor query = DBUtil.query(__db, acquire, false, null);
        try {
            @SuppressLint("RestrictedApi") int columnIndexOrThrow = CursorUtil.getColumnIndexOrThrow(query, "id");
            @SuppressLint("RestrictedApi") int columnIndexOrThrow2 = CursorUtil.getColumnIndexOrThrow(query, "downloadedlng_name");
            @SuppressLint("RestrictedApi") int columnIndexOrThrow3 = CursorUtil.getColumnIndexOrThrow(query, "islang_downloaded");
            ArrayList arrayList = new ArrayList(query.getCount());
            while (query.moveToNext()) {
                DownloadedLngsTable downloadedLngs_Table = new DownloadedLngsTable();
                downloadedLngs_Table.f555id = query.getInt(columnIndexOrThrow);
                downloadedLngs_Table.setDownloadedlng_name(query.isNull(columnIndexOrThrow2) ? null : query.getString(columnIndexOrThrow2));
                downloadedLngs_Table.setIslang_downloaded(query.isNull(columnIndexOrThrow3) ? null : query.getString(columnIndexOrThrow3));
                arrayList.add(downloadedLngs_Table);
            }
            return arrayList;
        } finally {
            query.close();
            acquire.release();
        }
    }

    @SuppressLint("RestrictedApi")
    @Override
    public WordsHistoryTable[] selectalllngs() {
        int i = 0;
        @SuppressLint("RestrictedApi") RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT * FROM WordsHistoryTable", 0);
        __db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(__db, acquire, false, null);
        try {
            int columnIndexOrThrow = CursorUtil.getColumnIndexOrThrow(query, "lngsId");
            int columnIndexOrThrow2 = CursorUtil.getColumnIndexOrThrow(query, "language1");
            int columnIndexOrThrow3 = CursorUtil.getColumnIndexOrThrow(query, "language2");
            int columnIndexOrThrow4 = CursorUtil.getColumnIndexOrThrow(query, "texttotranslate");
            int columnIndexOrThrow5 = CursorUtil.getColumnIndexOrThrow(query, "translatedtext");
            WordsHistoryTable[] wordsHistoryTableArr = new WordsHistoryTable[query.getCount()];
            while (query.moveToNext()) {
                WordsHistoryTable wordsHistoryTable = new WordsHistoryTable();
                wordsHistoryTable.lngsId = query.getInt(columnIndexOrThrow);
                wordsHistoryTable.setLanguage1(query.isNull(columnIndexOrThrow2) ? null : query.getString(columnIndexOrThrow2));
                wordsHistoryTable.setLanguage2(query.isNull(columnIndexOrThrow3) ? null : query.getString(columnIndexOrThrow3));
                wordsHistoryTable.setTexttotranslate(query.isNull(columnIndexOrThrow4) ? null : query.getString(columnIndexOrThrow4));
                wordsHistoryTable.setTranslatedtext(query.isNull(columnIndexOrThrow5) ? null : query.getString(columnIndexOrThrow5));
                wordsHistoryTableArr[i] = wordsHistoryTable;
                i++;
            }
            return wordsHistoryTableArr;
        } finally {
            query.close();
            acquire.release();
        }
    }

    @SuppressLint("RestrictedApi")
    @Override
    public ChatTable[] selectallchats() {
        int i = 0;
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT * FROM Chat_table ", 0);
        __db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(__db, acquire, false, null);
        try {
            int columnIndexOrThrow = CursorUtil.getColumnIndexOrThrow(query, "tableid");
            int columnIndexOrThrow2 = CursorUtil.getColumnIndexOrThrow(query, "chatname");
            int columnIndexOrThrow3 = CursorUtil.getColumnIndexOrThrow(query, "lang1");
            int columnIndexOrThrow4 = CursorUtil.getColumnIndexOrThrow(query, "lang2");
            int columnIndexOrThrow5 = CursorUtil.getColumnIndexOrThrow(query, "texttotranslate");
            int columnIndexOrThrow6 = CursorUtil.getColumnIndexOrThrow(query, "translatedtext");
            int columnIndexOrThrow7 = CursorUtil.getColumnIndexOrThrow(query, "user");
            ChatTable[] chat_TableArr = new ChatTable[query.getCount()];
            while (query.moveToNext()) {
                ChatTable chat_Table = new ChatTable(query.isNull(columnIndexOrThrow3) ? null : query.getString(columnIndexOrThrow3), query.isNull(columnIndexOrThrow4) ? null : query.getString(columnIndexOrThrow4), query.isNull(columnIndexOrThrow5) ? null : query.getString(columnIndexOrThrow5), query.isNull(columnIndexOrThrow6) ? null : query.getString(columnIndexOrThrow6), query.isNull(columnIndexOrThrow7) ? null : query.getString(columnIndexOrThrow7), query.isNull(columnIndexOrThrow2) ? null : query.getString(columnIndexOrThrow2));
                chat_Table.tableid = query.getInt(columnIndexOrThrow);
                chat_TableArr[i] = chat_Table;
                i++;
            }
            return chat_TableArr;
        } finally {
            query.close();
            acquire.release();
        }
    }

    @SuppressLint("RestrictedApi")
    @Override
    public String[] getSavedChats() {
        @SuppressLint("RestrictedApi") RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT DISTINCT chatname FROM Chat_table ORDER BY chatname", 0);
        __db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(__db, acquire, false, null);
        try {
            String[] strArr = new String[query.getCount()];
            int i = 0;
            while (query.moveToNext()) {
                strArr[i] = query.isNull(0) ? null : query.getString(0);
                i++;
            }
            return strArr;
        } finally {
            query.close();
            acquire.release();
        }
    }

    @SuppressLint("RestrictedApi")
    @Override
    public List<ChatTable> specificchat(final String chatname) {
        @SuppressLint("RestrictedApi") RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT * FROM Chat_table WHERE chatname LIKE ?", 1);
        if (chatname == null) {
            acquire.bindNull(1);
        } else {
            acquire.bindString(1, chatname);
        }
        __db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(__db, acquire, false, null);
        try {
            int columnIndexOrThrow = CursorUtil.getColumnIndexOrThrow(query, "tableid");
            int columnIndexOrThrow2 = CursorUtil.getColumnIndexOrThrow(query, "chatname");
            int columnIndexOrThrow3 = CursorUtil.getColumnIndexOrThrow(query, "lang1");
            int columnIndexOrThrow4 = CursorUtil.getColumnIndexOrThrow(query, "lang2");
            int columnIndexOrThrow5 = CursorUtil.getColumnIndexOrThrow(query, "texttotranslate");
            int columnIndexOrThrow6 = CursorUtil.getColumnIndexOrThrow(query, "translatedtext");
            int columnIndexOrThrow7 = CursorUtil.getColumnIndexOrThrow(query, "user");
            ArrayList arrayList = new ArrayList(query.getCount());
            while (query.moveToNext()) {
                ChatTable chat_Table = new ChatTable(query.isNull(columnIndexOrThrow3) ? null : query.getString(columnIndexOrThrow3), query.isNull(columnIndexOrThrow4) ? null : query.getString(columnIndexOrThrow4), query.isNull(columnIndexOrThrow5) ? null : query.getString(columnIndexOrThrow5), query.isNull(columnIndexOrThrow6) ? null : query.getString(columnIndexOrThrow6), query.isNull(columnIndexOrThrow7) ? null : query.getString(columnIndexOrThrow7), query.isNull(columnIndexOrThrow2) ? null : query.getString(columnIndexOrThrow2));
                chat_Table.tableid = query.getInt(columnIndexOrThrow);
                arrayList.add(chat_Table);
            }
            return arrayList;
        } finally {
            query.close();
            acquire.release();
        }
    }
}
