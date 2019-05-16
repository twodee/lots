package org.twodee.lots

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.drawable.Drawable
import android.preference.PreferenceManager
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class PickerView @JvmOverloads constructor(context: Context, attributes: AttributeSet? = null, style: Int = 0) : View(context, attributes, style) {
  private var pointerIdToPosition = HashMap<Int, PointF>()
  private var pickedId: Int = -1
  private var fingerprint: Drawable

  private val unpickedPaint: Paint
  private val pickedPaint: Paint
  private val prefs: SharedPreferences

  init {
    unpickedPaint = Paint().apply {
      strokeWidth = 10f
      color = Color.WHITE
      this.style = Paint.Style.STROKE
    }

    pickedPaint = Paint(unpickedPaint).apply {
      color = Color.MAGENTA
      strokeWidth = 20f
    }

    prefs = PreferenceManager.getDefaultSharedPreferences(context)
    fingerprint = resources.getDrawable(R.drawable.fingerprint, null)
  }

  // Task
  private val pickTask = Runnable {
    pickedId = pointerIdToPosition.keys.random()
  }

  // Task
  var onFirstDown: () -> Unit = {}

  // Task
  private fun handleDowns(event: MotionEvent) {
    if (event.actionMasked == MotionEvent.ACTION_DOWN) {
      onFirstDown()
    }

    when (event.actionMasked) {
      MotionEvent.ACTION_DOWN,
      MotionEvent.ACTION_POINTER_DOWN -> {
        val id = event.getPointerId(event.actionIndex)
        if (!pointerIdToPosition.containsKey(id)) {
          pointerIdToPosition[id] = PointF()
        }
        schedulePick()
      }
    }
  }

  // Task
  private fun handlePositions(event: MotionEvent) {
    repeat (event.pointerCount) { i ->
      id = event.getPointerId(i)
      pointerIdToPosition[id]?.apply {
        x = event.getX(i)
        y = event.getY(i)
      }
    }
  }

  // Task
  private fun handleUps(event: MotionEvent) {
    when (event.actionMasked) {
      MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP -> {
        pointerIdToPosition.remove(event.getPointerId(event.actionIndex))
        schedulePick()
      }
    }
  }

  // Task
  override fun onTouchEvent(event: MotionEvent): Boolean {
    handleDowns(event)
    handlePositions(event)
    handleUps(event)
    invalidate()
    return true
  }

  // Task
  private fun schedulePick() {
    pickedId = -1
    handler.removeCallbacks(pickTask)
    if (pointerIdToPosition.isNotEmpty()) {
      val delay = ((prefs.getString("pickDelay", null) ?: "3").toFloat() * 1000).toLong()
      handler.postDelayed(pickTask, delay)
    }
  }

  // Task
  override fun onDraw(canvas: Canvas) {
    if (pointerIdToPosition.isEmpty()) {
      drawFingerprint(canvas)
    } else {
      pointerIdToPosition.forEach { id, p ->
        canvas.drawCircle(p.x, p.y, 150f, if (id == pickedId) pickedPaint else unpickedPaint)
      }
    }
  }

  // Task
  private fun drawFingerprint(canvas: Canvas) {
    val imageAspect = fingerprint.intrinsicWidth / fingerprint.intrinsicHeight.toFloat()
    val frameAspect = width / height.toFloat()

    val boxWidth: Int
    val boxHeight: Int

    if (frameAspect < imageAspect) {
      boxWidth = width
      boxHeight = (width / imageAspect).toInt()
    } else {
      boxHeight = height
      boxWidth = (height * imageAspect).toInt()
    }

    val centerX = (width * 0.5f).toInt()
    val centerY = (height * 0.5f).toInt()

    fingerprint.setBounds(centerX - boxWidth / 2, centerY - boxHeight / 2, centerX + boxWidth / 2, centerY + boxHeight / 2)
    fingerprint.draw(canvas)
  }
}
