package co.prandroid.wishbirthday

/**
 * Created by dharmakshetri on 8/28/17.
 */
import android.os.Environment
import android.util.Log

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.channels.FileChannel
import java.util.ArrayList
import java.util.Calendar
import java.util.Date
import java.util.TimeZone


class Birthday {


    var name: String?=null
    var birthday_ID: Int = 0
    var email: String? = null
    var mobile: String? = null
    var date: String? = null
    var category: String? = null
    var notification: String? = null
    var favourite: Int = 0

    var category_ID: Int = 0
    var catName: String? = null

    var notification_ID: Int = 0
    var notificationName: String? = null
    var notificationTime: String? = null


    var packageName: String? = null

    companion object {
        // Labels table name
        val TABLE_BIRTHDAY = "Birthday"

        // Labels Table Columns names
        val KEY_ID = "id"
        val KEY_name = "name"
        val KEY_email = "email"
        val KEY_mobile = "mobile"
        val KEY_date = "date"
        val KEY_category = "category"
        val KEY_notifications = "notification"
        val KEY_favourite = "favourite"


        val TABLE_CATEGORY = "Category"
        val KEY_catId = "id"
        val KEY_canName = "catname"

        val TABLE_NOTIFICATION = "Botification"
        val KEY_notId = "id"
        val KEY_notName = "name"
        val KStudentEY_time = "time"
        var name: String? = null

        var currentDay: Int = 0
        var currentMonth: Int = 0
        var currentYear: Int = 0
        var daysInMonth: Int = 0


        val all = "All"
        val friends = "Friend"
        val relative = "Relative"
        val colleague = "Colleague"
        val other = "Other"
        val family = "Family"

        val Status = "status"
        var Edit = "edit"

        var arrayMonths: Array<String>? = null
        var arrayDays: Array<String>? = null
        var arraysCategory: Array<String>? = null
        var arraysMessages: Array<String>? = null
        var arrayNotificationCheckList: List<String> = ArrayList()

        //    public Student(Context mcontext) {
        //        super();
        //        //packageName = mcontext.getPackageName();
        //    }

        fun importDB(newDbPath: String, newDbName: String): Boolean {
            var tmp = false
            try {
                // TODO Auto-generated method stub
                val sd = Environment.getExternalStorageDirectory()
                val data = Environment.getDataDirectory()

                Log.e("SD", "sd.canWrite()=" + sd.canWrite())
                if (sd.canWrite()) {
                    // First Create a new database

                    val currentDBPath = "//data/data/" + "co.happybirthday" + "//databases//happybirth.db"
                    val backupDBPath = newDbPath + newDbName

                    val currentDB = File(currentDBPath)
                    val backupDB = File(backupDBPath)

                    val src = FileInputStream(currentDB).channel
                    val dst = FileOutputStream(backupDB).channel
                    dst.transferFrom(src, 0, src.size())
                    src.close()
                    dst.close()
                    tmp = true

                }
            } catch (e: Exception) {
                //Common.CreateLogFile("ImportExport", e);
                tmp = false
            }

            return tmp
        }

        fun getMonth(strmonth: String): String {

            val intmonth = Integer.parseInt(strmonth)
            var month = ""
            when (intmonth) {
                1 -> month = Birthday.arrayMonths!![0].toString()
                2 -> month = Birthday.arrayMonths!![1].toString()
                3 -> month = Birthday.arrayMonths!![2].toString()
                4 -> month = Birthday.arrayMonths!![3].toString()
                5 -> month = Birthday.arrayMonths!![4].toString()
                6 -> month = Birthday.arrayMonths!![5].toString()
                7 -> month = Birthday.arrayMonths!![6].toString()
                8 -> month = Birthday.arrayMonths!![7].toString()
                9 -> month = Birthday.arrayMonths!![8].toString()
                10 -> month = Birthday.arrayMonths!![9].toString()
                11 -> month = Birthday.arrayMonths!![10].toString()
                12 -> month = Birthday.arrayMonths!![11].toString()
            }

            return month

        }

        fun getCategoryId(cat: String): Int {
            var catid = 0

            if (cat.equals(Birthday.friends, ignoreCase = true)) {
                catid = 0
            } else if (cat.equals(Birthday.family, ignoreCase = true)) {
                catid = 1
            } else if (cat.equals(Birthday.relative, ignoreCase = true)) {
                catid = 2
            } else if (cat.equals(Birthday.colleague, ignoreCase = true)) {
                catid = 3
            } else if (cat.equals(Birthday.other, ignoreCase = true)) {
                catid = 4
            }

            return catid
        }

        fun getTimes() {
            val localCalendar = Calendar.getInstance(TimeZone.getDefault())

            val currentTime = localCalendar.time
            currentDay = localCalendar.get(Calendar.DATE)
            currentMonth = localCalendar.get(Calendar.MONTH) + 1
            currentYear = localCalendar.get(Calendar.YEAR)
            val currentDayOfWeek = localCalendar.get(Calendar.DAY_OF_WEEK)
            val currentDayOfMonth = localCalendar.get(Calendar.DAY_OF_MONTH)
            val CurrentDayOfYear = localCalendar.get(Calendar.DAY_OF_YEAR)
            daysInMonth = localCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)

            //getting time, date, day of week and other details in GMT timezone
            val gmtCalendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"))


        }
    }
}