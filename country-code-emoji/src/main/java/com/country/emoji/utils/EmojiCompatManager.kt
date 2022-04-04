package com.country.emoji.utils

import android.content.Context
import androidx.core.provider.FontRequest
import androidx.emoji.text.EmojiCompat
import androidx.emoji.text.FontRequestEmojiCompatConfig
import com.country.emoji.R


class EmojiCompatManager {

    companion object {
        /**
         * Initialisation EmojiCompat with downloadable font configuration to display last google emoji
         */
        fun init(application: Context) {
            val config: EmojiCompat.Config
            val fontRequest = FontRequest(
                "com.google.android.gms.fonts",
                "com.google.android.gms",
                "Noto Color Emoji Compat",
                R.array.com_google_android_gms_fonts_certs
            )
            config = FontRequestEmojiCompatConfig(application, fontRequest)
                .setReplaceAll(true)
                .registerInitCallback(object : EmojiCompat.InitCallback() {
                    override fun onInitialized() {
                        //TODO log success result, needs log component
                    }

                    override fun onFailed(throwable: Throwable?) {
                        //TODO log throwable, needs log component
                    }
                })
            EmojiCompat.init(config)
        }
    }
}