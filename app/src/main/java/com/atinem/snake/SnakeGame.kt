package com.atinem.snake

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.IOException

class SnakeGame(context : Context, size : Point) : SurfaceView(context) {

    companion object {
        private const val NUM_BLOCK_WIDE = 40f
    }

    private var mNextFrameTime : Long = 0

    private var mPlaying : Boolean = false
    private var mPaused : Boolean = true

    private val mSP : SoundPool
    private var mEatID = -1
    private var mCrashID = -1

    private val mNumbBlocksHigh : Float

    private var mScore : Int = 0

    private val mSurfaceHolder : SurfaceHolder
    private val mPaint : Paint

    //private val mSnake : SnakeGame
    private val mApple : Apple

    private lateinit var job : Job

    init {
        val blockSize = size.x / NUM_BLOCK_WIDE
        mNumbBlocksHigh = size.y / blockSize

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            val attributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()
            mSP = SoundPool.Builder()
                .setMaxStreams(5)
                .setAudioAttributes(attributes)
                .build()
        }else{
            mSP = SoundPool(5,AudioManager.STREAM_MUSIC,0)
        }
        try{
            GlobalScope.launch(Dispatchers.IO){
                val assetManager = context.assets
                var descriptor = assetManager.openFd("get_apple.ogg")
                mEatID = mSP.load(descriptor,0)

                descriptor = assetManager.openFd("snake_death.ogg")
                mCrashID = mSP.load(descriptor,0)

            }
        }catch (e : IOException){
            //TODO CATCH ERROR
        }

        mSurfaceHolder = holder
        mPaint = Paint()

        mApple = Apple(context, Point(NUM_BLOCK_WIDE.toInt(), mNumbBlocksHigh.toInt()),blockSize)
    }

    fun newGame(){
        //reset the snake

        //get the apple ready
        mApple.spawn()
        //reset the score
        mScore = 0

        //setup mNextFrametime so an update can be triggered
        mNextFrameTime = System.currentTimeMillis()
    }

    fun runJob(){
        job = GlobalScope.launch {
            while(mPlaying){
                if(!mPaused){
                    //Update 10 times a second
                    if(updateRequired()){
                        update()
                    }
                }
                draw()
            }
        }
    }

    fun updateRequired() : Boolean {
        val TARGET_FPS = 10
        val MILLIS_PER_SECOND = 1000

        if(mNextFrameTime <= System.currentTimeMillis()){
            mNextFrameTime = System.currentTimeMillis() + MILLIS_PER_SECOND / TARGET_FPS
            return true
        }

        return false
    }

    fun update(){
        //Move the snake

        //Did the head of the snake eat the apple?

        //Did the snake die?
    }

    fun draw(){
        if(mSurfaceHolder.surface.isValid){
            val canvas = mSurfaceHolder.lockCanvas()
            canvas.drawColor(Color.argb(255,26,128,182))

            mPaint.color = Color.argb(255,255,255,255)
            mPaint.textSize = 120f

            canvas.drawText("$mScore", 20f, 120f, mPaint)

            //Draw the apple and the snake
            mApple.draw(canvas,mPaint)
            //Draw some text while paused
            if(mPaused){
                mPaint.color = Color.argb(255,255,255,255)
                mPaint.textSize = 250f

                canvas.drawText(resources.getString(R.string.tap_to_play), 200f, 700f, mPaint)
            }
            mSurfaceHolder.unlockCanvasAndPost(canvas)
        }


    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event?.action){
            MotionEvent.ACTION_UP -> {
                if(mPaused) {
                    mPaused = false
                    newGame()

                    return true
                }

                //Let the Snake class handle the input

            }
        }
        return true
    }

    fun pause(){
        mPlaying = false
        job.cancel()
    }

    fun resume(){
        mPlaying = true
        runJob()
    }
}