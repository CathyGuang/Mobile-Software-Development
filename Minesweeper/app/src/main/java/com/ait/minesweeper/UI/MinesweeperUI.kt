package com.ait.minesweeper.UI

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.ait.minesweeper.MainActivity
import com.ait.minesweeper.Model.MinesweeperModel
import com.ait.minesweeper.Model.MinesweeperModel.FLAG
import com.ait.minesweeper.Model.MinesweeperModel.VISITED
import com.ait.minesweeper.R
import android.graphics.RectF
import android.util.Log
import com.ait.minesweeper.Model.MinesweeperModel.MINE


class MinesweeperUI(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    lateinit var paintBackGround: Paint
    lateinit var paintLine: Paint
    lateinit var paintLineCircle: Paint
    lateinit var paintLineCross: Paint
    lateinit var paintText: Paint
    lateinit var bitmapFlag: Bitmap
    lateinit var bitmapMine: Bitmap
    var endGame: Boolean

    init {

        bitmapFlag = BitmapFactory.decodeResource(
            context?.resources,
            R.drawable.flag
        )
        bitmapMine = BitmapFactory.decodeResource(
            context?.resources,
            R.drawable.mine
        )

        paintBackGround = Paint()
        paintBackGround.color = Color.BLACK
        paintBackGround.style = Paint.Style.FILL

        paintLine = Paint()
        paintLine.color = Color.GRAY
        paintLine.style = Paint.Style.STROKE
        paintLine.strokeWidth = 10f

        paintText = Paint()
        paintText.textSize = 100f
        paintText.color = Color.RED

        endGame = false

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paintBackGround)

        drawGameArea(canvas!!)

        drawGameProgress(canvas!!)

    }

    private fun drawGameArea(canvas: Canvas) {
        // border
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paintLine)
        // four horizontal lines and vertical lines
        for (i in 1..4){
            canvas.drawLine(
                0f, (i * height / 5).toFloat(), width.toFloat(), (i * height / 5).toFloat(),
                paintLine
            )
            canvas.drawLine(
                (i * width / 5).toFloat(), 0f, (i * width / 5).toFloat(), height.toFloat(),
                paintLine
            )
        }
    }

    private fun getMode(): Boolean {
        if ((context as MainActivity).isTryMode()){
            return true
        }
        return false
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)


        bitmapFlag = Bitmap.createScaledBitmap(
            bitmapFlag,
            width/5, height/5, false)

        bitmapMine = Bitmap.createScaledBitmap(
            bitmapMine,
            width/5, height/5, false)

    }

    private fun drawGameProgress(canvas: Canvas){
        Log.d("1,4", MinesweeperModel.getFieldContent(1, 4).toString())
        //For each square, draw already established flags or visited square hint number
        for (i in 0..4) {
            for (j in 0..4) {
                if (MinesweeperModel.getFieldContent(i, j) == FLAG) {
                    val X = (i * width / 5).toFloat()
                    val Y = (j * height / 5).toFloat()

                    canvas?.drawBitmap(bitmapFlag, X,Y, null)
                } else if (MinesweeperModel.getFieldContent(i, j) == VISITED) {
                    val num = MinesweeperModel.trackMines(i,j)
                    canvas.drawText(num.toString(),(i * width / 5).toFloat(),((j+1) * height / 5).toFloat(),paintText)
                }
                if (endGame){
                    if (MinesweeperModel.getFieldContent(i, j) == MINE) {
                        val X = (i * width / 5).toFloat()
                        val Y = (j * height / 5).toFloat()

                        canvas?.drawBitmap(bitmapMine, X,Y, null)

                    }
                }
            }
        }
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {

        if (event?.action == MotionEvent.ACTION_DOWN) {

            val tX = event.x.toInt() / (width / 5)
            val tY = event.y.toInt() / (height / 5)

            val isTry = getMode()

            if (tX < 5 && tY < 5 && MinesweeperModel.getFieldContent(tX, tY) ==
                MinesweeperModel.EMPTY) {
                if (isTry) {
                    MinesweeperModel.setFieldContent(tX, tY, VISITED)

                } else {
                    MinesweeperModel.setFieldContent(tX, tY, FLAG)
                    endGame = true
                    (context as MainActivity).showTextMessage(
                        context.getString(R.string.oops))
                }
            } else if (tX < 5 && tY < 5 && MinesweeperModel.getFieldContent(tX, tY) ==
                        MinesweeperModel.MINE){
                                Log.d("mine","$tX $tY")

                if (isTry) {
                    endGame = true
                    (context as MainActivity).showTextMessage(
                        context.getString(R.string.oops))
                } else {
                    MinesweeperModel.setFieldContent(tX, tY, FLAG)
                    if (checkWin()){
                        endGame = true
                        (context as MainActivity).showTextMessage(
                            context.getString(R.string.win))
                    }
                }

            }
            invalidate()
        }
        return true

    }

    private fun checkWin():Boolean {
        for (i in 0..4) {
            for (j in 0..4) {
                if (MinesweeperModel.getFieldContent(i, j) == MINE) {
                    return false
                }
            }
        }
        return true        
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val w = View.MeasureSpec.getSize(widthMeasureSpec)
        val h = View.MeasureSpec.getSize(heightMeasureSpec)
        val d = if (w == 0) h else if (h == 0) w else if (w < h) w else h
        setMeasuredDimension(d, d)
    }

    public fun resetGame() {
        MinesweeperModel.resetModel()

        (context as MainActivity).showTextMessage(
            context.getString(R.string.game_in_process))

        endGame = false
        invalidate()
    }
}
