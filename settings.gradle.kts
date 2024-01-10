pluginManagement {
    repositories {
        google()
        mavenCentral()

        maven ("https://jitpack.io" )
        maven ("https://jcenter.bintray.com" )
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven ("https://jitpack.io" )
        maven ("https://jcenter.bintray.com" )
    }
}

rootProject.name = "Insulin-dose-App"
include(":app")
 