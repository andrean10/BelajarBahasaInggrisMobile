package com.tribuanabagus.belajarbahasainggris.view.main.ui.teacher

import android.content.res.AssetFileDescriptor
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.tribuanabagus.belajarbahasainggris.R

class TeacherActivity : AppCompatActivity() {
    private var isReady: Boolean = false
    private var isFinished: Boolean = false

    private  val TAG = TeacherActivity::class.java.simpleName

    private lateinit var audioRaw: AssetFileDescriptor
    lateinit var mediaPlayer: MediaPlayer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teacher)

        //play music as long as lifecycle this activity not destroyed
        mediaPlayer = MediaPlayer()
        audioRaw = applicationContext.resources.openRawResourceFd(R.raw.music_app_new)
        prepareMediaPlayer()

        mediaPlayer.setOnCompletionListener { _mediaPlayer ->
            isFinished = true
            _mediaPlayer.release()
            mediaPlayer = MediaPlayer()
            audioRaw = applicationContext.resources.openRawResourceFd(R.raw.music_app_new)
            mediaPlayer.isLooping = true
            prepareMediaPlayer()
        }
    }

    private fun prepareMediaPlayer() {
        val attribute = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
            .build()
        try {
            mediaPlayer.setAudioAttributes(attribute)
            mediaPlayer.setDataSource(
                audioRaw.fileDescriptor,
                audioRaw.startOffset,
                audioRaw.length
            )
            mediaPlayer.prepare()
        } catch (e: Exception) {
            Log.e(TAG, "prepareMediaPlayer: ${e.message}")
        }

        mediaPlayer.setOnPreparedListener {
            isReady = true
            mediaPlayer.start()
        }
        mediaPlayer.setOnErrorListener { _, _, _ -> false }
    }


    override fun onStart() {
        super.onStart()
        mediaPlayer.start()
    }

    override fun onStop() {
        super.onStop()
        mediaPlayer.pause()
    }

    override fun onDestroy() {
        mediaPlayer.release()
        super.onDestroy()
    }
}