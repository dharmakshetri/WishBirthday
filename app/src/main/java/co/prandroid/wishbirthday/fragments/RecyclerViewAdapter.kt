package co.prandroid.wishbirthday.fragments

import android.content.Intent
import android.graphics.Color
import android.widget.TextView
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.prandroid.wishbirthday.R
import co.prandroid.wishbirthday.birthday.Birthday
import co.prandroid.wishbirthday.birthday.BirthdayDetails


/**
 * Created by dharmakshetri on 9/1/17.
 */
class RecyclerViewAdapter( val datas: ArrayList<Birthday>) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    var birthdayList = ArrayList<Birthday>()

    init {
        birthdayList = datas
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.birthdayrowitem, parent, false),birthdayList )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        //ListData currentListData = getItem(position);
        Birthday.getTimes()

        val date = birthdayList[position].date.toString()
        val splitArray = date.split("-".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
        val month = Birthday.getMonth(splitArray[0].toString())

        val birthMonth = Integer.parseInt(splitArray[0].toString())
        val birthDay = Integer.parseInt(splitArray[1].toString())

        //5068C2

        val differenceMonth = Birthday.currentMonth - birthMonth
        val differenceDay = Birthday.currentDay - birthDay
        //Student.currentMonth=6;
        //Student.currentDay=10;
        if (birthMonth == Birthday.currentMonth && birthDay == Birthday.currentDay) {
            holder.tvRTime!!.text = "Today is BirthDay, wish to click"
            holder.tvRTime!!.setTextColor(Color.parseColor("#5068C2"))
        } else if (birthMonth > Birthday.currentMonth && birthDay > Birthday.currentDay) {
            holder.tvRTime!!.setTextColor(Color.parseColor("#5068C2"))
            holder.tvRTime!!.setText("${birthMonth - Birthday.currentMonth} Month and ${birthDay - Birthday.currentDay} Days to go")
        } else if (birthMonth > Birthday.currentMonth && birthDay < Birthday.currentDay) {
            val lastMonthDays = Birthday.daysInMonth - Birthday.currentDay
            val finalMonths = birthMonth - (Birthday.currentMonth + 1)
            Log.e("final", "FInal months=" + finalMonths)
            if (finalMonths == 0) {
                holder.tvRTime!!.setText("${lastMonthDays + birthDay} Days  Gone")
                holder.tvRTime!!.setTextColor(Color.parseColor("#FF0000"))
            } else {
                holder.tvRTime!!.setText("${birthMonth - (Birthday.currentMonth + 1)} Month and  ${lastMonthDays + birthDay}  Days to go")
                holder.tvRTime!!.setTextColor(Color.parseColor("#5068C2"))
            }
        } else if (birthMonth < Birthday.currentMonth && birthDay > Birthday.currentDay) {
            val dMonth = 12 - Birthday.currentMonth
            if (dMonth + birthMonth > 0) {
                holder.tvRTime!!.setTextColor(Color.parseColor("#5068C2"))
                holder.tvRTime!!.setText("${dMonth + birthMonth}   Month and ${birthDay - Birthday.currentDay}  Days to go")
            } else {
                holder.tvRTime!!.setText("${birthDay - Birthday.currentDay} Days Gone")
                holder.tvRTime!!.setTextColor(Color.parseColor("#FF0000"))
            }


        } else if (birthMonth < Birthday.currentMonth && birthDay < Birthday.currentDay) {
            val dMonth = 12 - (Birthday.currentMonth + 1)
            val lastMonthDays = Birthday.daysInMonth - Birthday.currentDay

            holder.tvRTime!!.setText("${dMonth + birthMonth} Month and  ${lastMonthDays + birthDay} Days to go")
            holder.tvRTime!!.setTextColor(Color.parseColor("#5068C2"))
        } else if (birthMonth == Birthday.currentMonth && birthDay < Birthday.currentDay) {

            holder.tvRTime!!.setText("${Birthday.currentDay - birthDay} Days gone")
            holder.tvRTime!!.setTextColor(Color.parseColor("#FF0000"))
        } else if (birthMonth == Birthday.currentMonth && birthDay > Birthday.currentDay) {
            holder.tvRTime!!.setText("${birthDay - Birthday.currentDay} Days to go")
            holder.tvRTime!!.setTextColor(Color.parseColor("#5068C2"))
        } else if (birthMonth > Birthday.currentMonth && birthDay == Birthday.currentDay) {
            holder.tvRTime!!.setText("(${birthMonth - Birthday.currentMonth} Month to go")
            holder.tvRTime!!.setTextColor(Color.parseColor("#5068C2"))
        } else if (birthMonth < Birthday.currentMonth && birthDay == Birthday.currentDay) {
            val dMonth = 12 - (Birthday.currentMonth + 1)
            holder.tvRTime!!.setText("${dMonth + birthMonth} Month to go")
            holder.tvRTime!!.setTextColor(Color.parseColor("#5068C2"))
        }


        //Log.e("Data"," ->"+studentList.get(position).notification);
        if (birthdayList[position].name.toString().length === 0 || birthdayList[position] == null) {
            holder.tvTitle!!.text = "No Name"
        } else {
            holder.tvTitle!!.setText(birthdayList[position].name.toString())
        }
        holder.tvDate!!.setText(month + "-" + splitArray[1].toString())
    }

    override fun getItemCount(): Int {
        return birthdayList.size
    }

     class ViewHolder(itemView: View, birthdayList: ArrayList<Birthday>) : RecyclerView.ViewHolder(itemView) {

        internal var tvTitle: TextView? = null
        internal var tvDate: TextView? = null
        internal var tvRTime: TextView? = null

         init {
            tvTitle = itemView.findViewById<TextView>(R.id.tvName)
            tvDate = itemView.findViewById<TextView>(R.id.tvBirthDate)
            tvRTime = itemView.findViewById<TextView>(R.id.tvRemainingTime)
            // tvDesc = (TextView) item.findViewById(R.id.tvDesc);
            // ivIcon = (ImageView) item.findViewById(R.id.ivIcon);
            itemView.setOnClickListener {
                val birthday_id = birthdayList[position].birthday_ID
                val iDetails = Intent(itemView.context, BirthdayDetails::class.java)
                iDetails.putExtra(Birthday.KEY_ID, birthday_id)
                itemView.context.startActivity(iDetails)
            }
        }


    }
}