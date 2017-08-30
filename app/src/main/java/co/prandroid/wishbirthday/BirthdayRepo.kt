package co.prandroid.wishbirthday

/**
 * Created by dharmakshetri on 8/28/17.
 */
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log

import java.util.ArrayList
import java.util.HashMap

class BirthdayRepo(context: Context) {
    var dbHelper: DBHelper? = null

    init {
        dbHelper = DBHelper(context)
    }

    fun insert(birthday: Birthday): Int {
        // TODO Auto-generated method stub

        val db = dbHelper?.getWritableDatabase()
        val values = ContentValues()
        values.put(Birthday.KEY_date, birthday.date)
        values.put(Birthday.KEY_email, birthday.email)
        values.put(Birthday.KEY_name, birthday.name)
        values.put(Birthday.KEY_mobile, birthday.mobile)
        values.put(Birthday.KEY_category, birthday.category)
        values.put(Birthday.KEY_favourite, birthday.favourite)
        values.put(Birthday.KEY_notifications, birthday.notification)

        // Inserting Row
        val birthday_Id = db?.insert(Birthday.TABLE_BIRTHDAY, null, values)
        db?.close() // Closing database connection
        return birthday_Id?.toInt()!!
    }

    fun delete(birthday_Id: Int) {
        //int student_Id = getFirstStudent();

        val db = dbHelper?.getWritableDatabase()
        db!!.delete(Birthday.TABLE_BIRTHDAY, Birthday.KEY_ID + "=" + birthday_Id, null)
        db.close() // Closing database connection
    }

    fun update(birthday: Birthday, sid: Int) {

        val db = dbHelper?.getWritableDatabase()
        val values = ContentValues()

        values.put(Birthday.KEY_date, birthday.date)
        values.put(Birthday.KEY_email, birthday.email)
        values.put(Birthday.KEY_name, birthday.name)
        values.put(Birthday.KEY_mobile, birthday.mobile)
        values.put(Birthday.KEY_category, birthday.category)
        values.put(Birthday.KEY_favourite, birthday.favourite)
        values.put(Birthday.KEY_notifications, birthday.notification)

        db!!.update(Birthday.TABLE_BIRTHDAY, values, Birthday.KEY_ID + "=" + sid, null)
        db.close() // Closing database connection
    }

    fun updateFavourite(fav: Int, sid: Int) {


        val db = dbHelper?.getWritableDatabase()
        val values = ContentValues()

        values.put(Birthday.KEY_favourite, fav)
        db!!.update(Birthday.TABLE_BIRTHDAY, values, Birthday.KEY_ID + "=" + sid, null)
        db.close() // Closing database connection
    }


     lateinit var  birthdayList: ArrayList<String>

    // get all data
    // looping through all rows and adding to list
    val all: ArrayList<String>
        get() {
            val db = dbHelper?.getReadableDatabase()
            val selectQuery = "SELECT  " +
                    Birthday.KEY_ID + "," +
                    Birthday.KEY_name + "," +
                    Birthday.KEY_email + "," +
                    Birthday.KEY_category + "," +
                    Birthday.KEY_notifications + "," +
                    Birthday.KEY_date +
                    " FROM " + Birthday.TABLE_BIRTHDAY



            val cursor = db!!.rawQuery(selectQuery, null)

            if (cursor.moveToFirst()) {
                do {
                    birthdayList.add(cursor.getString(cursor.getColumnIndex(Birthday.KEY_ID)) + "_"
                            + cursor.getString(cursor.getColumnIndex(Birthday.KEY_name))
                            + cursor.getString(cursor.getColumnIndex(Birthday.KEY_date))
                            + cursor.getString(cursor.getColumnIndex(Birthday.KEY_email))
                    )

                } while (cursor.moveToNext())
            }

            cursor.close()
            db.close()
            return birthdayList

        }

    // get all data and set in students

    //ArrayList studentList =new ArrayList();
    // looping through all rows and adding to list
    val allBirthday: ArrayList<Birthday>
        get() {
            val db = dbHelper?.getReadableDatabase()
            val selectQuery = "SELECT  " +
                    Birthday.KEY_ID + "," +
                    Birthday.KEY_name + "," +
                    Birthday.KEY_email + "," +
                    Birthday.KEY_mobile + "," +
                    Birthday.KEY_date + "," +
                    Birthday.KEY_category + "," +
                    Birthday.KEY_favourite + " , " +
                    Birthday.KEY_notifications +
                    " FROM " + Birthday.TABLE_BIRTHDAY + " ORDER BY " + Birthday.KEY_name + " ASC"
            val birthdayList = ArrayList<Birthday>()

            val cursor = db!!.rawQuery(selectQuery, null)

            if (cursor.moveToFirst()) {
                do {
                    val birthday = Birthday()
                    birthday.birthday_ID = cursor.getInt(cursor.getColumnIndex(Birthday.KEY_ID))
                    birthday.name = cursor.getString(cursor.getColumnIndex(Birthday.KEY_name))
                    birthday.email = cursor.getString(cursor.getColumnIndex(Birthday.KEY_email))
                    birthday.mobile = cursor.getString(cursor.getColumnIndex(Birthday.KEY_mobile))
                    birthday.category = cursor.getString(cursor.getColumnIndex(Birthday.KEY_category))
                    birthday.date = cursor.getString(cursor.getColumnIndex(Birthday.KEY_date))
                    birthday.favourite = cursor.getInt(cursor.getColumnIndex(Birthday.KEY_favourite))
                    birthday.notification = cursor.getString(cursor.getColumnIndex(Birthday.KEY_notifications))

                    birthdayList.add(birthday)

                } while (cursor.moveToNext())
            }

            cursor.close()
            db.close()
            return birthdayList

        }

    // get data  accorading to category and set in students

    fun getCategoryBirthDay(cat: String): ArrayList<Birthday> {
        val category = cat
        Log.e("category", "===" + cat)
        val db = dbHelper?.getReadableDatabase()
        val selectQuery = "SELECT  " +
                Birthday.KEY_ID + "," +
                Birthday.KEY_name + "," +
                Birthday.KEY_email + "," +
                Birthday.KEY_mobile + "," +
                Birthday.KEY_date + "," +
                Birthday.KEY_category + "," +
                Birthday.KEY_favourite + "," +
                Birthday.KEY_notifications +
                " FROM " + Birthday.TABLE_BIRTHDAY + " WHERE " + Birthday.KEY_category + "='" + category + "'"

        //ArrayList studentList =new ArrayList();
        val birthdayList = ArrayList<Birthday>()

        val cursor = db?.rawQuery(selectQuery, null)
        // looping through all rows and adding to list

        if (cursor!!.moveToFirst()) {
            do {
                val birthday = Birthday()
                Log.e("Category", " Frined= Name=" + cursor.getColumnIndex(Birthday.KEY_name))
                birthday.birthday_ID = cursor.getInt(cursor.getColumnIndex(Birthday.KEY_ID))
                birthday.name = cursor.getString(cursor.getColumnIndex(Birthday.KEY_name))
                birthday.email = cursor.getString(cursor.getColumnIndex(Birthday.KEY_email))
                birthday.mobile = cursor.getString(cursor.getColumnIndex(Birthday.KEY_mobile))
                birthday.category = cursor.getString(cursor.getColumnIndex(Birthday.KEY_category))
                birthday.date = cursor.getString(cursor.getColumnIndex(Birthday.KEY_date))
                birthday.favourite = cursor.getInt(cursor.getColumnIndex(Birthday.KEY_favourite))
                birthday.notification = cursor.getString(cursor.getColumnIndex(Birthday.KEY_notifications))

                birthdayList.add(birthday)

            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return birthdayList

    }

    // get all favourite

    //ArrayList studentList =new ArrayList();
    // looping through all rows and adding to list
    val favouriteBirthDay: ArrayList<Birthday>
        get() {
            val db = dbHelper?.getReadableDatabase()
            val selectQuery = "SELECT  " +
                    Birthday.KEY_ID + "," +
                    Birthday.KEY_name + "," +
                    Birthday.KEY_email + "," +
                    Birthday.KEY_mobile + "," +
                    Birthday.KEY_date + "," +
                    Birthday.KEY_category + "," +
                    Birthday.KEY_favourite + "," +
                    Birthday.KEY_notifications +
                    " FROM " + Birthday.TABLE_BIRTHDAY + " WHERE " + Birthday.KEY_favourite + "=" + 1
            val birthdayList = ArrayList<Birthday>()

            val cursor = db?.rawQuery(selectQuery, null)

            if (cursor!!.moveToFirst()) {
                do {
                    val birthday = Birthday()
                    Log.e("Category", " Frined= Name=" + cursor.getColumnIndex(Birthday.KEY_name))
                    birthday.birthday_ID = cursor.getInt(cursor.getColumnIndex(Birthday.KEY_ID))
                    birthday.name = cursor.getString(cursor.getColumnIndex(Birthday.KEY_name))
                    birthday.email = cursor.getString(cursor.getColumnIndex(Birthday.KEY_email))
                    birthday.mobile = cursor.getString(cursor.getColumnIndex(Birthday.KEY_mobile))
                    birthday.category = cursor.getString(cursor.getColumnIndex(Birthday.KEY_category))
                    birthday.date = cursor.getString(cursor.getColumnIndex(Birthday.KEY_date))
                    birthday.favourite = cursor.getInt(cursor.getColumnIndex(Birthday.KEY_favourite))
                    birthday.notification = cursor.getString(cursor.getColumnIndex(Birthday.KEY_notifications))

                    birthdayList.add(birthday)

                } while (cursor.moveToNext())
            }

            cursor.close()
            db?.close()
            return birthdayList

        }

    //Student student = new Student();
    // looping through all rows and adding to list
    val studentList: ArrayList<HashMap<String, String>>
        get() {
            val db = dbHelper?.getReadableDatabase()
            val selectQuery = "SELECT  " +
                    Birthday.KEY_ID + "," +
                    Birthday.KEY_name + "," +
                    Birthday.KEY_email + "," +
                    Birthday.KEY_date +
                    " FROM " + Birthday.TABLE_BIRTHDAY
            val studentList = ArrayList<HashMap<String, String>>()


            val cursor = db!!.rawQuery(selectQuery, null)

            if (cursor.moveToFirst()) {
                do {
                    val student = HashMap<String, String>()
                    student.put("id", cursor.getString(cursor.getColumnIndex(Birthday.KEY_ID)))
                    student.put("name", cursor.getString(cursor.getColumnIndex(Birthday.KEY_name)))
                    studentList.add(student)

                } while (cursor.moveToNext())
            }

            cursor.close()
            db.close()
            return studentList

        }

    fun getStudentById(Id: Int): Birthday {
        val db = dbHelper?.getReadableDatabase()
        val selectQuery = "SELECT  " +
                Birthday.KEY_ID + "," +
                Birthday.KEY_name + "," +
                Birthday.KEY_email + "," +
                Birthday.KEY_mobile + " , " +
                Birthday.KEY_category + "," +
                Birthday.KEY_notifications + "," +
                Birthday.KEY_favourite + "," +
                Birthday.KEY_date +
                " FROM " + Birthday.TABLE_BIRTHDAY +" WHERE " +
                Birthday.KEY_ID + "=" + Id


        val iCount = 0
        val birthday = Birthday()

        val cursor = db!!.rawQuery(selectQuery, null)
        // looping through all rows and adding to list

        if (cursor.moveToFirst()) {
            do {
                birthday.birthday_ID = cursor.getInt(cursor.getColumnIndex(Birthday.KEY_ID))
                birthday.name = cursor.getString(cursor.getColumnIndex(Birthday.KEY_name))
                birthday.email = cursor.getString(cursor.getColumnIndex(Birthday.KEY_email))
                birthday.date = cursor.getString(cursor.getColumnIndex(Birthday.KEY_date))
                birthday.mobile = cursor.getString(cursor.getColumnIndex(Birthday.KEY_mobile))
                birthday.category = cursor.getString(cursor.getColumnIndex(Birthday.KEY_category))
                birthday.favourite = cursor.getInt(cursor.getColumnIndex(Birthday.KEY_favourite))
                birthday.notification = cursor.getString(cursor.getColumnIndex(Birthday.KEY_notifications))

            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return birthday
    }

    private // looping through all rows and adding to list
    val firstStudent: Int
        get() {
            val db = dbHelper?.getReadableDatabase()
            val selectQuery = "SELECT  " +
                    Birthday.KEY_ID +
                    " FROM " + Birthday.TABLE_BIRTHDAY +" LIMIT 1;"


            var student_Id = 0


            val cursor = db!!.rawQuery(selectQuery, null)

            if (cursor.moveToFirst()) {
                do {
                    student_Id = cursor.getInt(cursor.getColumnIndex(Birthday.KEY_ID))

                } while (cursor.moveToNext())
            }

            cursor.close()
            db!!.close()
            return student_Id
        }

    //" DESC LIMIT 1;" ;
    // looping through all rows and adding to list
    val lastBirthday: Int
        get() {
            val db = dbHelper?.getReadableDatabase()
            val selectQuery = "SELECT  " +
                    Birthday.KEY_ID +
                    " FROM " + Birthday.TABLE_BIRTHDAY +" ORDER BY " +
                    Birthday.KEY_ID + " DESC "


            var birthday_Id = 0


            val cursor = db!!.rawQuery(selectQuery, null)

            if (cursor.moveToFirst()) {
                do {
                    birthday_Id = cursor.getInt(cursor.getColumnIndex(Birthday.KEY_ID))
                    break
                } while (cursor.moveToNext())
            }

            cursor.close()
            db.close()
            return birthday_Id
        }


}