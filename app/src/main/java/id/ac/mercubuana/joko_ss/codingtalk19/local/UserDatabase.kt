package id.ac.mercubuana.joko_ss.codingtalk19.local

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import id.ac.mercubuana.joko_ss.codingtalk19.local.UserDatabase.Companion.DATABASE_VERSION
import id.ac.mercubuana.joko_ss.codingtalk19.model.User

@Database(entities = arrayOf(User::class), version = DATABASE_VERSION)
abstract class UserDatabase: RoomDatabase(){
    abstract fun userDAO(): UserDao
    companion object {
        const val DATABASE_VERSION = 1
        val DATABASE_NAME = "CodingTalk_Room"

        private var mInstance : UserDatabase? = null

        fun getIntance(context: Context): UserDatabase{
            if(mInstance == null){
                mInstance = Room.databaseBuilder(context, UserDatabase::class.java, DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build()

            }
            return mInstance!!
        }
    }
}