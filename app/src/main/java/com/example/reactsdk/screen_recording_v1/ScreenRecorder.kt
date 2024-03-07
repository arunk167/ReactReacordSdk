package com.selflearningcoursecreationapp.utils.screen_recording_v1

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioAttributes
import android.media.AudioPlaybackCaptureConfiguration
import android.media.MediaRecorder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.view.Surface
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import me.daemon.storage.VideoMetadata
import me.daemon.storage.openMedia
import me.daemon.storage.openFileDescriptor


class ScreenRecorder(
    context: Context,
    width: Int,
    height: Int,
    densityDpi: Int,
) : ScreenAction(context.applicationContext, width, height, densityDpi) {

    private val mediaRecorder by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(context.applicationContext)
        } else {
            MediaRecorder()
        }
    }

    private var audioPlaybackCaptureConfiguration: AudioPlaybackCaptureConfiguration? = null

    private var videoUri: Uri? = null

    override fun surface(): Surface = mediaRecorder.surface

    override fun init() {

            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT)
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE)
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)

        val uri = context.openVideo(
            VideoMetadata
                .builder()
                .name("screen-${timestamp()}.mp4")
                .width(width)
                .height(height)
                .build()
        ) ?: return

        videoUri = uri

        val descriptor = context.openFileDescriptor(uri) ?: return

        mediaRecorder.setOutputFile(descriptor.fileDescriptor)
        mediaRecorder.setVideoSize(width, height)
        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264)
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
        mediaRecorder.setVideoEncodingBitRate(2 * 1920 * 1080)
        mediaRecorder.setVideoFrameRate(18)
        mediaRecorder.prepare()
        mediaRecorder.start()
    }

    override fun start(intent: Intent,height: Int,width: Int) {
        super.start(intent,
            height, width)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            mediaProjection?.let {
                val builder = AudioPlaybackCaptureConfiguration.Builder(it)
                builder.addMatchingUsage(AudioAttributes.USAGE_MEDIA)
                builder.addMatchingUsage(AudioAttributes.USAGE_ALARM)
                builder.addMatchingUsage(AudioAttributes.USAGE_GAME)
                audioPlaybackCaptureConfiguration = builder.build()
            }

        }
    }

    fun Context.openVideo(
        metadata: VideoMetadata,
        block: ContentValues.() -> Unit = {},
    ): Uri? =
        openMedia(metadata, block)
    override fun stop() {
        mediaRecorder.stop()
        mediaRecorder.release()
        videoUri?.let {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                context.closeMedia(it)
            }
            videoCallback(it)
        }
        videoUri = null

        super.stop()
    }

    fun resume(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mediaRecorder.resume()
        }
    }

    fun pause(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mediaRecorder.pause()
        }
    }

}
fun Context.closeMedia(uri: Uri) {
    val contentValues = ContentValues()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        contentValues.put(MediaStore.MediaColumns.IS_PENDING, 0)
    }
    contentResolver.update(uri, contentValues, null, null)
}
//
//private fun Context.openFileDescriptor(uri: Any): Nothing? {
//}
