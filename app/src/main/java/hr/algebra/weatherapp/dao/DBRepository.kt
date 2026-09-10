package hr.algebra.weatherapp.dao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import hr.algebra.weatherapp.model.LocationItem

private const val DB_NAME = "LocationItems.db"
private const val DB_VERSION = 1
private const val TABLE_NAME = "locationItems"
private val CREATE_TABLE = "create table $TABLE_NAME( " +
        "${LocationItem::_id.name} integer primary key autoincrement, " +
        "${LocationItem::cityName.name} text not null, " +
        "${LocationItem::temperature.name} real not null, " +
        "${LocationItem::feelsLike.name} real not null, " +
        "${LocationItem::humidity.name} integer not null, " +
        "${LocationItem::pressure.name} integer not null, " +
        "${LocationItem::windSpeed.name} real not null, " +
        "${LocationItem::description.name} text not null, " +
        "${LocationItem::isFavorite.name} integer not null" +
        ")"
private const val DROP_TABLE = "drop table $TABLE_NAME"

class DBRepository(context: Context?) :
    SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION)
    ,Repository {
    override fun delete(
        selection: String?,
        selectionArgs: Array<String>?
    ): Int = writableDatabase.delete(
        TABLE_NAME,
        selection,
        selectionArgs
    )

    override fun update(
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<String>?
    ): Int = writableDatabase.update(
        TABLE_NAME,
        values,
        selection,
        selectionArgs
    )

    override fun query(
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor = readableDatabase.query(
        TABLE_NAME,
        projection,
        selection,
        selectionArgs,
        null,
        null,
        sortOrder
    )

    override fun insert(values: ContentValues?): Long
        = writableDatabase.insert(
            TABLE_NAME,
            null,
            values
        )

    override fun onCreate(db: SQLiteDatabase?) {

        db?.execSQL(CREATE_TABLE)

    }

    override fun onUpgrade(
        db: SQLiteDatabase?,
        oldVersion: Int,
        newVersion: Int
    ) {
        db?.execSQL(DROP_TABLE)
        onCreate(db)
    }
}