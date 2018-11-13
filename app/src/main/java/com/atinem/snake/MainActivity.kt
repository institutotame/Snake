package com.atinem.snake

import android.graphics.Point
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    var mSnakeGame : SnakeGame? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        mSnakeGame = SnakeGame(this,size)

        setContentView(mSnakeGame)
    }

    override fun onResume() {
        super.onResume()
        mSnakeGame?.resume()
    }

    override fun onPause() {
        super.onPause()
        mSnakeGame?.pause()
    }
}
