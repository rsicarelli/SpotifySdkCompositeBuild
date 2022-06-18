pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Spotify SDK as Composite Build"
include(":app")
includeBuild("spotifyAppRemote")

/*
* Optional - automatically sync gradle files for included build
* */
rootDir.run {
    listOf(
        "gradle.properties",
        "gradlew.bat",
        "gradlew",
        "gradle/wrapper/gradle-wrapper.jar",
        "gradle/wrapper/gradle-wrapper.properties"
    ).map { path ->
        resolve(path)
            .copyTo(
                target = rootDir.resolve("spotifyAppRemote").resolve(path),
                overwrite = true
            )
    }
}