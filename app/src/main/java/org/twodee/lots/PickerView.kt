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

  // Task

  // Task

  // Task

  // Task

  // Task

  // Task

  // Task

  // Task
}
