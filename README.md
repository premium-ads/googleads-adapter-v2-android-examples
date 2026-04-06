# PremiumAds Google Ads Adapter V2 — Android Examples

Example apps demonstrating PremiumAds mediation adapter integration with Google Ads on Android.

## Examples

### [android/admob](android/admob)
Google AdMob mediation example with all supported ad formats:
- Banner
- Interstitial
- Rewarded
- Rewarded Interstitial
- Native
- App Open

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
- [Migration Guide](https://docs.premiumads.net/v2.0/docs/google-admob#migration-from-sdk-v1)

## Dependencies

```kotlin
repositories {
    maven { url = uri("https://repo.premiumads.net/artifactory/mobile-ads-sdk/") }
}
dependencies {
    implementation("net.premiumads.sdk:admob-adapter-v2:+")
}
```
