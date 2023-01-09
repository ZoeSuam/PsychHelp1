package com.example.myapplication

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

private const val DATABASE_NAME = "contacts.db"
private const val DATABASE_VERSION = 1

class ContactsDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val createTableSql = "CREATE TABLE ${Contact.TABLE_NAME} (" +
                "${Contact.COLUMN_ID} INTEGER PRIMARY KEY," +
                "${Contact.COLUMN_NAME} TEXT," +
                "${Contact.COLUMN_PLACE} TEXT" +
                ")"
        db.execSQL(createTableSql)

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

        val dropTableSql = "DROP TABLE IF EXISTS ${Contact.TABLE_NAME}"
        db.execSQL(dropTableSql)
        onCreate(db)
    }

    fun insertContact(contact: Contact): Long {
        val db = writableDatabase
        val contentValues = ContentValues()
        contentValues.put(Contact.COLUMN_NAME, contact.name)
        contentValues.put(Contact.COLUMN_PLACE, contact.place)
        return db.insert(Contact.TABLE_NAME, null, contentValues)

    }

    fun deleteContact(id: Int): Int {
        val db = writableDatabase
        val selection = "${Contact.COLUMN_ID} = ?"
        val selectionArgs = arrayOf(id.toString())
        return db.delete(Contact.TABLE_NAME, selection, selectionArgs)
    }

    fun getContacts(): List<Contact> {

        val db = readableDatabase
        val cursor = db.query(
            Contact.TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            null
        )

        val contacts = mutableListOf<Contact>()
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(Contact.COLUMN_ID))
            val name = cursor.getString(cursor.getColumnIndexOrThrow(Contact.COLUMN_NAME))
            val place = cursor.getString(cursor.getColumnIndexOrThrow(Contact.COLUMN_PLACE))
            val contact = Contact(id, name, place)
            contacts.add(contact)
        }
        cursor.close()
        return contacts
    }

    fun searchContacts(query: String): List<Contact> {

        val db = readableDatabase
        val selection = "${Contact.COLUMN_NAME} LIKE ? OR ${Contact.COLUMN_PLACE} LIKE ?"
        val selectionArgs = arrayOf("%$query%", "%$query%")
        val cursor = db.query(
            Contact.TABLE_NAME,
            null,
            selection,
            selectionArgs,
            null,
            null,
            null
        )

        val contacts = mutableListOf<Contact>()
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(Contact.COLUMN_ID))
            val name = cursor.getString(cursor.getColumnIndexOrThrow(Contact.COLUMN_NAME))
            val place = cursor.getString(cursor.getColumnIndexOrThrow(Contact.COLUMN_PLACE))
            val contact = Contact(id, name, place)
            contacts.add(contact)
        }
        cursor.close()
        return contacts
    }
}