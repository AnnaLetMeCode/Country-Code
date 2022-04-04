package com.country.example

import android.app.Application
import com.country.emoji.utils.EmojiCompatManager

class ExampleApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        EmojiCompatManager.init(this)
    }
}