package co.prandroid.wishbirthday

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.TabLayout

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import co.happybirthday.fragments.FriendsFragment
import co.happybirthday.fragments.RelativeFragment
import co.prandroid.wishbirthday.birthday.Birthday


import java.util.ArrayList
import java.util.Arrays
import java.util.Calendar
import java.util.Date


import co.prandroid.wishbirthday.birthday.BirthdayRepo
import co.prandroid.wishbirthday.birthday.BirthdaysAdd
import co.prandroid.wishbirthday.fragments.AllFragment
import co.prandroid.wishbirthday.fragments.FavouriteFragment
import co.prandroid.wishbirthday.fragments.OtherFragment
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd


class MainActivity : AppCompatActivity() {

    private var toolbar: Toolbar? = null

    private var viewPager: ViewPager? = null
    internal var currentTimeMilli: Long = 0
    private var mInterstitialAd: InterstitialAd? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //setNotification(bid);
        toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        RateThisApp.init(RateThisApp.Config(3, 5))

        Birthday.arraysCategory = resources.getStringArray(R.array.Categories)
        Birthday.arrayMonths = resources.getStringArray(R.array.months)
        Birthday.arrayDays = resources.getStringArray(R.array.days)
        val arrayNotificationCheckList = resources.getStringArray(R.array.NotificationCheckList)
        Birthday.arrayNotificationCheckList = Arrays.asList(*arrayNotificationCheckList)


        viewPager = findViewById<ViewPager>(R.id.viewpager)
        setupViewPager(viewPager!!)

        var tabLayout = findViewById<TabLayout>(R.id.tabs)
        tabLayout.setupWithViewPager(viewPager)


        mInterstitialAd = InterstitialAd(this)
        mInterstitialAd!!.setAdUnitId(getString(R.string.interstitial_ad_unit_id))

        // Set an AdListener.
        mInterstitialAd!!.setAdListener(object : AdListener() {
            //     @Override
            //            public void onAdLoaded() {
            //                Toast.makeText(MainActivity.this,
            //                        "The interstitial is loaded", Toast.LENGTH_SHORT).show();
            //            }

            override fun onAdClosed() {
                // Proceed to the next level.
                requestNewInterstitial()
                goToNextLevel()
            }
        })

        requestNewInterstitial()
        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener(View.OnClickListener {
            // without ever showing it.
            if (mInterstitialAd!!.isLoaded()) {
                mInterstitialAd!!.show()
            } else {
                // Proceed to the next level.
                goToNextLevel()
            }
            //goToNextLevel();
        })


    }

    private fun requestNewInterstitial() {
        val adRequest = AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build()

        mInterstitialAd!!.loadAd(adRequest)
    }

    fun goToNextLevel() {
        val iAddBirthDay = Intent(this@MainActivity, BirthdaysAdd::class.java)
        startActivity(iAddBirthDay)
    }


    override fun onStart() {
        super.onStart()// ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        //client!!.connect()
        // Monitor launch times and interval from installation
        RateThisApp.onStart(this)
        // Show a dialog if criteria is satisfied
        RateThisApp.showRateDialogIfNeeded(this)
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
       // AppIndex.AppIndexApi.start(client, indexApiAction)
    }

    fun setNotification() {
        val birthdayRepo = BirthdayRepo(this)
        var studentsList = ArrayList<Birthday>()

        studentsList = birthdayRepo.allBirthday

        val rightNow = Calendar.getInstance()

        // offset to add since we're not UTC
        val offset = (rightNow.get(Calendar.ZONE_OFFSET) + rightNow.get(Calendar.DST_OFFSET)).toLong()
        val sinceMidnight = (rightNow.timeInMillis + offset) % (24 * 60 * 60 * 1000)

        println(sinceMidnight.toString() + " milliseconds since midnight")

        val date = Date()
        /*getTime():It returns the number of milliseconds since
       * January 1, 1970, 00:00:00 GMT
       * represented by this date.
       */
        currentTimeMilli = date.time
        println("Time in milliseconds using Date class: " + currentTimeMilli)


        val c = Calendar.getInstance()
        val now = c.timeInMillis
        c.set(Calendar.HOUR_OF_DAY, 0)
        c.set(Calendar.MINUTE, 0)
        c.set(Calendar.SECOND, 0)
        c.set(Calendar.MILLISECOND, 0)
        val midNight = c.timeInMillis
        val passed = now - c.timeInMillis
        val secondsPassed = passed / 1000
        val minPassed = secondsPassed / 60
        val hourpassed = minPassed / 60

        for (i in studentsList.indices) {
            val strDate = studentsList[i].date.toString()
            val splitArray = strDate.split("-".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
            val month = Birthday.getMonth(splitArray[0].toString())

            val birthMonth = Integer.parseInt(splitArray[0].toString())
            val birthDay = Integer.parseInt(splitArray[1].toString())

            if (Birthday.currentMonth === birthMonth && Birthday.currentDay - 1 === birthDay) {
                Log.e("One Day", "One before +set notificaiton==" + (currentTimeMilli - Common.dayMiliseconds))

            }
        }

    }

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFrag(AllFragment(), "All")
        adapter.addFrag(FriendsFragment(), "Friends")
        adapter.addFrag(FamilyFragment(), "Family")
        adapter.addFrag(RelativeFragment(), "Relatives")
        adapter.addFrag(ColleagueFragment(), "Colleagues")
        adapter.addFrag(OtherFragment(), "Others")
        adapter.addFrag(FavouriteFragment(), "Favourite")
        viewPager.adapter = adapter
    }


    public override fun onStop() {
        super.onStop()

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
       // AppIndex.AppIndexApi.end(client, indexApiAction)
       // client!!.disconnect()
    }

    internal inner class ViewPagerAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager) {
        private val mFragmentList = ArrayList<Fragment>()
        private val mFragmentTitleList = ArrayList<String>()

        override fun getItem(position: Int): Fragment {
            return mFragmentList[position]
        }

        override fun getCount(): Int {
            return mFragmentList.size
        }

        fun addFrag(fragment: Fragment, title: String) {
            mFragmentList.add(fragment)
            mFragmentTitleList.add(title)
        }

        override fun getPageTitle(position: Int): CharSequence {
            return mFragmentTitleList[position]
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId


        if (id == R.id.action_settings) {
            return true
        }
        if (id == R.id.action_rate_app) {
            // Toast.makeText(getApplicationContext(),"Rate App",Toast.LENGTH_SHORT).show();
            RateThisApp.showRateDialog(this@MainActivity)
            return true
        }
        if (id == R.id.action_favourite) {
            // displayFavourite();
            //  Toast.makeText(getApplicationContext(),"Favourite Display",Toast.LENGTH_SHORT).show();
            viewPager!!.setCurrentItem(6, false)
            return true
        }

        return super.onOptionsItemSelected(item)
    }
}
