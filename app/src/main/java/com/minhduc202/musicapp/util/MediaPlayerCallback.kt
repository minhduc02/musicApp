package com.minhduc202.musicapp.util

interface MediaPlayerCallback {
    fun onPrepared()
    fun onFailed()
    fun onDone()
}