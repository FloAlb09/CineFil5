package fr.epf.projet.cinefil5.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import fr.epf.projet.cinefil5.model.Favoris
import fr.epf.projet.cinefil5.model.ServiceDetailsResult

class FavorisDatabase(contextDb: Context) : SQLiteOpenHelper(contextDb, DB_NAME, null, DB_VERSION) {
    override fun onCreate(db: SQLiteDatabase?) {
        val createTableFavoris = """
            CREATE TABLE $FAVORIS_TABLE_NAME(
            $MOVIE_ID integer PRIMARY KEY,
            $MOVIE_POSTER_PATH varchar(50),
            $MOVIE_TITLE varchar (50),
            $MOVIE_ORIGINAL_TITLE varchar (50),
            $MOVIE_RELEASE_DATE varchar (10),
            $MOVIE_VOTE_AVERAGE float,            
            $MOVIE_OVERVIEW varchar(500),
            $MOVIE_LIKED boolean
            )
        """.trimIndent()
        db?.execSQL(createTableFavoris)

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $FAVORIS_TABLE_NAME")
        onCreate(db)
    }

    fun addFavoris(favoris: Favoris): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(MOVIE_ID, favoris.id)
        values.put(MOVIE_POSTER_PATH, favoris.posterPath)
        values.put(MOVIE_TITLE, favoris.title)
        values.put(MOVIE_ORIGINAL_TITLE, favoris.originalTitle)
        values.put(MOVIE_RELEASE_DATE, favoris.releaseDate)
        values.put(MOVIE_VOTE_AVERAGE, favoris.voteAverage)
        values.put(MOVIE_OVERVIEW, favoris.overview)
        Log.i("--FavorisDatabase-overview------------------------", favoris.overview)
        values.put(MOVIE_LIKED, 1)
        val result = db.insert(FAVORIS_TABLE_NAME, null, values).toInt()
        db.close()
        return result != -1
    }

    fun findFavoris(id: Int): ServiceDetailsResult? {
        var favoris: ServiceDetailsResult? = null
        val db = this.readableDatabase
        val selectionArgs = arrayOf(id.toString())
        val cursor =
            db.query(FAVORIS_TABLE_NAME, null, "$MOVIE_ID=?", selectionArgs, null, null, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(MOVIE_ID))
                val posterPath = cursor.getString(cursor.getColumnIndexOrThrow(MOVIE_POSTER_PATH))
                val title = cursor.getString(cursor.getColumnIndexOrThrow(MOVIE_TITLE))
                val originalTitle =
                    cursor.getString(cursor.getColumnIndexOrThrow(MOVIE_ORIGINAL_TITLE))
                val releaseDate = cursor.getString(cursor.getColumnIndexOrThrow(MOVIE_RELEASE_DATE))
                val voteAverage = cursor.getFloat(cursor.getColumnIndexOrThrow(MOVIE_VOTE_AVERAGE))
                val overview = cursor.getString(cursor.getColumnIndexOrThrow(MOVIE_OVERVIEW))
                val liked = cursor.getInt(cursor.getColumnIndexOrThrow(MOVIE_LIKED))
                val favoris = ServiceDetailsResult(
                    id, posterPath, title, originalTitle, releaseDate, voteAverage, overview, liked
                )
                return favoris
            }
        }
        db.close()
        return favoris
    }

    fun findAll(): List<Favoris> {
        var favoris = ArrayList<Favoris>()
        val db = this.readableDatabase
        val selectQuery = "SELECT * FROM $FAVORIS_TABLE_NAME"
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    val id = cursor.getInt(cursor.getColumnIndexOrThrow(MOVIE_ID))
                    val posterPath =
                        cursor.getString(cursor.getColumnIndexOrThrow(MOVIE_POSTER_PATH))
                    val title = cursor.getString(cursor.getColumnIndexOrThrow(MOVIE_TITLE))
                    val originalTitle =
                        cursor.getString(cursor.getColumnIndexOrThrow(MOVIE_ORIGINAL_TITLE))
                    val releaseDate =
                        cursor.getString(cursor.getColumnIndexOrThrow(MOVIE_RELEASE_DATE))
                    val voteAverage =
                        cursor.getFloat(cursor.getColumnIndexOrThrow(MOVIE_VOTE_AVERAGE))
                    val overview = cursor.getString(cursor.getColumnIndexOrThrow(MOVIE_OVERVIEW))
                    val liked = cursor.getInt(cursor.getColumnIndexOrThrow(MOVIE_LIKED))
                    val collection = Favoris(
                        id,
                        posterPath,
                        title,
                        originalTitle,
                        releaseDate,
                        voteAverage,
                        overview,
                        liked
                    )
                    favoris.add(collection)
                } while (cursor.moveToNext())
            }
        }
        db.close()
        return favoris
    }

    fun deleteFavoris(id: Int): Boolean {
        val db = writableDatabase
        val rowDeleted = db.delete(FAVORIS_TABLE_NAME, "movieId=?", arrayOf(id.toString()))
        return rowDeleted > 0
    }

    companion object {
        private val DB_NAME = "favorisDB"
        private val DB_VERSION = 9
        private val FAVORIS_TABLE_NAME = "favoris"
        private val MOVIE_ID = "movieId"
        private val MOVIE_POSTER_PATH = "moviePosterPath"
        private val MOVIE_TITLE = "movieTitle"
        private val MOVIE_ORIGINAL_TITLE = "movieOriginalTitle"
        private val MOVIE_RELEASE_DATE = "movieReleaseDate"
        private val MOVIE_VOTE_AVERAGE = "movieVoteAverage"
        private val MOVIE_OVERVIEW = "movieOverview"
        private val MOVIE_LIKED = "movieLiked"
    }
}