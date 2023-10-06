package com.top1shvetsvadim1.jarvis.data.locale_storage

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.top1shvetsvadim1.jarvis.data.locale_storage.converts.IntTypeConverts
import com.top1shvetsvadim1.jarvis.data.locale_storage.genres.MovieGenreDB
import com.top1shvetsvadim1.jarvis.data.locale_storage.genres.MovieGenreDao
import com.top1shvetsvadim1.jarvis.data.locale_storage.now_playing_storage.MovieNowPlayingDDModel
import com.top1shvetsvadim1.jarvis.data.locale_storage.now_playing_storage.MovieNowPlayingDao
import com.top1shvetsvadim1.jarvis.data.locale_storage.popular.MoviePopularDB
import com.top1shvetsvadim1.jarvis.data.locale_storage.popular.MoviePopularDao


@Database(
    entities = [MovieNowPlayingDDModel::class, MovieGenreDB::class, MoviePopularDB::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(
    IntTypeConverts::class
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun moviesNowPlayingDao(): MovieNowPlayingDao
    abstract fun moviesGenreDao(): MovieGenreDao
    abstract fun moviePopularDao(): MoviePopularDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java, "movies_hub.db"
        ).fallbackToDestructiveMigration()
            .setJournalMode(JournalMode.AUTOMATIC)
            .build()
    }
}