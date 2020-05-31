package com.burakusluer.foursquarekotlin.service

import android.app.Application
import com.parse.Parse


class Back4App : Application() {
    override fun onCreate() {
        super.onCreate()
        Parse.initialize(
            Parse.Configuration.Builder(this)
                .applicationId("aOT5Y2FuDi6wIzg98NXSFy0uyEbYoltS4aPFd5Pe")
                .clientKey("q6JXTqpQTwUaM7P3hBKWEWpKdRELSjkG9akJacIL")
                .server("https://parseapi.back4app.com")
                .build()
        )
    }
}