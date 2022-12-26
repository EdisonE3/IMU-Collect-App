package com.wireless.spyapp

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.wireless.spyapp.util.MusicManager


// this activity is used to deal with the activity_music
class MusicActivity : AppCompatActivity(), View.OnClickListener {
    var musicManager: MusicManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music)

        val list: ArrayList<Int> = ArrayList()
        list.add(R.raw.t0_01_0)
        list.add(R.raw.t0_01_1)
        list.add(R.raw.t0_01_10)
        list.add(R.raw.t0_01_11)
        list.add(R.raw.t0_01_12)
        musicManager = MusicManager(this, list)

        val btnPlay: Button = findViewById<View>(R.id.btnPlay) as Button
        val btnPause: Button = findViewById<View>(R.id.btnPause) as Button
        val btnStop: Button = findViewById<View>(R.id.btnStop) as Button

        btnPlay.setOnClickListener(this)
        btnPause.setOnClickListener(this)
        btnStop.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        val mediaPlayer = musicManager?.mMediaPlayer!!
        when (v!!.id) {
            R.id.btnPlay -> {
                Log.d("MusicActivity", "play")
                musicManager?.setStart()
                //如果没在播放中，立刻开始播放。
                if (!mediaPlayer.isPlaying()) {
                    mediaPlayer.start()
                }
            }
            R.id.btnPause -> {
                //如果在播放中，立刻暂停。
                Log.d("MusicActivity", "pause11")
                musicManager?.setPause()
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause()
                }
            }
            R.id.btnStop -> {
                //如果在播放中，立刻停止。
                Log.d("MusicActivity", "stop")
                musicManager?.setPause()
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.reset()
                }
            }
            else -> {}
        }
    }
}