package com.atinem.snake

import android.content.Context
import android.graphics.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.random.Random

class Apple(context : Context, sr : Point, s : Float){

    val mLocation = Point()
    private val mSpawnRange : Point = sr

    private val mSize : Float = s

    private var mBitmapApple : Bitmap? = null


    init {
        mLocation.x = -10
        GlobalScope.launch(Dispatchers.IO){
            mBitmapApple = BitmapFactory.decodeResource(context.resources, R.drawable.apple)

            mBitmapApple = Bitmap.createScaledBitmap(mBitmapApple, s.toInt(), s.toInt(), false)
        }

    }

    fun spawn(){
        mLocation.x = Random.nextInt(mSpawnRange.x) + 1
        mLocation.y = Random.nextInt(mSpawnRange.y - 1) + 1
    }

    fun draw(canvas : Canvas, paint : Paint){
        canvas.drawBitmap(mBitmapApple,mLocation.x * mSize, mLocation.y * mSize, paint)
    }
}