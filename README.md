# PremiumAds Google Ads Adapter V2 — Android Examples

Example apps demonstrating PremiumAds mediation adapter integration with Google Ads on Android.

## Examples

### android/admob — Google AdMob mediation
All supported ad formats: Banner, Interstitial, Rewarded, Rewarded Interstitial, Native, App Open.

Two language variants are provided:

| Variant | Path |
|---------|------|
| Kotlin | [android/admob/kotlin](android/admob/kotlin) |
| Java | [android/admob/java](android/admob/java) |

### android/admanager (coming soon)
Google Ad Manager mediation example.

## Getting Started

1. Open the example project in Android Studio
2. Sync Gradle
3. Configure your AdMob custom event in the [AdMob console](https://apps.admob.com):
   - **Class Name:** `net.premiumads.sdk.adapter.PremiumAdsAdapter`
   - **Parameter:** Your PremiumAds ad unit ID
4. Run the app

## Documentation

- [Integration Guide](https://docs.premiumads.net/v2.0/docs/google-admob)
- [Test Ad Units](https://docs.premiumads.net/v2.0/docs/enabling-test-ads)
- [Migration Guide](https://docs.premiumads.net/v2.0/docs/google-admob#migration-from-sdk-v1)

## Dependencies

```kotlin
repositories {
    maven { url = uri("https://repo.premiumads.net/artifactory/mobile-ads-sdk/") }
}
dependencies {
    implementation("net.premiumads.sdk:admob-adapter-v2:1.0.8")
}
```
