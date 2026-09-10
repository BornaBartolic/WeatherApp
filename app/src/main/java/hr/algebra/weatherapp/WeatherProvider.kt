package hr.algebra.weatherapp

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import androidx.core.net.toUri
import hr.algebra.weatherapp.dao.Repository
import hr.algebra.weatherapp.dao.getRepository
import hr.algebra.weatherapp.model.LocationItem

private const val AUTHORITY = "hr.algebra.weatherapp.provider"
private const val PATH = "items"
val WEATHER_PROVIDER_CONTENT_URI: Uri = "content://$AUTHORITY/$PATH".toUri()

private const val ITEMS = 10
private const val ITEM_ID = 20

private val URI_MATCHER = with(UriMatcher(UriMatcher.NO_MATCH)){
    addURI(AUTHORITY,PATH,ITEMS) //SVI ITEMI
    addURI(AUTHORITY,"$PATH/#",ITEM_ID)//1 item, # oznacava where
    this
}
class WeatherProvider : ContentProvider() {

    private lateinit var repository: Repository

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        when(URI_MATCHER.match(uri)){
            ITEMS -> return repository.delete(selection,selectionArgs)
            ITEM_ID -> {
                val id = uri.lastPathSegment
                if (id != null){
                    return repository.delete("${LocationItem::_id.name}=?",arrayOf(id))
                }
            }
        }


        throw IllegalArgumentException("WRONG URI")
    }


    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val id = repository.insert(values)
        return ContentUris.withAppendedId(WEATHER_PROVIDER_CONTENT_URI, id)
    }

    override fun onCreate(): Boolean {
        repository = getRepository(context)
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? = repository.query(
        projection,
        selection,
        selectionArgs,
        sortOrder
    )

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        when(URI_MATCHER.match(uri)){
            ITEMS -> return repository.update(values,selection,selectionArgs)
            ITEM_ID -> {
                val id = uri.lastPathSegment
                if (id != null){
                    return repository.update(values,"${LocationItem::_id.name}=?",arrayOf(id))
                }
            }
        }


        throw IllegalArgumentException("WRONG URI")
    }


    override fun getType(uri: Uri): String? {
        TODO(
            "Implement this to handle requests for the MIME type of the data" +
                    "at the given URI"
        )
    }
}