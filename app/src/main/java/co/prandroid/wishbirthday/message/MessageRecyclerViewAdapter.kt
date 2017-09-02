package co.prandroid.wishbirthday.message

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import co.prandroid.wishbirthday.R
import co.prandroid.wishbirthday.birthday.BirthdayDetails

/**
 * Created by dharmakshetri on 9/1/17.
 */
internal class MessageRecyclerViewAdapter( val c: Context, val data: ArrayList<Message>) : RecyclerView.Adapter<MessageRecyclerViewAdapter.ViewHolder>() {

    internal var messagesList = ArrayList<Message>()

    init {
        messagesList = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.messagerow, parent, false), messagesList,c )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val message = messagesList[position].messageName
        if (message.length <= 100) {
            holder.tvMessageTitle!!.text = message
        } else {

            val subMessage = message.substring(0, 100)
            holder.tvMessageTitle!!.text = subMessage + "..."
        }

    }

    override fun getItemCount()= messagesList.size


    class ViewHolder(itemView: View, messageList: ArrayList<Message>, c: Context) : RecyclerView.ViewHolder(itemView) {

        internal var tvMessageTitle: TextView? = null

        init {
            tvMessageTitle = itemView.findViewById<TextView>(R.id.tvMessageName)
            itemView.setOnClickListener {
                val messageId = messageList[position].message_ID
                showMessage(messageId, c )
            }
        }

        private fun  showMessage(messageId: Int, c: Context) {
            val messageRepo = MessageRepo(c)
            var message = Message(c)
            message = messageRepo.getMessage(messageId, c)
            BirthdayDetails.message_recycler_view.visibility = View.GONE
            BirthdayDetails.tvMessage.visibility = View.VISIBLE
            BirthdayDetails.tvMessage.text = message.messageName
            BirthdayDetails.btnEditMessage.visibility = View.VISIBLE
            BirthdayDetails.messageType = "textview"
        }
    }
}