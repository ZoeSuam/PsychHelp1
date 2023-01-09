package com.example.myapplication

class Contact(val id: Int, val name: String, val place: String) {

    companion object {
        const val TABLE_NAME = "contacts"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_PLACE = "place"
    }
}
