package hr.algebra.weatherapp.dao

import android.content.ContentValues
import android.database.Cursor
import android.net.Uri

interface Repository {
     fun delete(selection: String?, selectionArgs: Array<String>?): Int



     fun update(values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int

     fun query(
        projection: Array<String>?, /*biramo npr. od 5 kolona koje 3 samo zelimo selectat*/
        selection: String?,
        selectionArgs: Array<String>?,
         sortOrder: String?
    ): Cursor   /*result/lista resultova, daj sljedeci..*/

     fun insert( values: ContentValues?): Long

}