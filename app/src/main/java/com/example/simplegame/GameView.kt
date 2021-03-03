package com.example.simplegame

import android.content.Context
import android.content.Intent
import android.graphics.*
import android.os.Handler
import android.view.MotionEvent
import android.view.View
import java.util.*

class GameView(context: Context?, w: Float, h: Float) : View(context) {
    var timer : Timer
    var timehandler : Handler
    private val width = w
    private val height = h
    private val pipeXLen = 128
    private val pipeYLen = 800

    private val pipe2XLen = 120
    private val pipe2YLen = 800
    private val birdYSize = 144
    private val birdXSize = 190

    private val bird = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources, R.drawable.birb), birdXSize, birdYSize, true)
    private val pipe = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources, R.drawable.pipe), pipeXLen, pipeYLen, true)
    private val pipe2 = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources, R.drawable.pipe2), pipe2XLen, pipe2YLen, true)

    private val paint = Paint()
    private val birdXSpeed = 10

    private var birdX = width/5
    private var birdY = height/2

    private var pipeX = width
    private var pipeY = 5*height/6 // 0, 0 is the top left !
    private var pipe2X = width
    private var pipe2Y = height/9

    private var frames = 0

    private var diff = 0
    private var gravity : Double = 0.0
    var score = 0

    init {
        timer = Timer()
        timehandler = Handler()
        val timetask = object : TimerTask() {
            override fun run() {
                timehandler.post { // async thread
                    invalidate()
                    if (checkCollision()) {
                        timer.cancel()

                        val i = Intent(context, NewScore::class.java)
                        i.putExtra("score", score)
                        context!!.startActivity(i)
                    }
                }
            }
        }
        startTimer(timetask)
    }

    private fun startTimer(task: TimerTask) {
        timer = Timer()
        timer.schedule(task, 1, 10)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawARGB(255, 0, 128, 128)
        canvas?.drawBitmap(bird, birdX - birdXSize / 2, birdY - birdYSize / 2, paint)
        // specifies top left of the entire sprite
        canvas?.drawBitmap(pipe, pipeX - pipeXLen / 2, pipeY - pipeYLen / 2, paint)
        canvas?.drawBitmap(pipe2, pipe2X - pipeXLen / 2, pipe2Y - pipeYLen / 2, paint)

        pipeX -= birdXSpeed
        pipe2X -= birdXSpeed
        gravity += 0.1
        birdY += (15 + gravity.toInt())
        if(frames > 0) {
            if (birdY > -20) { //some arbitrary ceiling
                birdY -= diff
            }
            gravity = 0.0
            diff -= 1
            frames -= 1
        }

        if (pipeX < 0) { //reset after player passes pipe
            pipeX = width
            pipe2X = width

            pipe2Y = height/(7..14).random()
            val rand = (4..7).random()
            pipeY = (rand - 1)*height/rand

            score += 1
        }

    }

    override fun onTouchEvent(e: MotionEvent): Boolean {
        when (e.actionMasked) {
            MotionEvent.ACTION_DOWN -> {}
            MotionEvent.ACTION_UP -> jump()
            MotionEvent.ACTION_CANCEL -> {}
        }
        return true
    }

    private fun jump() {
        frames = 40 //how long the jump lasts
        diff = 40 //size of jump
    }

    fun checkCollision(): Boolean {
        if ((birdY + birdYSize / 2 >= pipeY - pipeYLen / 2)
            && (birdY - birdYSize / 2 <= pipeY + pipeYLen / 2)
            && (birdX + birdXSize / 2 >= pipeX - pipeXLen / 2)
            && (birdX - birdXSize / 2 <= pipeX + pipeXLen / 2)) {
            return true
        }
        if ((birdY + birdYSize / 2 >= pipe2Y - pipeYLen / 2)
            && (birdY - birdYSize / 2 <= pipe2Y + pipeYLen / 2)
            && (birdX + birdXSize / 2 >= pipe2X - pipeXLen / 2)
            && (birdX - birdXSize / 2 <= pipe2X + pipeXLen / 2)) {
            return true
        }
        if (birdY >= height - 2) {
            return true
        }
        return false
    }

}