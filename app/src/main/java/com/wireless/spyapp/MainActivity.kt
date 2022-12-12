package com.wireless.spyapp

import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.wireless.spyapp.databinding.ActivityMainBinding
import com.wireless.spyapp.imu.Data
import com.wireless.spyapp.util.FileManager
import com.wireless.spyapp.util.MusicManager
import java.util.*


class MainActivity : AppCompatActivity() {
    private val TAG = "sensor-sample"
    private var mAccelerometerSensorTextView: TextView? = null
    private var mMagneticSensorTextView: TextView? = null
    private var mGyroscopeSensorTextView: TextView? = null
    private var mOrientationSensorTextView: TextView? = null
    private var mSensorManager: SensorManager? = null
    private var mMySensorEventListener: MySensorEventListener? = null
    private var mAccelerometerReading = FloatArray(3)
    private var mMagneticFieldReading = FloatArray(3)
    // an ArrayList of Floats to store the accelerometer values
    private var mAccelerometerValues: ArrayList<Data>? = null
    // an ArrayList of Floats to store the magnetic field values
    private var mMagneticFieldValues: ArrayList<Data>? = null
    // an ArrayList of Floats to store the gyroscope values
    private var mGyroscopeValues: ArrayList<Data>? = null
    // an ArrayList of Floats to store the orientation values
    private var mOrientationValues: ArrayList<Data>? = null

    private lateinit var binding: ActivityMainBinding

    private var timer: Timer? = null

    private var startTime : Long = 0
    private var currentTime : Long = 0

    private var fileManager: FileManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mAccelerometerSensorTextView = findViewById<TextView>(R.id.accelerometer_sensor)
        mMagneticSensorTextView = findViewById(R.id.magnetic_sensor)
        mGyroscopeSensorTextView = findViewById(R.id.gyroscope_sensor)
        mOrientationSensorTextView = findViewById(R.id.orientation_sensor)
        mSensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        mMySensorEventListener = MySensorEventListener()

        mAccelerometerValues = ArrayList()
        mMagneticFieldValues = ArrayList()
        mGyroscopeValues = ArrayList()
        mOrientationValues = ArrayList()

        fileManager = FileManager()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.fab.setOnClickListener {
            run {
                changeActivity()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (mSensorManager == null) {
            return
        }
        val accelerometerSensor = mSensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        if (accelerometerSensor != null) {
            //register accelerometer sensor listener
            mSensorManager!!.registerListener(
                mMySensorEventListener,
                accelerometerSensor,
                SensorManager.SENSOR_DELAY_FASTEST
            )
        } else {
            Log.d(TAG, "Accelerometer sensors are not supported on current devices.")
        }
        val magneticSensor = mSensorManager!!.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
        if (magneticSensor != null) {
            //register magnetic sensor listener
            mSensorManager!!.registerListener(mMySensorEventListener, magneticSensor, SensorManager.SENSOR_DELAY_FASTEST)
        } else {
            Log.d(TAG, "Magnetic sensors are not supported on current devices.")
        }
        val gyroscopeSensor = mSensorManager!!.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
        if (gyroscopeSensor != null) {
            //register gyroscope sensor listener
            mSensorManager!!.registerListener(mMySensorEventListener, gyroscopeSensor, SensorManager.SENSOR_DELAY_FASTEST)
        } else {
            Log.d(TAG, "Gyroscope sensors are not supported on current devices.")
        }
    }

    override fun onPause() {
        super.onPause()
        if (mSensorManager == null) {
            return
        }
        //unregister all listener
//        mSensorManager!!.unregisterListener(mMySensorEventListener)
    }

    /*
    This orientation sensor was deprecated in Android 2.2 (API level 8), and this sensor type was deprecated in Android 4.4W (API level 20).
    The sensor framework provides alternate methods for acquiring device orientation.
     */
    private fun calculateOrientation() {
        val rotationMatrix = FloatArray(9)
        SensorManager.getRotationMatrix(rotationMatrix, null, mAccelerometerReading, mMagneticFieldReading)
        val orientationAngles = FloatArray(3)
        SensorManager.getOrientation(rotationMatrix, orientationAngles)
//        Log.d(
//            TAG,
//            "orientation data[x:" + orientationAngles[0] + ", y:" + orientationAngles[1] + ", z:" + orientationAngles[2] + "]"
//        )
        mOrientationSensorTextView?.setText("[x:" + orientationAngles[0] + ", y:" + orientationAngles[1] + ", z:" + orientationAngles[2] + "]")
//        mOrientationValues?.add(Data(orientationAngles[0], orientationAngles[1], orientationAngles[2]))
    }

    private inner class MySensorEventListener : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            currentTime = System.currentTimeMillis()
            if (currentTime - startTime >= 10*1000){
                listData()
                startTime = System.currentTimeMillis()
            }

            // 没有播放的时候就不要记录
            if (MusicManager.isPause){
                listData()
                Log.d(TAG, "onSensorChanged: 没有播放的时候就不要记录")
                return
            }


            if (event.sensor.type === Sensor.TYPE_ACCELEROMETER) {
                mAccelerometerReading = event.values
//                Log.d(
//                    TAG,
//                    "accelerometer data[x:" + event.values[0] + ", y:" + event.values[1] + ", z:" + event.values[2] + "]"
//                )
                mAccelerometerSensorTextView?.setText("[x:" + event.values[0] + ", y:" + event.values[1] + ", z:" + event.values[2] + "]")
                mAccelerometerValues?.add(
                    Data(
                        System.nanoTime(),
                        event.values[0],
                        event.values[1],
                        event.values[2]
                    )
                )
            } else if (event.sensor.type === Sensor.TYPE_MAGNETIC_FIELD) {
                mMagneticFieldReading = event.values
//                Log.d(
//                    TAG,
//                    "magnetic data[x:" + event.values[0] + ", y:" + event.values[1] + ", z:" + event.values[2] + "]"
//                )
                mMagneticSensorTextView?.setText("[x:" + event.values[0] + ", y:" + event.values[1] + ", z:" + event.values[2] + "]")
//                mMagneticFieldValues?.add(Data(event.values[0], event.values[1], event.values[2]))
            } else if (event.sensor.type === Sensor.TYPE_GYROSCOPE) {
//                Log.d(
//                    TAG,
//                    "gyroscope data[x:" + event.values[0] + ", y:" + event.values[1] + ", z:" + event.values[2] + "]"
//                )
                mGyroscopeSensorTextView?.setText("[x:" + event.values[0] + ", y:" + event.values[1] + ", z:" + event.values[2] + "]")
//                mGyroscopeValues?.add(Data(event.values[0], event.values[1], event.values[2]))
            }
            calculateOrientation()
        }

        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
            Log.d(TAG, "onAccuracyChanged:" + sensor.type + "->" + accuracy)
        }

        private fun listData(){
            if (mAccelerometerValues != null && mAccelerometerValues!!.size == 0){
                return
            }

            Log.d(TAG, "-----------------------------------------")
            for (it in mAccelerometerValues!!){
                val data = it.x.toString() + "," + it.y.toString() + "," + it.z.toString()
//                Log.d("path", applicationContext.filesDir.toString())
                fileManager?.writeTxtToFile(data, applicationContext.filesDir.toString(), MusicManager.fileName)
//                Log.d(TAG, it.id.toString() + " accelerometer: [x:" + it.x + ", y:" + it.y + ", z:" + it.z + "]")

            }
            mAccelerometerValues?.clear()
            Log.d(TAG, "-----------------------------------------")

        }
    }

    private fun changeActivity(){
        val intent = Intent()
        intent.setClass(this@MainActivity, MusicActivity::class.java)
        startActivity(intent)
    }
}