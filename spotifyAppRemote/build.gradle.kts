plugins {
    base
}

configurations.maybeCreate("default")
artifacts.add("default", file("spotify-app-remote-release-0.7.2.aar"))

group = "com.spotify.android"
version = "0.7.2"