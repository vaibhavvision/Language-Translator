package com.translatealll.anguagesapp.database;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

public abstract class RoomDB extends RoomDatabase {
    private static RoomDB roomDB_instance;

    public static synchronized RoomDB getRoomDBInstance(Context context) {
        RoomDB roomDB;
        synchronized (RoomDB.class) {
            if (roomDB_instance == null) {
                roomDB_instance = (RoomDB) Room.databaseBuilder(context.getApplicationContext(), RoomDB.class, "TranslatorDatabase").allowMainThreadQueries().fallbackToDestructiveMigration().build();
            }
            roomDB = roomDB_instance;
        }
        return roomDB;
    }

    public abstract Downloadedlngs_dao downloadedlngs_dao();
}
