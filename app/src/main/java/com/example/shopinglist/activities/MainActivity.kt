package com.example.shopinglist.activities

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.preference.PreferenceManager
import com.example.shopinglist.R
import com.example.shopinglist.billing.BillingManager
import com.example.shopinglist.databinding.ActivityMainBinding
import com.example.shopinglist.dialogs.NewListDialog
import com.example.shopinglist.fragments.FragmentManager
import com.example.shopinglist.fragments.NoteFragment
import com.example.shopinglist.fragments.ShopListNamesFragment
import com.example.shopinglist.settings.SettingsActivity
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

class MainActivity : AppCompatActivity(),NewListDialog.Listener {
    lateinit var binding: ActivityMainBinding
    private var currentMenuId = R.id.shop_list
    private lateinit var defPref: SharedPreferences
    private var currentTheme = ""
    private var iAd: InterstitialAd? = null
    private var adShowCounter = 0
    private var adShowCounterMax = 4
    private lateinit var pref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        defPref = PreferenceManager.getDefaultSharedPreferences(this)
        setTheme(getSelectedTheme())
        super.onCreate(savedInstanceState)
        pref = getSharedPreferences(BillingManager.MAIN_PREF, MODE_PRIVATE)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        currentTheme = defPref.getString("theme_key", "blue").toString()
        FragmentManager.setFragment(ShopListNamesFragment.newInstance(), this)
        setButtomNavListiner()
        if(!pref.getBoolean(BillingManager.REMOVE_ADS_KEY, false))loadInterAd()
    }

    private fun loadInterAd(){
        val request = com.google.android.gms.ads.AdRequest.Builder().build()
        InterstitialAd.load(this, getString(R.string.inter_ad_id), request,
            object: InterstitialAdLoadCallback(){
                override fun onAdLoaded(p0: InterstitialAd) {
                    iAd = p0
                }
                override fun onAdFailedToLoad(p0: LoadAdError) {
                    iAd = null
                }
        })
    }

    private fun showInterAd(adListener: AdListener){
        if(iAd !== null && adShowCounter > adShowCounterMax
            && !pref.getBoolean(BillingManager.REMOVE_ADS_KEY, false)){
            iAd?.fullScreenContentCallback = object : FullScreenContentCallback(){
                override fun onAdDismissedFullScreenContent() {
                    iAd = null
                    loadInterAd()
                    adListener.onFinish()
                }

                override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                    iAd = null
                    loadInterAd()
                }

                override fun onAdShowedFullScreenContent() {
                    iAd = null
                    loadInterAd()
                }
            }
            adShowCounter = 0
            iAd?.show(this)
        }else{
            adShowCounter++
            adListener.onFinish()
        }
    }

    private fun setButtomNavListiner(){
        binding.bNav.setOnItemSelectedListener {
            when(it.itemId){
                R.id.settings -> {
                    showInterAd(object : AdListener{
                        override fun onFinish() {
                            startActivity(Intent(this@MainActivity, SettingsActivity::class.java))
                        }

                    })

                }
                R.id.notes -> {
                    showInterAd(object : AdListener{
                        override fun onFinish() {
                            currentMenuId = R.id.notes
                            FragmentManager.setFragment(NoteFragment.newInstance(), this@MainActivity)
                        }

                    })
                }
                R.id.shop_list -> {
                    showInterAd(object : AdListener{
                        override fun onFinish() {
                            currentMenuId = R.id.shop_list
                            FragmentManager.setFragment(ShopListNamesFragment.newInstance(),this@MainActivity)
                        }

                    })
                }
                R.id.new_item -> {
                    FragmentManager.currentFrag?.onClickNew()
                }
            }
            true
        }
    }

    override fun onResume() {
        super.onResume()
        binding.bNav.selectedItemId = currentMenuId
        if(defPref.getString("theme_key","blue") !== currentTheme) recreate()

    }

    private fun getSelectedTheme(): Int{
        return if(defPref.getString("theme_key", "blue") == "blue"){
            R.style.Theme_ShoppingListBlue
        }else{
            R.style.Theme_ShoppingListRed
        }
    }

    override fun onClick(name: String) {
        Log.d("MyLog","Name: $name")
    }
    interface AdListener{
        fun onFinish()
    }
}