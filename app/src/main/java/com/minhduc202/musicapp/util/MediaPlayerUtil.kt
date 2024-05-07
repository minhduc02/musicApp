package com.minhduc202.musicapp.util

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log

object MediaPlayerUtil {
    private var mediaPlayer = MediaPlayer()
    private var curId = -1
    private var mediaBufferPercent = 0

    fun isPlaying(): Boolean {
        return mediaPlayer.isPlaying
    }

    fun getRemainingTime(): String {
        return if (mediaPlayer.isPlaying) FormatterUtil.millisecondsToFormattedTime(mediaPlayer.duration - mediaPlayer.currentPosition) else "0:00"
    }

    fun getCurrentTime(): String {
        return if (mediaPlayer.isPlaying) FormatterUtil.millisecondsToFormattedTime(mediaPlayer.currentPosition) else "0:00"
    }

    fun getDurationInSecond(): Int {
        return if (mediaPlayer.isPlaying) mediaPlayer.duration / 1000 else 0
    }

    fun getCurrentTimeInSecond(): Int {
        return if (mediaPlayer.isPlaying) mediaPlayer.currentPosition / 1000 else 0
    }

    fun getMediaBufferPercent(): Int {
        return ((mediaBufferPercent * mediaPlayer.duration / 1000) / 100)
    }

    fun seekTo(second: Int) {
        if (mediaPlayer.isPlaying) mediaPlayer.seekTo(second * 1000)
    }

    fun playNewOrResume(id: Int, url: String, isRepeat: Boolean = false, onDone: MediaPlayerCallback) {
        if (curId == id && !isRepeat) return
        try {
            if (isRepeat) {
                playOrPause()
                return
            }
            curId = id
            prepareForNew()
            mediaPlayer.setDataSource(url)
            Log.e("ABC", url)
            mediaPlayer.setOnPreparedListener {
                it.start()
                onDone.onPrepared()
            }
            mediaPlayer.setOnBufferingUpdateListener { _, percent ->
                mediaBufferPercent = percent
            }
            mediaPlayer.setOnCompletionListener {
                onDone.onDone()
            }
            mediaPlayer.prepareAsync()
        } catch (e: Exception) {
            onDone.onFailed()
            Log.e("ABC", e.message.toString())
        }
    }

    fun playOrPause() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
        } else {
            mediaPlayer.start()
        }
    }

    private fun prepareForNew() {
        if (mediaPlayer.isPlaying) mediaPlayer.stop()
        mediaPlayer.reset()
    }

    fun releaseAll() {
        if (mediaPlayer.isPlaying) mediaPlayer.stop()
        mediaPlayer.release()
    }

    fun playByUrl(context: Context, uri: Uri) {
        try {
            prepareForNew()
            mediaPlayer = MediaPlayer.create(context, uri)
            mediaPlayer.setOnPreparedListener {
                it.start()
            }
            mediaPlayer.setOnBufferingUpdateListener { _, percent ->
                mediaBufferPercent = percent
            }
            mediaPlayer.setOnCompletionListener {
            }
            mediaPlayer.prepareAsync()
        } catch (e: Exception) {
            Log.e("ABC", e.message.toString())
        }
    }
}