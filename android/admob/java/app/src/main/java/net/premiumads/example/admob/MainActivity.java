package net.premiumads.example.admob;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback;

import net.premiumads.sdk.adapter.PremiumAdsAdapter;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView tvLog;
    private FrameLayout bannerContainer;
    private FrameLayout nativeAdContainer;
    private NativeAd currentNativeAd;
    private AdLoader nativeAdLoader;

    private static final String BANNER_AD_UNIT_ID = "ca-app-pub-2142338037257831/5013815038";
    private static final String NATIVE_AD_UNIT_ID = "ca-app-pub-2142338037257831/3433902069";
    private static final String INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-2142338037257831/1616542060";
    private static final String REWARDED_AD_UNIT_ID = "ca-app-pub-2142338037257831/6768646189";
    private static final String REWARDED_INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-2142338037257831/9846792399";
    private static final String APP_OPEN_AD_UNIT_ID = "ca-app-pub-2142338037257831/3283026116";

    private InterstitialAd interstitialAd;
    private RewardedAd rewardedAd;
    private RewardedInterstitialAd rewardedInterstitialAd;
    private AppOpenAd appOpenAd;

    private boolean isSdkInitialized = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PremiumAdsAdapter.setDebug(true);

        tvLog = findViewById(R.id.tvLog);
        bannerContainer = findViewById(R.id.bannerContainer);
        nativeAdContainer = findViewById(R.id.nativeAdContainer);

        final Button btnBanner = findViewById(R.id.btnLoadBanner);
        final Button btnInterstitial = findViewById(R.id.btnLoadInterstitial);
        final Button btnRewarded = findViewById(R.id.btnLoadRewarded);
        final Button btnNative = findViewById(R.id.btnLoadNative);
        final Button btnRewardedInterstitial = findViewById(R.id.btnLoadRewardedInterstitial);
        final Button btnAppOpen = findViewById(R.id.btnLoadAppOpen);

        final List<Button> allButtons = Arrays.asList(
                btnBanner, btnInterstitial, btnRewarded,
                btnRewardedInterstitial, btnNative, btnAppOpen
        );

        for (Button btn : allButtons) {
            btn.setEnabled(false);
        }

        log("Initializing MobileAds SDK...");
        // Optional: add your test device ID
        // RequestConfiguration requestConfig = new RequestConfiguration.Builder()
        //     .setTestDeviceIds(Collections.singletonList("YOUR_TEST_DEVICE_ID"))
        //     .build();
        // MobileAds.setRequestConfiguration(requestConfig);
        MobileAds.initialize(this, initializationStatus -> {
            log("MobileAds initialized.");
            for (java.util.Map.Entry<String, com.google.android.gms.ads.AdapterStatus> entry
                    : initializationStatus.getAdapterStatusMap().entrySet()) {
                log("Adapter: " + entry.getKey()
                        + " | State: " + entry.getValue().getInitializationState()
                        + " | Desc: " + entry.getValue().getDescription());
            }
            runOnUiThread(() -> {
                isSdkInitialized = true;
                for (Button btn : allButtons) {
                    btn.setEnabled(true);
                }
                log("Ready! Tap a button to load ads.");
            });
        });

        btnBanner.setOnClickListener(v -> loadBanner());
        btnInterstitial.setOnClickListener(v -> loadInterstitial());
        btnRewarded.setOnClickListener(v -> loadRewarded());
        btnNative.setOnClickListener(v -> loadNative());
        btnRewardedInterstitial.setOnClickListener(v -> loadRewardedInterstitial());
        btnAppOpen.setOnClickListener(v -> loadAppOpen());
    }

    private void loadBanner() {
        log("Loading banner...");
        AdView adView = new AdView(this);
        adView.setAdUnitId(BANNER_AD_UNIT_ID);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                log("Banner loaded");
            }

            @Override
            public void onAdFailedToLoad(LoadAdError error) {
                log("Banner failed: " + error.getMessage());
            }

            @Override
            public void onAdImpression() {
                log("Banner impression");
            }

            @Override
            public void onAdClicked() {
                log("Banner clicked");
            }
        });
        bannerContainer.removeAllViews();
        bannerContainer.addView(adView);
        adView.loadAd(new AdRequest.Builder().build());
    }

    private void loadInterstitial() {
        log("Loading interstitial...");
        InterstitialAd.load(this, INTERSTITIAL_AD_UNIT_ID, new AdRequest.Builder().build(),
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(InterstitialAd ad) {
                        log("Interstitial loaded");
                        interstitialAd = ad;
                        ad.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdShowedFullScreenContent() {
                                log("Interstitial shown");
                            }

                            @Override
                            public void onAdDismissedFullScreenContent() {
                                log("Interstitial dismissed");
                            }
                        });
                        ad.show(MainActivity.this);
                    }

                    @Override
                    public void onAdFailedToLoad(LoadAdError error) {
                        log("Interstitial failed: " + error.getMessage());
                    }
                });
    }

    private void loadRewarded() {
        log("Loading rewarded...");
        RewardedAd.load(this, REWARDED_AD_UNIT_ID, new AdRequest.Builder().build(),
                new RewardedAdLoadCallback() {
                    @Override
                    public void onAdLoaded(RewardedAd ad) {
                        log("Rewarded loaded");
                        rewardedAd = ad;
                        ad.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdShowedFullScreenContent() {
                                log("Rewarded shown");
                            }

                            @Override
                            public void onAdDismissedFullScreenContent() {
                                log("Rewarded dismissed");
                            }
                        });
                        ad.show(MainActivity.this, reward ->
                                log("User earned reward: " + reward.getAmount() + " " + reward.getType())
                        );
                    }

                    @Override
                    public void onAdFailedToLoad(LoadAdError error) {
                        log("Rewarded failed: " + error.getMessage());
                    }
                });
    }

    private void loadRewardedInterstitial() {
        log("Loading rewarded interstitial...");
        RewardedInterstitialAd.load(this, REWARDED_INTERSTITIAL_AD_UNIT_ID, new AdRequest.Builder().build(),
                new RewardedInterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(RewardedInterstitialAd ad) {
                        log("Rewarded interstitial loaded");
                        rewardedInterstitialAd = ad;
                        ad.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdShowedFullScreenContent() {
                                log("Rewarded interstitial shown");
                            }

                            @Override
                            public void onAdDismissedFullScreenContent() {
                                log("Rewarded interstitial dismissed");
                            }
                        });
                        ad.show(MainActivity.this, reward ->
                                log("Rewarded interstitial reward: " + reward.getAmount() + " " + reward.getType())
                        );
                    }

                    @Override
                    public void onAdFailedToLoad(LoadAdError error) {
                        log("Rewarded interstitial failed: " + error.getMessage());
                    }
                });
    }

    private void loadNative() {
        log("Loading native...");
        nativeAdLoader = new AdLoader.Builder(this, NATIVE_AD_UNIT_ID)
                .forNativeAd(ad -> {
                    log("Native loaded: headline='" + ad.getHeadline() + "'");
                    if (currentNativeAd != null) {
                        currentNativeAd.destroy();
                    }
                    currentNativeAd = ad;
                    displayNativeAd(ad);
                })
                .withAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(LoadAdError error) {
                        log("Native failed: " + error.getMessage());
                    }

                    @Override
                    public void onAdImpression() {
                        log("Native impression");
                    }

                    @Override
                    public void onAdClicked() {
                        log("Native clicked");
                    }
                })
                .build();
        nativeAdLoader.loadAd(new AdRequest.Builder().build());
    }

    private void displayNativeAd(NativeAd ad) {
        NativeAdView adView = (NativeAdView) getLayoutInflater().inflate(R.layout.native_ad_layout, null);

        TextView headlineView = adView.findViewById(R.id.ad_headline);
        headlineView.setText(ad.getHeadline());
        adView.setHeadlineView(headlineView);

        TextView bodyView = adView.findViewById(R.id.ad_body);
        bodyView.setText(ad.getBody());
        bodyView.setVisibility(ad.getBody() != null ? View.VISIBLE : View.GONE);
        adView.setBodyView(bodyView);

        Button ctaView = adView.findViewById(R.id.ad_call_to_action);
        ctaView.setText(ad.getCallToAction());
        ctaView.setVisibility(ad.getCallToAction() != null ? View.VISIBLE : View.GONE);
        adView.setCallToActionView(ctaView);

        ImageView iconView = adView.findViewById(R.id.ad_app_icon);
        if (ad.getIcon() != null) {
            iconView.setImageDrawable(ad.getIcon().getDrawable());
            iconView.setVisibility(View.VISIBLE);
        } else {
            iconView.setVisibility(View.GONE);
        }
        adView.setIconView(iconView);

        MediaView mediaView = adView.findViewById(R.id.ad_media);
        if (ad.getMediaContent() != null) {
            mediaView.setMediaContent(ad.getMediaContent());
        }
        adView.setMediaView(mediaView);

        TextView advertiserView = adView.findViewById(R.id.ad_advertiser);
        advertiserView.setText(ad.getAdvertiser());
        advertiserView.setVisibility(ad.getAdvertiser() != null ? View.VISIBLE : View.GONE);
        adView.setAdvertiserView(advertiserView);

        adView.setNativeAd(ad);

        nativeAdContainer.removeAllViews();
        nativeAdContainer.addView(adView);
        log("Native ad displayed");
    }

    private void loadAppOpen() {
        log("Loading app open...");
        AppOpenAd.load(this, APP_OPEN_AD_UNIT_ID, new AdRequest.Builder().build(),
                new AppOpenAd.AppOpenAdLoadCallback() {
                    @Override
                    public void onAdLoaded(AppOpenAd ad) {
                        log("App open loaded");
                        appOpenAd = ad;
                        ad.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdShowedFullScreenContent() {
                                log("App open shown");
                            }

                            @Override
                            public void onAdDismissedFullScreenContent() {
                                log("App open dismissed");
                            }
                        });
                        ad.show(MainActivity.this);
                    }

                    @Override
                    public void onAdFailedToLoad(LoadAdError error) {
                        log("App open failed: " + error.getMessage());
                    }
                });
    }

    @Override
    protected void onDestroy() {
        if (currentNativeAd != null) {
            currentNativeAd.destroy();
        }
        super.onDestroy();
    }

    private void log(String message) {
        String timestamp = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        runOnUiThread(() -> tvLog.append("\n[" + timestamp + "] " + message));
    }
}
