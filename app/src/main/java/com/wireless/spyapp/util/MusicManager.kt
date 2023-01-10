package com.wireless.spyapp.util

import android.content.Context
import android.media.MediaPlayer
import android.os.SystemClock.sleep
import android.util.Log
import kotlin.concurrent.thread


class MusicManager// construction method with two parameter
    (context: Context, musicList: ArrayList<Int>) {
    // the context of the activity
    private var mContext: Context? = null
    // A list used to store all media player
    private var mediaPlayerList: MutableList<Int> = ArrayList()
    // The music currently played
    private var nextMusicListIndex = 0
    // current media player
    private var mCurrentMediaPlayer: MediaPlayer? = null

    // implement static variable in kotlin
    companion object {
        var dataSetNumber = 1
        var currentMusicNumber = 0 //当前播放的数字
        var currentListCount = 0
        var count = 0 //计数
        var isStart = false
        var isPause = true //是否暂停
        var fileName = "" //文件名
            get() {
                return "_" + dataSetNumber + "_" + currentMusicNumber + "_" + currentListCount + ".txt"
            }
    }

    init {
        this.mContext = context
        // create media player for each musicList
        for (i in musicList.indices) {
            Log.d("MusicManager", "init media player: $i")
            mediaPlayerList.add(musicList[i])
        }
    }

    fun startMusic() {
        Log.d("MusicManager", "start music, isPause: $isPause, isStart: $isStart")
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

    fun testMusic() {
        Log.d("MusicManager", "test music")
        val thread = Thread {
            currentListCount = 777
            currentMusicNumber = 777
            isPause = false
            isStart = true


            val playList = listOf(999, 58, 2009, 3222, 654, 4233)
            for (i in playList.indices) {
                Log.d("MusicManager", "test music: $i")
                mCurrentMediaPlayer = MediaPlayer.create(mContext, mediaPlayerList[playList[i]])
                mCurrentMediaPlayer?.isLooping = false
                mCurrentMediaPlayer?.start()
                while (mCurrentMediaPlayer?.isPlaying == true){
                    if (isPause){
                        break
                    }

                    if (!isStart){
                        break
                    }
                }
                mCurrentMediaPlayer?.stop()
                mCurrentMediaPlayer?.release()
                sleep(200)
            }


            isPause = true
            isStart = false
            currentListCount = 0
            currentMusicNumber = 0
        }
        thread.start()


    }

    fun stopMusic() {
        Log.d("MusicManager", "stop music")
        isPause = true
        isStart = false
    }

    private fun startFromZero() {
        // launch a new thread to start music
        val thread = Thread(Runnable {
//            currentListCount = 999
//            currentMusicNumber = 999
//            isPause = false
//            isStart = true
//            sleep(10000)
//            isPause = true
//            isStart = false
//            currentListCount = 0
//            currentMusicNumber = 0

            isPause = false
            isStart = true
            Log.d("MusicManager", "start music from zero, isPause: $isPause, isStart: $isStart")
            nextMusicListIndex = 0
            var currentCount = 0
            val select_num = 0
            currentMusicNumber = select_num
            currentCount = select_num * 500
            nextMusicListIndex = currentCount
            val endCount = currentCount + 500 * 5
            while (nextMusicListIndex < mediaPlayerList.size && !isPause && isStart) {
                if (currentCount >= endCount) {
                    break
                }

                sleep(200)
                Log.d("tag", "开始播放$currentCount 下一首:$nextMusicListIndex size:${mediaPlayerList.size}")
                mCurrentMediaPlayer = MediaPlayer.create(mContext, mediaPlayerList[nextMusicListIndex++])
                mCurrentMediaPlayer?.isLooping = false
                mCurrentMediaPlayer?.start()
                while (mCurrentMediaPlayer?.isPlaying == true){
                    if (isPause){
                        break
                    }

                    if (!isStart){
                        break
                    }
                }
                mCurrentMediaPlayer?.stop()
                mCurrentMediaPlayer?.release()
                Log.d("tag", "播放结束$currentCount 下一首:$nextMusicListIndex size:${mediaPlayerList.size}")


                currentCount++
                if (currentCount % 500 == 0) {
                    Log.d("tag", "Stored File Name: $fileName")
                    isPause = true
                    sleep(20)
                    currentListCount = 0
                    currentMusicNumber++
                    isPause = false
                    Log.d("tag", "Stored File Name: $fileName")
                } else {
                    Log.d("tag", "Stored File Name: $fileName")
                    isPause = true
                    sleep(20)
                    currentListCount++
                    isPause = false
                    Log.d("tag", "Stored File Name: $fileName")
                }
            }
            Log.d("MusicManager", "all music complete")
            isPause = true
            isStart = false
        })
        thread.start()
//        thread.join()
        Log.d("MusicManager", "start music from zero end")
    }
}