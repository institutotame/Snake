package com.atinem.snake

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Point

class Snake(context : Context, mr : Point, ss : Int) {

    private val segmentLocations = mutableListOf<Point>()

    private val mSegmentSize = ss

    private val mMoveRange = mr

    private val halfWayPoint = mr.x * ss /2

    private enum class Heading {
        UP, RIGHT, DOWN, LEFT
    }

    private val heading = Heading.RIGHT

    private val mBitmapHeadRight : Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.head)
    private val mBitmapHeadLeft : Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.head)
    private val mBitmapHeadUp : Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.head)
    private val mBitmapHeadDown : Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.head)

    private val mBitmapBody : Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.body)

}