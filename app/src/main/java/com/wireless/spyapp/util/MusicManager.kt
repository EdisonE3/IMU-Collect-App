package com.wireless.spyapp.util

import android.content.Context
import android.media.MediaPlayer
import android.media.MediaPlayer.OnCompletionListener
import android.os.SystemClock.sleep
import android.util.Log


class MusicManager// construction method with two parameter
    (context: Context, resId: Int) {
    var mMediaPlayer: MediaPlayer? = null
    var currentMusicId = 0
    var mContext: Context? = null
    var mResId = 0
    var isInit = false
    // A list used to store all music id
    var musicList: MutableList<Int> = ArrayList()

    // implement static variable in kotlin
    companion object {
        var isPause = true //是否暂停
        var fileName = "" //文件名
            get() {
                return "$field$count.txt"
            }
        var count = 0 //计数
    }

    init {
        initMediaPlayer(context, resId)
    }

    fun initMediaPlayer(context: Context, resId: Int) {
        if (!isInit){
            mMediaPlayer = MediaPlayer.create(context, resId)
            mContext = context
            mResId = resId
            Log.d("MusicManager", "init")
            isInit = true
            mMediaPlayer?.setLooping(false) //设置循环播放
            mMediaPlayer?.setOnCompletionListener(OnCompletionListener {
                Log.d("tag", "播放完毕" + count)
//                count++
                isPause = true
//                sleep(200)
//                this.initMediaPlayer(mContext!!, mResId)
//                isPause = false
            })
        }
    }

    fun setPause() {
        isPause = true
    }

    fun setStart() {
        isPause = false
        count++
    }
}