package com.wireless.spyapp.util

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Environment
import java.io.File
import java.io.IOException


class MusicManager {
    // this class is used to play and pause music
    // It can be used to play music in the background
    // the music can be load from sdcard
    var mediaPlayer: MediaPlayer? = null

    // construct function
    init {
        this.mediaPlayer = MediaPlayer()
        initMediaPlayer()
    }

    fun initMediaPlayer() {
        try {
            val filePath = ""
            mediaPlayer?.setDataSource(filePath)
            mediaPlayer!!.isLooping = true //设置为循环播放
            mediaPlayer!!.prepare() //初始化播放器MediaPlayer
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}