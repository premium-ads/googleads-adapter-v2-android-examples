package net.premiumads.example.admob

import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback
import android.widget.ImageView
import net.premiumads.sdk.adapter.PremiumAdsAdapter
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var tvLog: TextView
    private lateinit var bannerContainer: FrameLayout
    private lateinit var nativeAdContainer: FrameLayout
    private var currentNativeAd: NativeAd? = null
    private var nativeAdLoader: AdLoader? = null

    private val bannerAdUnitId = "ca-app-pub-2142338037257831/5013815038"
    private val nativeAdUnitId = "ca-app-pub-2142338037257831/3433902069"
    private val interstitialAdUnitId = "ca-app-pub-2142338037257831/1616542060"
    private val rewardedAdUnitId = "ca-app-pub-2142338037257831/6768646189"
    private val rewardedInterstitialAdUnitId = "ca-app-pub-2142338037257831/9846792399"
    private val appOpenAdUnitId = "ca-app-pub-2142338037257831/3283026116"

    private var interstitialAd: InterstitialAd? = null
    private var rewardedAd: RewardedAd? = null
    private var rewardedInterstitialAd: RewardedInterstitialAd? = null
    private var appOpenAd: AppOpenAd? = null

    private var isSdkInitialized = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        PremiumAdsAdapter.setDebug(true)
        tvLog = findViewById(R.id.tvLog)
        bannerContainer = findViewById(R.id.bannerContainer)
        nativeAdContainer = findViewById(R.id.nativeAdContainer)

        val btnBanner = findViewById<Button>(R.id.btnLoadBanner)
        val btnInterstitial = findViewById<Button>(R.id.btnLoadInterstitial)
        val btnRewarded = findViewById<Button>(R.id.btnLoadRewarded)
        val btnNative = findViewById<Button>(R.id.btnLoadNative)
        val btnRewardedInterstitial = findViewById<Button>(R.id.btnLoadRewardedInterstitial)
        val btnAppOpen = findViewById<Button>(R.id.btnLoadAppOpen)

        val allButtons = listOf(btnBanner, btnInterstitial, btnRewarded, btnRewardedInterstitial, btnNative, btnAppOpen)
        allButtons.forEach { it.isEnabled = false }

        log("Initializing MobileAds SDK...")
        // Optional: add your test device ID
        // val requestConfig = com.google.android.gms.ads.RequestConfiguration.Builder()
        //     .setTestDeviceIds(listOf("YOUR_TEST_DEVICE_ID"))
        //     .build()
        // MobileAds.setRequestConfiguration(requestConfig)
        MobileAds.initialize(this) { status ->
            log("MobileAds initialized.")
            val map = status.adapterStatusMap
            for ((adapterClass, adapterStatus) in map) {
                log("Adapter: $adapterClass | State: ${adapterStatus.initializationState} | Desc: ${adapterStatus.description}")
            }
            runOnUiThread {
                isSdkInitialized = true
                allButtons.forEach { it.isEnabled = true }
                log("Ready! Tap a button to load ads.")
            }
        }

        btnBanner.setOnClickListener { loadBanner() }
        btnInterstitial.setOnClickListener { loadInterstitial() }
        btnRewarded.setOnClickListener { loadRewarded() }
        btnNative.setOnClickListener { loadNative() }
        btnRewardedInterstitial.setOnClickListener { loadRewardedInterstitial() }
        btnAppOpen.setOnClickListener { loadAppOpen() }
    }

    private fun loadBanner() {
        log("Loading banner...")
        val adView = AdView(this)
        adView.adUnitId = bannerAdUnitId
        adView.setAdSize(AdSize.BANNER)
        adView.adListener = object : AdListener() {
            override fun onAdLoaded() { log("Banner loaded") }
            override fun onAdFailedToLoad(error: LoadAdError) { log("Banner failed: ${error.message}") }
            override fun onAdImpression() { log("Banner impression") }
            override fun onAdClicked() { log("Banner clicked") }
        }
        bannerContainer.removeAllViews()
        bannerContainer.addView(adView)
        adView.loadAd(AdRequest.Builder().build())
    }

    private fun loadInterstitial() {
        log("Loading interstitial...")
        InterstitialAd.load(this, interstitialAdUnitId, AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    log("Interstitial loaded")
                    interstitialAd = ad
                    ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                        override fun onAdShowedFullScreenContent() { log("Interstitial shown") }
                        override fun onAdDismissedFullScreenContent() { log("Interstitial dismissed") }
                    }
                    ad.show(this@MainActivity)
                }
                override fun onAdFailedToLoad(error: LoadAdError) { log("Interstitial failed: ${error.message}") }
            })
    }

    private fun loadRewarded() {
        log("Loading rewarded...")
        RewardedAd.load(this, rewardedAdUnitId, AdRequest.Builder().build(),
            object : RewardedAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedAd) {
                    log("Rewarded loaded")
                    rewardedAd = ad
                    ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                        override fun onAdShowedFullScreenContent() { log("Rewarded shown") }
                        override fun onAdDismissedFullScreenContent() { log("Rewarded dismissed") }
                    }
                    ad.show(this@MainActivity) { reward ->
                        log("User earned reward: ${reward.amount} ${reward.type}")
                    }
                }
                override fun onAdFailedToLoad(error: LoadAdError) { log("Rewarded failed: ${error.message}") }
            })
    }

    private fun loadRewardedInterstitial() {
        log("Loading rewarded interstitial...")
        RewardedInterstitialAd.load(this, rewardedInterstitialAdUnitId, AdRequest.Builder().build(),
            object : RewardedInterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedInterstitialAd) {
                    log("Rewarded interstitial loaded")
                    rewardedInterstitialAd = ad
                    ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                        override fun onAdShowedFullScreenContent() { log("Rewarded interstitial shown") }
                        override fun onAdDismissedFullScreenContent() { log("Rewarded interstitial dismissed") }
                    }
                    ad.show(this@MainActivity) { reward ->
                        log("Rewarded interstitial reward: ${reward.amount} ${reward.type}")
                    }
                }
                override fun onAdFailedToLoad(error: LoadAdError) { log("Rewarded interstitial failed: ${error.message}") }
            })
    }

    private fun loadNative() {
        log("Loading native...")
        nativeAdLoader = AdLoader.Builder(this, nativeAdUnitId)
            .forNativeAd { ad: NativeAd ->
                log("Native loaded: headline='${ad.headline}'")
                currentNativeAd?.destroy()
                currentNativeAd = ad
                displayNativeAd(ad)
            }
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(error: LoadAdError) { log("Native failed: ${error.message}") }
                override fun onAdImpression() { log("Native impression") }
                override fun onAdClicked() { log("Native clicked") }
            })
            .build()
        nativeAdLoader?.loadAd(AdRequest.Builder().build())
    }

    private fun displayNativeAd(ad: NativeAd) {
        val adView = layoutInflater.inflate(R.layout.native_ad_layout, null) as NativeAdView

        adView.headlineView = adView.findViewById<TextView>(R.id.ad_headline).apply {
            text = ad.headline
        }
        adView.bodyView = adView.findViewById<TextView>(R.id.ad_body).apply {
            text = ad.body
            visibility = if (ad.body != null) android.view.View.VISIBLE else android.view.View.GONE
        }
        adView.callToActionView = adView.findViewById<Button>(R.id.ad_call_to_action).apply {
            text = ad.callToAction
            visibility = if (ad.callToAction != null) android.view.View.VISIBLE else android.view.View.GONE
        }
        adView.iconView = adView.findViewById<ImageView>(R.id.ad_app_icon).apply {
            ad.icon?.let { setImageDrawable(it.drawable) }
            visibility = if (ad.icon != null) android.view.View.VISIBLE else android.view.View.GONE
        }
        adView.mediaView = adView.findViewById<MediaView>(R.id.ad_media).apply {
            ad.mediaContent?.let { mediaContent = it }
        }
        adView.advertiserView = adView.findViewById<TextView>(R.id.ad_advertiser).apply {
            text = ad.advertiser
            visibility = if (ad.advertiser != null) android.view.View.VISIBLE else android.view.View.GONE
        }

        adView.setNativeAd(ad)

        nativeAdContainer.removeAllViews()
        nativeAdContainer.addView(adView)
        log("Native ad displayed")
    }

    private fun loadAppOpen() {
        log("Loading app open...")
        AppOpenAd.load(this, appOpenAdUnitId, AdRequest.Builder().build(),
            object : AppOpenAd.AppOpenAdLoadCallback() {
                override fun onAdLoaded(ad: AppOpenAd) {
                    log("App open loaded")
                    appOpenAd = ad
                    ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                        override fun onAdShowedFullScreenContent() { log("App open shown") }
                        override fun onAdDismissedFullScreenContent() { log("App open dismissed") }
                    }
                    ad.show(this@MainActivity)
                }
                override fun onAdFailedToLoad(error: LoadAdError) { log("App open failed: ${error.message}") }
            })
    }

    override fun onDestroy() {
        currentNativeAd?.destroy()
        super.onDestroy()
    }

    private fun log(message: String) {
        val timestamp = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
        runOnUiThread {
            tvLog.append("\n[$timestamp] $message")
        }
    }
}
