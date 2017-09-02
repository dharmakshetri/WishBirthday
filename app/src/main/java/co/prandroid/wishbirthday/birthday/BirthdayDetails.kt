package co.prandroid.wishbirthday.birthday

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.view.View

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.telephony.SmsManager
import android.util.Log
import android.widget.*
import co.prandroid.wishbirthday.message.Message
import co.prandroid.wishbirthday.message.MessageRepo
import co.prandroid.wishbirthday.R
import co.prandroid.wishbirthday.message.MessageRecyclerViewAdapter


@SuppressLint("StaticFieldLeak")
/**
 * Created by dharmakshetri on 8/28/17.
 */
class BirthdayDetails : AppCompatActivity(), View.OnClickListener {
    internal lateinit var tvName: TextView
    internal lateinit var tvBirthDay: TextView
    internal lateinit var tvRemainingTime: TextView
    internal lateinit var btnEmail: Button
    internal lateinit var btnMessage: Button
    internal lateinit var btnShare: Button

    internal var birthDayId: Int = 0
    internal lateinit var birthday: Birthday
    // internal lateinit var listViewMesssage: ListView
    internal var arrayListMessae = ArrayList<Message>()
    // internal lateinit var messageAdapter: MessageBaseAdapter

    internal lateinit var btnEditNames: Button
    internal lateinit var editMessage: EditText
    internal var textMessage = ""
    internal var textSubject = "Happy Birthday to You!!!"

    internal lateinit var imgFav: ImageView
    internal var flagFav = 0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.birthdaydetails)

        val intent = intent
        val extras = intent.extras
        if (extras != null) {
            if (extras.containsKey(Birthday.KEY_ID)) {
                birthDayId = extras.getInt(Birthday.KEY_ID)
            }
        }

        val birthdayRepo = BirthdayRepo(this)
        birthday = birthdayRepo.getStudentById(birthDayId)
        supportActionBar!!.title = birthday.name!! + " Details"
        setUpViews()
        setUpValues()
    }

    companion object {
        lateinit var message_recycler_view: RecyclerView
        internal lateinit var tvMessage: TextView
        internal lateinit var btnEditMessage: Button
        var messageType: String = ""
    }
    fun setUpViews() {
        tvName = findViewById<TextView>(R.id.txtName)

        tvBirthDay = findViewById<TextView>(R.id.txtBirthDate)
        tvRemainingTime = findViewById<TextView>(R.id.txtRTime)

        btnEmail = findViewById<Button>(R.id.btnEmail)
        btnMessage = findViewById<Button>(R.id.btnMessage)
        btnShare = findViewById<Button>(R.id.btnShare)
        btnEditNames = findViewById<Button>(R.id.btnEdit)


        btnEditNames.setOnClickListener(this)
        btnEmail.setOnClickListener(this)
        btnMessage.setOnClickListener(this)
        btnShare.setOnClickListener(this)


        message_recycler_view = findViewById<RecyclerView>(R.id.message_recycler_view)
        message_recycler_view.hasFixedSize()
        message_recycler_view.visibility = View.VISIBLE


        tvMessage = findViewById<TextView>(R.id.tvMessages)
        editMessage = findViewById<EditText>(R.id.editMessage)
        btnEditMessage = findViewById<Button>(R.id.btnEditMessage)
        btnEditMessage.setOnClickListener(this)

        imgFav = findViewById<View>(R.id.imgFav) as ImageView
        imgFav.setOnClickListener(this)

    }

    fun setUpValues() {
        tvName.text = birthday.name
        val splitArray = birthday.date!!.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val month = Birthday.getMonth(splitArray[0].toString())
        tvBirthDay.text = month + " - " + splitArray[1].toString()
        tvRemainingTime.text = birthday.notification
        val messageRepo = MessageRepo(applicationContext)
        val message = Message(applicationContext)

        arrayListMessae.clear()
        arrayListMessae = messageRepo.getAllMessages(applicationContext)

        message_recycler_view.layoutManager = LinearLayoutManager(applicationContext);
        message_recycler_view.adapter = MessageRecyclerViewAdapter(applicationContext,arrayListMessae)

        ///listViewMesssage.onItemClickListener = this
        flagFav = birthday.favourite
        if (flagFav == 0) {
            imgFav.setBackgroundResource(R.drawable.nofav_32)

        } else {
            imgFav.setBackgroundResource(R.drawable.fav_32)
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnEdit -> {
                //Toast.makeText(getApplicationContext(), "id="+student.birthday_ID,Toast.LENGTH_SHORT).show();
                val ibackToEdit = Intent(this@BirthdayDetails, BirthdaysAdd::class.java)
                ibackToEdit.putExtra(Birthday.KEY_ID, birthDayId)
                ibackToEdit.putExtra(Birthday.Status, 1)
                startActivity(ibackToEdit)
            }
            R.id.btnEmail -> {

                if (messageType.equals("textview", ignoreCase = true)) {
                    textMessage = tvMessage.text.toString()
                }
                if (messageType.equals("edittext", ignoreCase = true)) {
                    textMessage = editMessage.text.toString()
                }
                if (messageType.length > 0) {
                    val birthdayRepo = BirthdayRepo(applicationContext)
                    var student = Birthday()
                    student = birthdayRepo.getStudentById(birthDayId)
                    if (student.email!!.length == 0) {
                        Toast.makeText(this@BirthdayDetails, "Email Address is not provided", Toast.LENGTH_SHORT).show()
                    } else {
                        sendEmail(student.email!!, textMessage)
                    }
                } else {
                    Toast.makeText(this@BirthdayDetails, "Please choose birthday message", Toast.LENGTH_SHORT).show()
                }
            }
            R.id.btnMessage -> {

                if (messageType.equals("textview", ignoreCase = true)) {
                    textMessage = tvMessage.text.toString()
                }
                if (messageType.equals("edittext", ignoreCase = true)) {
                    textMessage = editMessage.text.toString()
                }
                if (messageType.length > 0) {
                    val birthdayRepo = BirthdayRepo(applicationContext)
                    var birthdayBoy = Birthday()
                    birthdayBoy = birthdayRepo.getStudentById(birthDayId)
                    if (birthdayBoy.mobile!!.length == 0) {
                        Toast.makeText(this@BirthdayDetails, "Mobile Number is not provided", Toast.LENGTH_SHORT).show()
                    } else {
                        sendSmsBySIntent(birthdayBoy.mobile!!, textMessage + " " + tvName.text.toString() + " !!!")
                    }
                } else {
                    Toast.makeText(this@BirthdayDetails, "Please choose birthday message", Toast.LENGTH_SHORT).show()
                }
            }
            R.id.btnShare -> {

                if (messageType.equals("textview", ignoreCase = true)) {
                    textMessage = tvMessage.text.toString()
                }
                if (messageType.equals("edittext", ignoreCase = true)) {
                    textMessage = editMessage.text.toString()
                }
                if (messageType.length > 0) {
                    shareMessage(textMessage)
                } else {
                    Toast.makeText(this@BirthdayDetails, "Please choose birthday message", Toast.LENGTH_SHORT).show()
                }
            }
            R.id.btnEditMessage -> {
                editMessage.visibility = View.VISIBLE
                tvMessage.visibility = View.GONE
                message_recycler_view.visibility = View.GONE
                btnEditMessage.visibility = View.GONE
                editMessage.setText(tvMessage.text.toString())
                messageType = "edittext"
            }
            R.id.imgFav -> {

                val birthdayRepo = BirthdayRepo(this)

                if (flagFav == 0) {
                    //Toast.makeText(getApplicationContext()," Faveourite "+student.birthday_ID,Toast.LENGTH_SHORT).show();
                    imgFav.setBackgroundResource(R.drawable.fav_32)
                    flagFav = 1

                } else {
                    //Toast.makeText(getApplicationContext(),"No Faveourite= "+student.birthday_ID,Toast.LENGTH_SHORT).show();
                    imgFav.setBackgroundResource(R.drawable.nofav_32)
                    flagFav = 0
                }
                birthdayRepo.updateFavourite(flagFav, birthday.birthday_ID)
            }
        }
    }

    //share content in social media
    fun shareMessage(messsage: String) {

        val share = Intent(android.content.Intent.ACTION_SEND)
        share.type = "text/plain"
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET)

        // Add data to the intent, the receiving app will decide
        // what to do with it.
        share.putExtra(Intent.EXTRA_SUBJECT, textSubject + birthday.name!!)
        share.putExtra(Intent.EXTRA_TEXT, messsage)

        startActivity(Intent.createChooser(share, "Share Messages!"))
    }

    // send message

    fun sendSmsBySIntent(phoneNo: String, message: String) {
        // add the phone number in the data
        val uri = Uri.parse("smsto:" + phoneNo)

        val smsSIntent = Intent(Intent.ACTION_SENDTO, uri)
        // add the message at the sms_body extra field
        smsSIntent.putExtra("sms_body", message)
        try {
            startActivity(smsSIntent)
        } catch (ex: Exception) {
            Toast.makeText(this@BirthdayDetails, "Your sms has failed...",
                    Toast.LENGTH_LONG).show()
            ex.printStackTrace()
        }

    }

    fun sendSMSMessage(phoneNo: String, message: String) {
        try {
            val smsManager = SmsManager.getDefault()
            smsManager.sendTextMessage(phoneNo, null, message, null, null)
            Toast.makeText(applicationContext, "SMS sent.", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Toast.makeText(applicationContext, "SMS faild, please try again.", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }

    }
    //sendEmail

    fun sendEmail(toEmail: String, message: String) {
        val TO = arrayOf(toEmail)
        // String[] CC = {""};
        val emailIntent = Intent(Intent.ACTION_SEND)

        emailIntent.data = Uri.parse("mailto:")
        emailIntent.type = "text/plain"
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO)
        // emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, textSubject + birthday.name!!)
        emailIntent.putExtra(Intent.EXTRA_TEXT, message)

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."))
            finish()
        } catch (ex: android.content.ActivityNotFoundException) {
            Toast.makeText(this@BirthdayDetails, "There is no email client installed.", Toast.LENGTH_SHORT).show()
        }

    }
}
