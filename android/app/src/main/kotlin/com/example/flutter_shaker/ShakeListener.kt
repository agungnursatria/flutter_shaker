package com.example.flutter_shaker

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log


open class ShakeListener(mContext: Context) : SensorEventListener {

    private val SHAKE_THRESHOLD_GRAVITY = 2.7f
    private val SHAKE_SLOP_TIME_MS = 500
    private val SHAKE_COUNT_RESET_TIME_MS = 3000
    private var mListener: OnShakeListener? = null
    private var mShakeTimestamp: Long = 0
    private var mShakeCount: Int = 0

    private var mSensorMgr: SensorManager? = null
    var accSensor: Sensor? = null
    var magnetSensor: Sensor? = null

    var supportedAccSensor = false
//    var supportedMagnetSensor = false

    init {
        mSensorMgr = mContext.getSystemService(Context.SENSOR_SERVICE) as SensorManager

        mSensorMgr?.let {
            accSensor = it.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
            magnetSensor = it.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
        } ?: throw UnsupportedOperationException("Sensors not supported")
    }

    fun setOnShakeListener(listener: OnShakeListener) {
        this.mListener = listener
    }

    interface OnShakeListener {
        fun onShake(count: Int)
    }

    fun resume() {
        supportedAccSensor = mSensorMgr!!.registerListener(this, accSensor, SensorManager.SENSOR_DELAY_GAME)
        if (!supportedAccSensor) {
            mSensorMgr!!.unregisterListener(this, accSensor)
            throw UnsupportedOperationException("Accelerometer not supported")
        }

//        supportedMagnetSensor = mSensorMgr!!.registerListener(this, magnetSensor, SensorManager.SENSOR_DELAY_GAME)
//        if (!supportedMagnetSensor) {
//            mSensorMgr!!.unregisterListener(this, magnetSensor)
//            throw UnsupportedOperationException("Magnetic Field not supported")
//        }
    }

    fun onPause(){
        if (supportedAccSensor) {
            mSensorMgr!!.unregisterListener(this, accSensor)
        }

//        if (supportedMagnetSensor) {
//            mSensorMgr!!.unregisterListener(this, magnetSensor)
//        }
    }


    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    override fun onSensorChanged(event: SensorEvent?) {

        if (mListener != null && event != null) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]

            val gX = x / SensorManager.GRAVITY_EARTH
            val gY = y / SensorManager.GRAVITY_EARTH
            val gZ = z / SensorManager.GRAVITY_EARTH

            // gForce will be close to 1 when there is no movement.
            val gForce = Math.sqrt((gX * gX + gY * gY + gZ * gZ).toDouble()).toFloat()

            if (gForce > SHAKE_THRESHOLD_GRAVITY) {
                val now = System.currentTimeMillis()
                // ignore shake events too close to each other (500ms)
                if (mShakeTimestamp + SHAKE_SLOP_TIME_MS > now) {
                    return
                }

                // reset the shake count after 3 seconds of no shakes
                if (mShakeTimestamp + SHAKE_COUNT_RESET_TIME_MS < now) {
                    mShakeCount = 0
                }

                mShakeTimestamp = now
                mShakeCount++

                mListener!!.onShake(mShakeCount)
            }
        }
    }

}