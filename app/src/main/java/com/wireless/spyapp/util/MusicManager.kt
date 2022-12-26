package com.wireless.spyapp.util

import android.content.Context
import android.content.res.AssetFileDescriptor
import android.media.MediaPlayer
import android.media.MediaPlayer.OnCompletionListener
import android.net.Uri
import android.os.SystemClock.sleep
import android.util.Log


class MusicManager// construction method with two parameter
    (context: Context, musicList: ArrayList<Int>) {
    var mMediaPlayer: MediaPlayer? = null
    var mContext: Context? = null

    // A list used to store all music id
    var musicList: MutableList<Int> = ArrayList()
    // The music currently played
    private var musicListIndex = 0

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
        this.mMediaPlayer = MediaPlayer.create(context, musicList[0])
        this.mContext = context
        this.musicList = musicList
        this.musicListIndex = 1
        initMediaPlayer()
    }

    private fun initMediaPlayer() {
        Log.d("MusicManager", "set music manager states")
        mMediaPlayer?.isLooping = false //设置循环播放
        mMediaPlayer?.setOnCompletionListener(OnCompletionListener {
            Log.d("tag", "播放完毕$count")

            // 播放完了先暫停
            isPause = true

            // 休息几秒钟
            sleep(2000)

            // 播放下一首
            if (musicListIndex < musicList.size) {
                Log.d("MusicManager", "start new music: $musicListIndex")
                // 初始化一个MediaPlayer
                mMediaPlayer = MediaPlayer.create(mContext, musicList[musicListIndex])
                // 设置该MediaPlayer的监听器
                initMediaPlayer()
                // 开始播放
                mMediaPlayer?.start()
                musicListIndex++
                count++
                isPause = false
            }
        })
    }

    fun setPause() {
        isPause = true
    }

    fun setStart() {
        isPause = false
        count++
    }

    private fun setAndStartNextMusic(musicId:Int) {
        val afd: AssetFileDescriptor = mContext?.resources?.openRawResourceFd(musicId)!!
        Log.d("MusicManager", "afd: ${afd.toString()}")
        mContext?.let { mMediaPlayer?.setDataSource(afd) }
        mMediaPlayer?.prepare()
        mMediaPlayer?.start()
    }
}