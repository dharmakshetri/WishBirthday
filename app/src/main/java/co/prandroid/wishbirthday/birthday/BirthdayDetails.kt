package co.prandroid.wishbirthday.birthday

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.view.View

import android.os.Bundle
import android.telephony.SmsManager
import android.util.Log
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import co.prandroid.wishbirthday.message.Message
import co.prandroid.wishbirthday.message.MessageBaseAdapter
import co.prandroid.wishbirthday.message.MessageRepo
import co.prandroid.wishbirthday.R


/**
 * Created by dharmakshetri on 8/28/17.
 */
class BirthdayDetails : AppCompatActivity(), View.OnClickListener, OnItemClickListener {
    internal lateinit var tvName: TextView
    internal lateinit var tvBirthDay: TextView
    internal lateinit var tvRemainingTime: TextView
    internal lateinit var btnEmail: Button
    internal lateinit var btnMessage: Button
    internal lateinit var btnShare: Button
    internal lateinit var btnEditNames: Button
    internal var birthDayId: Int = 0
    internal lateinit var birthday: Birthday
    internal lateinit var listViewMesssage: ListView
    internal var arrayListMessae = ArrayList<Message>()
    internal lateinit var messageAdapter: MessageBaseAdapter
    internal lateinit var btnEditMessage: Button
    internal lateinit var tvMessage: TextView
    internal lateinit var editMessage: EditText
    internal var messageType = ""
    internal var textSubject = "Happy Birthday to You!!!"
    internal var textMessage = ""
    internal lateinit var imgFav: ImageView
    internal var flagFav = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.birthdaydetails)

        val intent = intent
        val extras = intent.extras
        if (extras != null) {
            if (extras.containsKey(Birthday.KEY_ID)) {
                // extract the extra-data in the Notification
                birthDayId = extras.getInt(Birthday.KEY_ID)
            }
        }

        val birthdayRepo = BirthdayRepo(this)
        birthday = birthdayRepo.getStudentById(birthDayId)
        supportActionBar!!.title = birthday.name!! + " Details"
        setUpViews()
        setUpValues()
    }

    fun setUpViews() {
        tvName = findViewById<View>(R.id.txtName) as TextView
        tvBirthDay = findViewById<View>(R.id.txtBirthDate) as TextView
        tvRemainingTime = findViewById<View>(R.id.txtRTime) as TextView

        btnEmail = findViewById<View>(R.id.btnEmail) as Button
        btnMessage = findViewById<View>(R.id.btnMessage) as Button
        btnShare = findViewById<View>(R.id.btnShare) as Button
        btnEditNames = findViewById<View>(R.id.btnEdit) as Button


        btnEditNames.setOnClickListener(this)
        btnEmail.setOnClickListener(this)
        btnMessage.setOnClickListener(this)
        btnShare.setOnClickListener(this)

        listViewMesssage = findViewById<View>(R.id.listmessage) as ListView
        listViewMesssage.visibility = View.VISIBLE
        tvMessage = findViewById<View>(R.id.tvMessages) as TextView
        editMessage = findViewById<View>(R.id.editMessage) as EditText
        btnEditMessage = findViewById<View>(R.id.btnEditMessage) as Button
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
        Log.e("LIXE SIXE", "   1     arrayListMessae=" + arrayListMessae.size)
        arrayListMessae.clear()
        Log.e("LIXE SIXE", "   2     arrayListMessae=" + arrayListMessae.size)
        arrayListMessae = messageRepo.getAllMessages(applicationContext)

        Log.e("LIXE SIXE", "   3     arrayListMessae=" + arrayListMessae.size)
        messageAdapter = MessageBaseAdapter(applicationContext, arrayListMessae)
        listViewMesssage.adapter = messageAdapter
        listViewMesssage.onItemClickListener = this
        flagFav = birthday.favourite
        if (flagFav == 0) {
            imgFav.setBackgroundResource(R.drawable.nofav_32)

        } else {
            imgFav.setBackgroundResource(R.drawable.fav_32)
        }

    }

    override fun onItemClick(arg0: AdapterView<*>, view: View, arg2: Int, arg3: Long) {
        // TODO Auto-generated method stub
        val messageId = arrayListMessae[arg2].message_ID
        showMessage(messageId)

    }

    protected fun showMessage(messageId: Int) {
        // Toast.makeText(getApplicationContext(), ""+messageId, Toast.LENGTH_SHORT).show();
        // TODO Auto-generated method stub
        val messageRepo = MessageRepo(applicationContext)
        var message = Message(applicationContext)
        message = messageRepo.getMessage(messageId, applicationContext)
        listViewMesssage.visibility = View.GONE
        tvMessage.visibility = View.VISIBLE
        tvMessage.text = message.messageName
        btnEditMessage.visibility = View.VISIBLE
        messageType = "textview"
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
                listViewMesssage.visibility = View.GONE
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

    protected fun sendSMSMessage(phoneNo: String, message: String) {
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