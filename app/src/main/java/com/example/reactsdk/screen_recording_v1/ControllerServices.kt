package com.example.reactsdk.screen_recording_v1

import android.app.ActivityManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.reactsdk.R
import com.example.reactsdk.screen_recording_v1.ScreenRecording.START_REC
import com.selflearningcoursecreationapp.utils.screenRecorder.home.FloatingWidgetService
import com.selflearningcoursecreationapp.utils.screen_recording_v1.ScreenRecordService

class ControllerServices : Service() {
    private val TAG = ControllerServices::class.java.simpleName
    private var mViewRoot: View? = null
    private var mWindowManager: WindowManager? = null
    var paramViewRoot: WindowManager.LayoutParams? = null
    private var mRecordingServiceBound = true

    var paramCam: WindowManager.LayoutParams? = null

    var paramCountdown: WindowManager.LayoutParams? = null


    private var mImgClose: TextView? = null
    private var mImgRec: ImageView? = null

    //    private var mImgStart: TextView? = null
    private var mImgStop: TextView? = null
    private var mImgSetting: ImageView? = null
    private var mImgPause: TextView? = null
    private var mImgResume: TextView? = null
    private var mRecordingStarted = false
    private var mRecordingPaused = false
    private var mScreenWidth = 0
    private var mScreenHeight: Int = 0
    private var mTvCountdown: TextView? = null
    private var mCountdownLayout: View? = null

    private lateinit var intentIs: Intent
    private var width: Int = 0
    private var height: Int = 0

    fun ControllerService() {}

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        intentIs = intent?.getParcelableExtra("data") ?: Intent()
        height = intent?.getIntExtra("height", 1080) ?: 1024
        width = intent?.getIntExtra("width", 720) ?: 720

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onCreate() {
        super.onCreate()
        updateScreenSize()
        if (paramViewRoot == null) {
            initParam()
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                val chan = NotificationChannel(
//                    NotificationHelper.CHANNEL_ID,
//                    NotificationHelper.CHANNEL_NAME,
//                    NotificationManager.IMPORTANCE_HIGH
//                )
//                chan.lightColor = Color.BLUE
//                chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
//                val manager = (getSystemService(NOTIFICATION_SERVICE) as NotificationManager)
//                manager.createNotificationChannel(chan)
//                val notificationBuilder =
//                    NotificationCompat.Builder(this, NotificationHelper.CHANNEL_ID)
//                val notification: Notification = notificationBuilder.setOngoing(true)
//                    .setSmallIcon(R.mipmap.ic_launcher)
//                    .setContentTitle("App is running in background")
//                    .setPriority(NotificationManager.IMPORTANCE_MIN)
//                    .setCategory(Notification.CATEGORY_SERVICE)
//                    .build()
//                startForeground(2, notification)
//            }
        }
        if (mViewRoot == null) initializeViews()
    }

    private fun initParam() {
        val LAYOUT_FLAG: Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            WindowManager.LayoutParams.TYPE_PHONE
        }
        paramViewRoot = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            LAYOUT_FLAG,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )
        paramCam = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            LAYOUT_FLAG,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )
        paramCountdown = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            LAYOUT_FLAG,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )
    }

    private fun updateScreenSize() {
        val metrics = resources.displayMetrics
        mScreenWidth = metrics.widthPixels
        mScreenHeight = metrics.heightPixels
    }

    private fun initializeViews() {
        mViewRoot = LayoutInflater.from(this).inflate(R.layout.layout_recording_via_other, null)
        val mViewCountdown: View =
            LayoutInflater.from(this).inflate(R.layout.layout_countdown, null)
        paramViewRoot!!.gravity = Gravity.BOTTOM or Gravity.CENTER
        paramViewRoot!!.x = 0
        paramViewRoot!!.y = 0
        mWindowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        mWindowManager!!.addView(mViewCountdown, paramCountdown)
        mWindowManager!!.addView(mViewRoot, paramViewRoot)
        mCountdownLayout = mViewCountdown.findViewById(R.id.countdown_container)
        mTvCountdown = mViewCountdown.findViewById(R.id.tvCountDown)
        toggleView(mCountdownLayout, View.GONE)
        mImgRec = mViewRoot?.findViewById(R.id.imgRec)
        mImgSetting = mViewRoot?.findViewById<ImageView>(R.id.imgSetting)
        mImgClose = mViewRoot?.findViewById(R.id.imgClose)
        mImgPause = mViewRoot?.findViewById(R.id.imgPause)
        mImgStop = mViewRoot?.findViewById(R.id.imgStop)
        mImgResume = mViewRoot?.findViewById(R.id.imgResume)
        toggleView(mImgResume, View.GONE)
        toggleView(mImgStop, View.VISIBLE)
        toggleNavigationButton(View.GONE)

        mImgPause?.setOnClickListener(View.OnClickListener {
            //                MyUtils.toast(getApplicationContext(), "Pause will available soon!", Toast.LENGTH_SHORT);
            toggleNavigationButton(View.GONE)
            mRecordingPaused = true
//            Toast.makeText(this, "pause", Toast.LENGTH_SHORT).show()
            screenRecord(ScreenRecording.PAUSE_REC)
        })

        mImgResume?.setOnClickListener(View.OnClickListener {
            screenRecord(ScreenRecording.RESUME_REC)
//            Toast.makeText(this, "resume", Toast.LENGTH_SHORT).show()
            toggleNavigationButton(View.GONE)

            mRecordingPaused = false
        })

//        mImgStart?.setOnClickListener(View.OnClickListener {
//            toggleNavigationButton(View.GONE)
//
//            Toast.makeText(this, "start", Toast.LENGTH_SHORT).show()
//            if (mRecordingServiceBound) {
//                if (!isMyServiceRunning(ScreenRecordService::class.java)) {
//                    toggleView(mCountdownLayout, View.VISIBLE)
//                    val countdown: Int = (SettingManager.getCountdown(application) + 1) * 1000
//                    object : CountDownTimer(countdown.toLong(), 1000) {
//                        override fun onTick(millisUntilFinished: Long) {
//                            toggleView(mViewRoot, View.GONE)
//                            mTvCountdown?.text = "" + millisUntilFinished / 1000
//                        }
//
//                        override fun onFinish() {
//                            toggleView(mCountdownLayout, View.GONE)
//                            toggleView(mViewRoot, View.VISIBLE)
//                            mRecordingStarted = true
//                            startRecordingService()
//
//                        }
//                    }.start()
//
//                } else {
//                    Toast.makeText(this, "Service already running", Toast.LENGTH_SHORT).show()
//                }
//            } else {
//                mRecordingStarted = false
//                Toast.makeText(this, "Recording Service connection has not been established", Toast.LENGTH_SHORT).show()
//
////                MyUtils.toast(
////                    applicationContext,
////                    "Recording Service connection has not been established",
////                    Toast.LENGTH_LONG
////                )
//                stopService()
//            }
//        })


        mImgStop?.setOnClickListener(View.OnClickListener
        {
            toggleNavigationButton(View.GONE)

            if (mRecordingServiceBound) {
                mRecordingStarted = false
//                Toast.makeText(this, "stop", Toast.LENGTH_SHORT).show()
                screenRecord(ScreenRecording.CLOSE_SERVICES)
            } else {
                mRecordingStarted = true
                Toast.makeText(
                    this,
                    "Recording Service connection has not been established",
                    Toast.LENGTH_SHORT
                ).show()
//                MyUtils.toast(
//                    applicationContext,
//                    "Recording Service connection has not been established",
//                    Toast.LENGTH_LONG
//                )
            }

        })

        mImgClose?.setOnClickListener(View.OnClickListener
        {
            mRecordingStarted = false

//            Toast.makeText(this, "close", Toast.LENGTH_SHORT).show()
            toggleNavigationButton(View.GONE)

//            stopService()
//            stopService(Intent(this, FloatingWidgetService::class.java))
//            stopService(Intent(this, ScreenRecordService::class.java))

//            if (!HomeActivity.active) {
//                val intent = Intent(
//                    applicationContext,
//                    HomeActivity::class.java
//                )
//                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//
//                startActivity(intent)
//            }
        })


        mViewRoot?.findViewById<View>(R.id.root_container)?.setOnTouchListener(
            object : View.OnTouchListener {
                private var initialX = 0
                private var initialY = 0
                private var initialTouchX = 0f
                private var initialTouchY = 0f
                override fun onTouch(v: View, event: MotionEvent): Boolean {
                    when (event.action) {
                        MotionEvent.ACTION_DOWN -> {
                            //remember the initial position.
                            initialX = paramViewRoot!!.x
                            initialY = paramViewRoot!!.y

                            //get the touch location
                            initialTouchX = event.rawX
                            initialTouchY = event.rawY
                            return true
                        }

                        MotionEvent.ACTION_UP -> {
//                                if (event.rawX < width / 2) {
//                                    paramViewRoot!!.x = 0
//                                } else {
//                                    paramViewRoot!!.x = width
//                                }
//                                paramViewRoot!!.x = 0
                            paramViewRoot!!.y = initialY + (event.rawY - initialTouchY).toInt()
                            paramViewRoot!!.x = initialX + (event.rawX - initialTouchX).toInt()

                            //Update the layout with new X & Y coordinate
                            mWindowManager!!.updateViewLayout(mViewRoot, paramViewRoot)
                            val Xdiff = (event.rawX - initialTouchX).toInt()
                            val Ydiff = (event.rawY - initialTouchY).toInt()

                            //The check for Xdiff <10 && YDiff< 10 because sometime elements moves a little while clicking.
                            //So that is click event.
                            if (Xdiff < 10 && Ydiff < 10) {
                                if (isViewCollapsed()) {
                                    //When user clicks on the image view of the collapsed layout,
                                    //visibility of the collapsed layout will be changed to "View.GONE"
                                    //and expanded view will become visible.
                                    toggleNavigationButton(View.VISIBLE)
                                } else {
                                    toggleNavigationButton(View.GONE)
                                }
                            }
                            return true
                        }

                        MotionEvent.ACTION_MOVE -> {
//                                //Calculate the X and Y coordinates of the view.
//                                paramViewRoot!!.x = initialX + (event.rawX - initialTouchX).toInt()
//                                paramViewRoot!!.y = initialY + (event.rawY - initialTouchY).toInt()
//
//                                //Update the layout with new X & Y coordinate
//                                mWindowManager!!.updateViewLayout(mViewRoot, paramViewRoot)
//                                return true
                            paramViewRoot?.x = initialX + (event.rawX - initialTouchX).toInt()
                            paramViewRoot?.y = initialY + (event.rawY - initialTouchY).toInt()
                            mWindowManager?.updateViewLayout(mViewRoot, paramViewRoot)
                            return true
                        }
                    }
                    return false
                }
            })

        mViewRoot?.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus)
                toggleNavigationButton(View.GONE)
        }

    }

    private fun screenRecord(i: Int) {
        val intent2 = Intent(applicationContext, ScreenRecordService::class.java)
        intent2.putExtra("command", i)
        intent2.putExtra("largeIconId", R.mipmap.ic_launcher)
        intent2.putExtra("smallIconId", R.mipmap.ic_launcher)
        intent2.putExtra("width", width)
        intent2.putExtra("height", height)
        intent2.putExtra("data", intentIs)
        if (i == ScreenRecording.CLOSE_SERVICES) {
            stopService(intent2)
            stopService(Intent(this, FloatingWidgetService::class.java))
            stopService(Intent(this, ControllerServices::class.java))

        } else {
            startService(intent2)
        }
    }

    private fun stopService() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                stopForeground(true)
                stopSelf()
            } else {
                stopSelf()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun startRecordingService() {
        try {
            val intent2 = Intent(applicationContext, ScreenRecordService::class.java)
            intent2.putExtra("command", START_REC)
            intent2.putExtra("largeIconId", R.mipmap.ic_launcher)
            intent2.putExtra("smallIconId", R.mipmap.ic_launcher)
            intent2.putExtra("width", width)
            intent2.putExtra("height", height)
            intent2.putExtra("data", intentIs)
            startService(intent2)
            startService(
                Intent(
                    applicationContext,
                    FloatingWidgetService::class.java
                )
            )
//        val recorderIntent = Intent(ACTION_RECORDER_BROADCAST_NOTIFY)
//        recorderIntent.putExtras(bundleOf("recordingStatus" to 4))
            try {
                val recorderIntent = Intent(ACTION_RECORDER_BROADCAST)
                recorderIntent.putExtras(bundleOf("recordingStatus" to 4))
                LocalBroadcastManager.getInstance(this)
                    .sendBroadcast(recorderIntent)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun toggleView(view: View?, visible: Int) {
        view?.visibility = visible
    }


    private fun isViewCollapsed(): Boolean {
        return mViewRoot == null || mViewRoot?.findViewById<View>(R.id.imgSetting)?.visibility == View.GONE
    }


    fun toggleNavigationButton(viewMode: Int) {
        //Todo: make animation here
//        mImgStart!!.visibility = viewMode
        mImgSetting?.visibility = viewMode
        mImgPause!!.visibility = viewMode
//        mImgCapture.setVisibility(viewMode)
//        mImgLive.setVisibility(viewMode)
//        mImgClose!!.visibility = viewMode
        mImgStop!!.visibility = viewMode
        mImgResume!!.visibility = viewMode
        if (viewMode == View.GONE) {
            mViewRoot!!.setPadding(32, 32, 32, 32)
        } else {
            if (mRecordingStarted) {
//                mImgStart!!.visibility = View.GONE
            } else {
                mImgStop!!.visibility = View.VISIBLE
            }
            if (mRecordingPaused) {
                mImgPause!!.visibility = View.GONE
            } else {
                mImgResume!!.visibility = View.GONE
            }
            mViewRoot!!.setPadding(32, 48, 32, 48)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        if (mViewRoot != null) {
            mWindowManager!!.removeViewImmediate(mViewRoot)
        }
        if (mRecordingServiceBound) {
            stopSelf()
            mRecordingServiceBound = false
        }

    }

    private fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager: ActivityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }
}