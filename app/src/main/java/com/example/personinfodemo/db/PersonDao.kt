package com.example.personinfodemo.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.example.personinfodemo.model.Person

class PersonDao(context: Context) {
    private val dbHelper = DBHelper(context)
    private val db = dbHelper.writableDatabase

    // 插入数据
    fun insertPerson(person: Person): Boolean {
        val values = ContentValues().apply {
            put("id", person.id)
            put("name", person.name)
            put("age", person.age)
            put("height", person.height)
        }
        val result = db.insert("info", null, values)
        return result != -1L
    }

    // 根据ID删除
    fun deleteById(id: Int): Boolean {
        val rows = db.delete("info", "id=?", arrayOf(id.toString()))
        return rows > 0
    }

    // 根据ID更新
    fun updatePerson(person: Person): Boolean {
        val values = ContentValues().apply {
            put("name", person.name)
            put("age", person.age)
            put("height", person.height)
        }
        val rows = db.update("info", values, "id=?", arrayOf(person.id.toString()))
        return rows > 0
    }

    // 查询所有数据
    fun getAllPersons(): List<Person> {
        val list = mutableListOf<Person>()
        val cursor = db.query("info", null, null, null, null, null, null)
        with(cursor) {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow("id"))
                val name = getString(getColumnIndexOrThrow("name"))
                val age = getInt(getColumnIndexOrThrow("age"))
                val height = getFloat(getColumnIndexOrThrow("height"))
                list.add(Person(id, name, age, height))
            }
            close()
        }
        return list
    }

    // 根据ID查询单条
    fun getPersonById(id: Int): Person? {
        val cursor = db.query(
            "info", null, "id=?", arrayOf(id.toString()),
            null, null, null
        )
        var person: Person? = null
        if (cursor.moveToFirst()) {
            val name = cursor.getString(1)
            val age = cursor.getInt(2)
            val height = cursor.getFloat(3)
            person = Person(id, name, age, height)
        }
        cursor.close()
        return person
    }

    // 给 ContentProvider 使用的游标方法
    fun getAllPersonsCursor(): Cursor {
        return db.rawQuery("SELECT * FROM info", null) // 直接用上面的 db
    }

    fun insertPersonById(id: Int?, name: String?, age: Int?, height: Float?) {
        if (id == null || name.isNullOrBlank() || age == null || height == null) return
        val values = ContentValues().apply {
            put("id", id)
            put("name", name)
            put("age", age)
            put("height", height)
        }
        db.insert("info", null, values)
    }

    fun updatePersonById(id: Int, name: String?, age: Int?, height: Float?): Boolean {
        val values = ContentValues().apply {
            if (!name.isNullOrBlank()) put("name", name)
            if (age != null) put("age", age)
            if (height != null) put("height", height)
        }
        val rows = db.update("info", values, "id=?", arrayOf(id.toString()))
        return rows > 0
    }

    // 关闭数据库
    fun close() {
        db.close()
        dbHelper.close()
    }
}