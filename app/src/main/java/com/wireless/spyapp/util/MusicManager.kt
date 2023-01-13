package com.wireless.spyapp.util

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.SystemClock.sleep
import android.util.Log


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
            currentListCount = 0
            sleep(200)
            isPause = false
            isStart = true

            for (_i in 0..9){
                sleep(2000)
                // a for loop from 0 to 499
                val selected = _i
                currentListCount = 0
                currentMusicNumber = selected
                val startIndex = selected * 500
                val endIndex = startIndex + 500
                // 1500 1300 1100
                val duration = 1500L

                Log.d("MusicManager", "test music, currentMusicNumber: $currentMusicNumber")
                for (i in startIndex until endIndex) {
                    if (isPause || !isStart) {
                        break
                    }

                    if (i % 50 == 0 && i > startIndex) {
                        isPause = true
                        isStart = false
                        sleep(2000)
                        Log.d("MusicManager", "test music, currentMusicNumber: $currentMusicNumber")
                        currentListCount++
                        isPause = false
                        isStart = true
                        sleep(2000)
                    }

                    Log.d("MusicManager", "test music: $i")
                    mCurrentMediaPlayer = MediaPlayer.create(mContext, mediaPlayerList[i])
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
                    val sleepTime = duration - mCurrentMediaPlayer!!.duration
                    mCurrentMediaPlayer?.stop()
                    mCurrentMediaPlayer?.release()
                    sleep(sleepTime)
                }
            }

            sleep(2000)
            isPause = true
            isStart = false

        }
        thread.start()


    }

    fun stopMusic() {
        Log.d("MusicManager", "stop music")
        isPause = true
        isStart = false
    }

    private fun startFromZero() {
        Log.d("MusicManager", "test music")
        // generate a list include 0, 2, 3
        val thread = Thread {
            val intArray = intArrayOf(3111, 3108, 3512, 3518, 4255, 4260, 4525, 4530)
            currentListCount = 10
            currentMusicNumber = 666
            sleep(200)
            isPause = false
            isStart = true

            sleep(4000)
            for (i in intArray){
                // a for loop from 0 to 499
                val selected = i
                val startIndex = selected * 500
                val endIndex = startIndex + 500
                // 1500 1300 1100
                val duration = 1300L

                Log.d("MusicManager", "test music, currentMusicNumber: $currentMusicNumber")
                if (isPause || !isStart) {
                    break
                }

                Log.d("MusicManager", "test music: $i")
                mCurrentMediaPlayer = MediaPlayer.create(mContext, mediaPlayerList[i])
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
                val sleepTime = duration - mCurrentMediaPlayer!!.duration
                mCurrentMediaPlayer?.stop()
                mCurrentMediaPlayer?.release()
                sleep(sleepTime)
            }

            sleep(2000)
            isPause = true
            isStart = false

        }
        thread.start()

        isPause = false
        isStart = true
        currentListCount = 10
        currentMusicNumber = 10
        Log.d("MusicManager", "start music from zero end")
    }
}