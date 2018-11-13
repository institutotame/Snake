package com.atinem.snake

import android.graphics.Point

class Snake {

    private val segmentLocations : List<Point>

    private mSegmentSize : Int

    private mMoveRange : Point

    private halfWayPoint : Int

    private enum class Heading {
        UP, RIGHT, DOWN, LEFT
    }
}