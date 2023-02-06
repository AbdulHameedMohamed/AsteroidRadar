package com.udacity.asteroidradar.database
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.udacity.asteroidradar.model.Asteroid
import com.udacity.asteroidradar.model.PictureOfDay

@Database(entities = [Asteroid::class, PictureOfDay::class], version = 1)
abstract class AsteroidDatabase : RoomDatabase() {
    abstract fun asteroidDao(): AsteroidDao
    abstract fun pictureDao(): PictureDayDao

    companion object {
        private var instance: AsteroidDatabase? = null

        fun getInstance(context: Context): AsteroidDatabase {
            if (instance == null) {
                instance = Room
                    .databaseBuilder(
                        context,
                        AsteroidDatabase::class.java,
                        "asteroid-db"
                    )
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return instance!!
        }
    }


}
