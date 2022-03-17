package sicae.oise.ceos

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.util.*
import kotlin.collections.ArrayList

class BddCeos(context: Context?) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    // Creating Tables
    override fun onCreate(db: SQLiteDatabase) {
        // Category table create query
        val CREATE_ITEM_TABLE = ("CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_ID + " INTEGER," + COLUMN_NAME + " TEXT," + COLUMN_REF + " TEXT)")
        db.execSQL(CREATE_ITEM_TABLE)

    }

    // Upgrading database
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)

        // Create tables again
        onCreate(db)
    }



// Select All Commune
    /** Getting all Commune returns list of Commune  */
    val allCommune: List<String>
        get() {
            val commune: MutableList<String> = ArrayList()
            val selectQuery = "SELECT DISTINCT Commune_Nom FROM $TABLE_NAME ORDER BY Commune_Nom"
            val db = this.readableDatabase
            val cursor = db.rawQuery(selectQuery, null)


            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    commune.add(cursor.getString(0))
                } while (cursor.moveToNext())
            }

            // closing connection
            cursor.close()
            db.close();

            // returning lables
            return commune
        }



// Select All Poste
    /** Getting all Poste returns list of Poste by Commune choice  */
        fun getFindPosteByCommune(Commune: String) : List<String>{
        val poste: MutableList<String> = ArrayList()
        val selectQuery = "SELECT DISTINCT REF_PRAO FROM $TABLE_NAME WHERE Commune_Nom='$Commune' ORDER BY Commune_Nom"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()) {
            do {
                poste.add(cursor.getString(0))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close();
        return poste
    }


// Select Ref Wapiti
    /** Getting ref Wapiti by post name choice  */
        fun getFindRefWapitiByPoste(Poste: String) : List<String>{
        val refWapiti: MutableList<String> = ArrayList()
        val selectQuery = "SELECT DISTINCT Id_WAPITI FROM $TABLE_NAME WHERE REF_PRAO='$Poste' ORDER BY REF_PRAO"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()) {
            do {
                refWapiti.add(cursor.getString(0))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close();
        return refWapiti
    }


    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "BddCeos"
        private const val TABLE_NAME = "ceos_table"
        private const val COLUMN_ID = "Id_WAPITI"
        private const val COLUMN_NAME = "Commune_Nom"
        private const val COLUMN_REF = "Ref_PRAO"
    }
}