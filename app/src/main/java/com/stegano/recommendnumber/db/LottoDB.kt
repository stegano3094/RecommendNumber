package com.stegano.recommendnumber.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [LottoEntity::class], version = 1)
abstract class LottoDB : RoomDatabase() {
    abstract fun lottoEntityDAO() : LottoEntityDAO

    companion object {
        private var INSTANCE: LottoDB? = null

        // 싱글톤 패턴으로 DB 생성
        fun getInstance(context: Context) : LottoDB? {
            if(INSTANCE == null) {
                synchronized(LottoDB::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                    LottoDB::class.java, "loadedLottoNumber.db")
                        .createFromAsset("database/olddb.db")
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}