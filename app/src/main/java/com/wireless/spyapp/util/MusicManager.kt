package com.wireless.spyapp.util

import android.content.Context
import android.media.MediaPlayer


class MusicManager// construction method with two parameter
    (context: Context, resId: Int) {
    var mediaPlayer :MediaPlayer? = null
    private var isPause = false //是否暂停

    init {
        initMediaPlayer(context, resId)
    }

    fun initMediaPlayer(context: Context, resId: Int) {
        mediaPlayer = MediaPlayer.create(context, resId)
        mediaPlayer?.setLooping(true)
    }

    fun setPause() {
        isPause = true
    }

    fun setStart() {
        isPause = false
    }
}