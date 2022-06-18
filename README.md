# Spotify Remote SDK as Gradle composite build
This demo is an alternative fix for the follow error:
```
> Direct local .aar file dependencies are not supported when building an AAR. 
The resulting AAR would be broken because the classes and Android resources from any local .aar 
file dependencies would not be packaged in the resulting AAR. Previous versions of the Android 
Gradle Plugin produce broken AARs in this case too (despite not throwing this error). The 
following direct local .aar file dependencies of the :library_module project caused this error: 
______.aar
```
More details on the [Stackoverflow question](https://stackoverflow.com/questions/60878599/error-building-android-library-direct-local-aar-file-dependencies-are-not-supp)


### Directions
We take advantage of [Gradle's Composite Builds](https://docs.gradle.org/current/userguide/composite_builds.html) to import Spotify Remote SDK as a normal dependency.


1. Create a folder eg. `spotifyAppRemote` at the same level of `app` folder
2. Add the desired `.aar` file at the root of `spotifyAppRemote` folder
3. Create a `settings.gradle.kts` file at the root of `spotifyAppRemote` folder. This file will be empty, it just needs to be there for the composite builds. See: [docs](https://docs.gradle.org/current/userguide/composite_builds.html#settings_defined_composite)
4. Create a `build.gradle.kts` file at the root of `spotifyAppRemote` folder
```kotlin
plugins {
    base //allows IDE clean to trigger clean on this module too
}

configurations.maybeCreate("default")
artifacts.add("default", file("spotify-app-remote-release-0.7.2.aar"))

//Change group to whatever you want. Here I'm using the package from the aar that I'm importing from
group = "com.spotify.android"
version = "0.7.2"
```
5. Next add Gradle files to this folder to allow this module to build itself. You can do it manually or add the following snippet at the root of `settings.gradle.kts` (!! the project root, not the empty one created above)
```kotlin
/* Optional - automatically sync gradle files for included build */
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
```
6. Now you can go ahead and add this folder as a module at the `settings.gradle.kts` on your project root. The same where may add the snippet above:
```kotlin
rootProject.name = "Your project name"
include(":app")
includeBuild("spotifyAppRemote")
```
7. Sync and build your project.
8. Now your included build will be available for your as a regular dependency with the defined group and version. To use this dependency:
```kotlin
dependencies {
    // group:moduleName:version
    implementation("com.spotify.android:spotifyAppRemote:0.7.2")
} 
```
