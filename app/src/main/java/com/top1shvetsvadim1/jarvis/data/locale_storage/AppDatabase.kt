package com.top1shvetsvadim1.jarvis.data.locale_storage

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.top1shvetsvadim1.jarvis.data.locale_storage.converts.ConvertsProductCompanies
import com.top1shvetsvadim1.jarvis.data.locale_storage.converts.IntTypeConverts
import com.top1shvetsvadim1.jarvis.data.locale_storage.genres.MovieGenreDB
import com.top1shvetsvadim1.jarvis.data.locale_storage.genres.MovieGenreDao
import com.top1shvetsvadim1.jarvis.data.locale_storage.movies_actors.MovieActorsDB
import com.top1shvetsvadim1.jarvis.data.locale_storage.movies_actors.MoviesActorsDao
import com.top1shvetsvadim1.jarvis.data.locale_storage.movies_details.MoviesDetailsDB
import com.top1shvetsvadim1.jarvis.data.locale_storage.movies_details.MoviesDetailsDao
import com.top1shvetsvadim1.jarvis.data.locale_storage.now_playing_storage.MovieNowPlayingDDModel
import com.top1shvetsvadim1.jarvis.data.locale_storage.now_playing_storage.MovieNowPlayingDao
import com.top1shvetsvadim1.jarvis.data.locale_storage.popular.MoviePopularDB
import com.top1shvetsvadim1.jarvis.data.locale_storage.popular.MoviePopularDao
import com.top1shvetsvadim1.jarvis.data.locale_storage.top_rated.MoviesTopRatedDB
import com.top1shvetsvadim1.jarvis.data.locale_storage.top_rated.MoviesTopRatedDao
import com.top1shvetsvadim1.jarvis.data.locale_storage.trending.MovieTrendDB
import com.top1shvetsvadim1.jarvis.data.locale_storage.trending.MovieTrendingDao
import com.top1shvetsvadim1.jarvis.data.locale_storage.trending.PeopleTrendDB
import com.top1shvetsvadim1.jarvis.data.locale_storage.trending.PeopleTrendingDao
import com.top1shvetsvadim1.jarvis.data.locale_storage.upcoming.MovieUpcomingDB
import com.top1shvetsvadim1.jarvis.data.locale_storage.upcoming.MoviesUpcomingDao


@Database(
    entities = [MovieNowPlayingDDModel::class, MovieGenreDB::class, MoviePopularDB::class, MoviesTopRatedDB::class,
        MovieUpcomingDB::class, MovieTrendDB::class, PeopleTrendDB::class, MoviesDetailsDB::class, MovieActorsDB::class],
    version = 2,
    exportSchema = true
)
@TypeConverters(
    IntTypeConverts::class,
    ConvertsProductCompanies::class
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun moviesNowPlayingDao(): MovieNowPlayingDao
    abstract fun moviesGenreDao(): MovieGenreDao
    abstract fun moviePopularDao(): MoviePopularDao
    abstract fun movieTopRatedDao(): MoviesTopRatedDao
    abstract fun movieUpcomingDao(): MoviesUpcomingDao
    abstract fun movieTrendingDao(): MovieTrendingDao
    abstract fun peopleTrendingDao(): PeopleTrendingDao
    abstract fun moviesDetailsDao(): MoviesDetailsDao
    abstract fun movieActorsDao(): MoviesActorsDao

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