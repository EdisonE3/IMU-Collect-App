package com.wireless.spyapp.util

import android.R.bool
import android.content.Context
import android.media.MediaPlayer


class MusicManager// construction method with two parameter
    (context: Context, resId: Int) {
    var mediaPlayer :MediaPlayer? = null


    // implement static variable in kotlin
    companion object {
        var isPause = false //是否暂停
        var fileName = "accelerometer" //文件名
        get() {return "$field$count.txt"}
        var count = 0 //计数
    }

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
        count++
    }
}