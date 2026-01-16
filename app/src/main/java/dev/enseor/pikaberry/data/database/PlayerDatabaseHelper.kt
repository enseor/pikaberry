package dev.enseor.pikaberry.data.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class PlayerDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "players.db"
        private const val DATABASE_VERSION = 2

        const val TABLE_NAME = "players"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_SCORE = "score"
        const val COLUMN_LEVEL = "level"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT NOT NULL,
                $COLUMN_SCORE INTEGER NOT NULL,
                $COLUMN_LEVEL TEXT NOT NULL
            )
        """
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addScore(name: String, score: Int, level: String) {
        val db = this.writableDatabase
        val query = "INSERT INTO $TABLE_NAME ($COLUMN_NAME, $COLUMN_SCORE, $COLUMN_LEVEL) VALUES (?, ?, ?)"
        db.execSQL(query, arrayOf(name, score, level))
        db.close()
    }

    fun getScoresByLevel(level: String): List<Pair<String, Int>> {
        val list = mutableListOf<Pair<String, Int>>()
        val db = this.readableDatabase

        val cursor = db.query(
            TABLE_NAME,
            arrayOf(COLUMN_NAME, COLUMN_SCORE),
            "$COLUMN_LEVEL = ?",
            arrayOf(level),
            null, null,
            "$COLUMN_SCORE DESC",
            "10" // Top 10
        )

        if (cursor.moveToFirst()) {
            do {
                val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
                val score = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SCORE))
                list.add(name to score)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return list
    }
}