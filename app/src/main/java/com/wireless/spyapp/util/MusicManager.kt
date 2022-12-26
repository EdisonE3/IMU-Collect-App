package com.wireless.spyapp.util

import android.content.Context
import android.media.MediaPlayer
import android.media.MediaPlayer.OnCompletionListener
import android.os.SystemClock.sleep
import android.util.Log


class MusicManager// construction method with two parameter
    (context: Context, musicList: ArrayList<Int>) {
    // the context of the activity
    private var mContext: Context? = null
    // A list used to store all media player
    private var mediaPlayerList: MutableList<MediaPlayer> = ArrayList()
    // The music currently played
    private var nextMusicListIndex = 0
    // current media player
    private var mCurrentMediaPlayer: MediaPlayer? = null

    // implement static variable in kotlin
    companion object {
        var isStart = false
        var isPause = true //是否暂停
        var fileName = "" //文件名
            get() {
                return "$field$count.txt"
            }
        var count = 0 //计数
    }

    init {
        this.mContext = context
        // create media player for each musicList
        for (i in musicList.indices) {
            Log.d("MusicManager", "init media player: $i")
            val mediaPlayer = MediaPlayer.create(context, musicList[i])
            mediaPlayerList.add(mediaPlayer)
        }
    }

    fun startMusic() {
        Log.d("MusicManager", "start music, isPause: $isPause")
        if (isStart){
            if (isPause) {
                // 如果暂停了，就继续播放
                mCurrentMediaPlayer?.start()
                isPause = false
            } else {
                // 如果没有暂停，就从头开始播放
                startFromZero()
            }
        }else{
            // 如果没有开始，就从头开始播放
            startFromZero()
        }
    }

    fun pauseMusic() {
        Log.d("MusicManager", "pause music")
        mCurrentMediaPlayer?.pause()
        isPause = true
    }

    fun stopMusic() {
        Log.d("MusicManager", "stop music")
        mCurrentMediaPlayer?.stop()
        isPause = true
    }

    private fun startFromZero() {
        nextMusicListIndex = 0
        count = 0
        Log.d("tag", "开始播放$count 当前:$count 下一首:$nextMusicListIndex")
        mCurrentMediaPlayer = mediaPlayerList[nextMusicListIndex]
        mCurrentMediaPlayer?.start()
        mCurrentMediaPlayer?.setOnCompletionListener(OnCompletionListener {
            // 播放完了，count++
            Log.d("tag", "播放完毕$count 当前:$count 下一首:$nextMusicListIndex")
            count++

            // 播放完了先暫停
            isPause = true

            // 休息几秒钟
            sleep(2000)

            // 播放下一首
            if (nextMusicListIndex < mediaPlayerList.size) {
                Log.d("MusicManager", "start new music: $nextMusicListIndex")
                if (nextMusicListIndex < mediaPlayerList.size - 1) {
                    // 初始化一个MediaPlayer
                    mCurrentMediaPlayer = mediaPlayerList[nextMusicListIndex]
                    // 开始播放
                    mCurrentMediaPlayer?.start()
                    nextMusicListIndex++
                    isPause = false
                }
            }
        })
        nextMusicListIndex++
    }
}