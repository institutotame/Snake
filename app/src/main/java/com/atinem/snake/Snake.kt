package com.atinem.snake

import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class Snake(context : Context, mr : Point, ss : Int) {

    private val segmentLocations = mutableListOf<Point>()

    private val mSegmentSize = ss.toFloat()

    private val mMoveRange = mr

    private val halfWayPoint = mr.x * ss /2

    private enum class Heading {
        UP, RIGHT, DOWN, LEFT
    }

    private var heading = Heading.RIGHT

    private var mBitmapHeadRight : Bitmap? = null
    private var mBitmapHeadLeft : Bitmap? = null
    private var mBitmapHeadUp : Bitmap? = null
    private var mBitmapHeadDown : Bitmap? = null

    private var mBitmapBody : Bitmap? = null

    init{
        GlobalScope.launch(Dispatchers.IO) {
            val bitmapHead = BitmapFactory.decodeResource(context.resources, R.drawable.head)
            val bitmapBody = BitmapFactory.decodeResource(context.resources, R.drawable.body)
            mBitmapHeadRight = Bitmap.createScaledBitmap(bitmapHead, ss, ss,false)
            val matrix = Matrix()
            matrix.preScale(-1f, 1f)
            mBitmapHeadLeft  = Bitmap.createBitmap(mBitmapHeadRight, 0, 0, ss, ss, matrix, false)
            matrix.preRotate(-90f)
            mBitmapHeadUp = Bitmap.createBitmap(mBitmapHeadRight, 0, 0, ss, ss, matrix, true)

            matrix.preRotate(180f)
            mBitmapHeadDown  = Bitmap.createBitmap(mBitmapHeadRight, 0, 0, ss, ss, matrix, true)

            mBitmapBody = Bitmap.createScaledBitmap(bitmapBody, ss, ss, false)
        }
    }

    fun reset(w : Int, h : Int){
        heading = Heading.RIGHT

        segmentLocations.clear()

        segmentLocations.add(Point(w/2, h/2))
    }

    fun move(){
        for(i in (segmentLocations.size -1) downTo 1){
            segmentLocations[i].x = segmentLocations[i-1].x

            segmentLocations[i].y = segmentLocations[i-1].y
        }

        val p = segmentLocations.get(0)

        when(heading){
            Heading.UP -> p.y--
            Heading.RIGHT -> p.x++
            Heading.DOWN -> p.y++
            Heading.LEFT -> p.x--
        }

        segmentLocations.set(0,p)
    }

    fun detectDeath() : Boolean{
        var dead = false

        if(segmentLocations[0].x == -1 ||
            segmentLocations[0].x > mMoveRange.x ||
            segmentLocations[0].y == -1 ||
            segmentLocations[0].y > mMoveRange.y){
            dead = true
        }
        return dead
    }

    fun checkDinner(p : Point) : Boolean {
        if (segmentLocations[0].x == p.x &&
            segmentLocations[0].y == p.y)
        {
            segmentLocations.add(Point(-10, -10))
            return true
        }
        return false
    }

    fun draw(canvas : Canvas, paint : Paint){
        if(!segmentLocations.isEmpty()){
            when(heading){
                Heading.RIGHT -> {
                    canvas.drawBitmap(mBitmapHeadRight,
                        segmentLocations[0].x * mSegmentSize,
                        segmentLocations[0].y * mSegmentSize,
                        paint)
                }
                Heading.LEFT -> {
                    canvas.drawBitmap(mBitmapHeadLeft,
                        segmentLocations[0].x * mSegmentSize,
                        segmentLocations[0].y * mSegmentSize,
                        paint)
                }
                Heading.UP ->{
                    canvas.drawBitmap(mBitmapHeadUp,
                        segmentLocations[0].x * mSegmentSize,
                        segmentLocations[0].y * mSegmentSize,
                        paint)
                }
                Heading.DOWN -> {
                    canvas.drawBitmap(mBitmapHeadDown,
                        segmentLocations[0].x * mSegmentSize,
                        segmentLocations[0].y * mSegmentSize,
                        paint)
                }
            }
            for(i in 1 until segmentLocations.size){
                canvas.drawBitmap(mBitmapBody,
                    segmentLocations[i].x * mSegmentSize,
                    segmentLocations[i].y * mSegmentSize,
                    paint)
            }
        }
    }

    fun switchHeading(event : MotionEvent){
        heading = if(event.x >= halfWayPoint){
            when(heading){
                Heading.UP -> Heading.RIGHT
                Heading.RIGHT -> Heading.DOWN
                Heading.DOWN -> Heading.LEFT
                Heading.LEFT -> Heading.UP
            }
        }else{
            when(heading){
                Heading.UP -> Heading.LEFT
                Heading.RIGHT -> Heading.UP
                Heading.DOWN -> Heading.RIGHT
                Heading.LEFT -> Heading.DOWN
            }
        }
    }

}