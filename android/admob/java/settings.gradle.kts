pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://repo.premiumads.net/artifactory/mobile-ads-sdk/") }
    }
}

rootProject.name = "PremiumAdsAdapterV2ExampleJava"
include(":app")
