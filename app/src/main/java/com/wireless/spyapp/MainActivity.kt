package com.wireless.spyapp

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.wireless.spyapp.imu.Data
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList


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

    private var timer: Timer? = null

    private var startTime : Long = 0
    private var currentTime : Long = 0

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

            if (event.sensor.type === Sensor.TYPE_ACCELEROMETER) {
                mAccelerometerReading = event.values
//                Log.d(
//                    TAG,
//                    "accelerometer data[x:" + event.values[0] + ", y:" + event.values[1] + ", z:" + event.values[2] + "]"
//                )
                mAccelerometerSensorTextView?.setText("[x:" + event.values[0] + ", y:" + event.values[1] + ", z:" + event.values[2] + "]")
                mAccelerometerValues?.add(Data(event.values[0], event.values[1], event.values[2]))
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

            Log.d(TAG, "-----------------------------------------")
            for (it in mAccelerometerValues!!){
                Log.d(TAG, it.id.toString() + " accelerometer: [x:" + it.x + ", y:" + it.y + ", z:" + it.z + "]")
            }
            mAccelerometerValues?.clear()
            Log.d(TAG, "-----------------------------------------")

        }
    }
}

//class MainActivity : AppCompatActivity(), SensorEventListener {
//
//    private lateinit var appBarConfiguration: AppBarConfiguration
//    private lateinit var binding: ActivityMainBinding
//    private var mLastTimestamp: Long = 0
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        checkPermission()
//        writeData()
//        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        setSupportActionBar(binding.toolbar)
//
//        val navController = findNavController(R.id.nav_host_fragment_content_main)
//        appBarConfiguration = AppBarConfiguration(navController.graph)
//        setupActionBarWithNavController(navController, appBarConfiguration)
//
//        // 获取TYPE_ACCELEROMETER对应的对象
//        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
//        val sensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
//
//        // 注册监听器
//        sensorManager.registerListener(this, sensorAccelerometer, SensorManager.SENSOR_DELAY_FASTEST)
//
//        // 获取TYPE_GRAVITY对应的对象
//        val sensorGravity = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY)
//
//        // 注册监听器
//        sensorManager.registerListener(this, sensorAccelerometer, SensorManager.SENSOR_DELAY_FASTEST)
//
//        binding.fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace Successfully 100", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
//        }
//    }
//
//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.menu_main, menu)
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        return when (item.itemId) {
//            R.id.action_settings -> true
//            else -> super.onOptionsItemSelected(item)
//        }
//    }
//
//    override fun onSupportNavigateUp(): Boolean {
//        val navController = findNavController(R.id.nav_host_fragment_content_main)
//        return navController.navigateUp(appBarConfiguration)
//                || super.onSupportNavigateUp()
//    }
//
//    override fun onSensorChanged(event: SensorEvent) {
//        if (mLastTimestamp == 0L) {
//            mLastTimestamp = event.timestamp
//            return
//        }
//
//        val rotateX = Math.abs(event.values[0])
//        val rotateY = Math.abs(event.values[1])
//        val rotateZ = Math.abs(event.values[2])
//
//        // log rotateX, rotateY, rotateZ
//        Log.d("accelerator", "rotateX: $rotateX, rotateY: $rotateY, rotateZ: $rotateZ")
//    }
//
//    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
//
//    private fun writeData() {
//        val filePath = "data/data/com.wireless.spyapp/"
//        val fileName = "data.txt"
//        writeTxtToFile("Wx:lcti1314", filePath, fileName)
//    }
//
//    // 将字符串写入到文本文件中
//    private fun writeTxtToFile(strcontent: String, filePath: String, fileName: String) {
//        //生成文件夹之后，再生成文件，不然会出错
//        makeFilePath(filePath, fileName)
//        val strFilePath = filePath + fileName
//        // 每次写入时，都换行写
//        val strContent = """
//         $strcontent
//
//         """.trimIndent()
//        try {
//            val file = File(strFilePath)
//            if (!file.exists()) {
//                Log.d("TestFile", "Create the file:$strFilePath")
//                file.getParentFile()?.mkdirs()
//                file.createNewFile()
//            }
//            val raf = RandomAccessFile(file, "rwd")
//            raf.seek(file.length())
//            raf.write(strContent.toByteArray())
//            raf.close()
//        } catch (e: Exception) {
//            Log.e("TestFile", "Error on write File:$e")
//        }
//    }
//
//    //生成文件
//    private fun makeFilePath(filePath: String, fileName: String): File? {
//        var file: File? = null
//        makeRootDirectory(filePath)
//        try {
//            file = File(filePath + fileName)
//            if (!file.exists()) {
//                file.createNewFile()
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//        return file
//    }
//
//    //生成文件夹
//    private fun makeRootDirectory(filePath: String) {
//        var file: File? = null
//        try {
//            file = File(filePath)
//            if (!file.exists()) {
//                file.mkdir()
//            }
//        } catch (e: Exception) {
//            Log.i("error:", e.toString() + "")
//        }
//    }
//
//    var permissions = arrayOf<String>(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//    var mPermissionList: MutableList<String> = ArrayList()
//
//    // private ImageView welcomeImg = null;
//    private val PERMISSION_REQUEST = 1
//    // 检查权限
//
//    // 检查权限
//    private fun checkPermission() {
//        mPermissionList.clear()
//
//        //判断哪些权限未授予
//        for (i in permissions.indices) {
//            if (ContextCompat.checkSelfPermission(this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
//                mPermissionList.add(permissions[i])
//            }
//        }
//        /**
//         * 判断是否为空
//         */
//        if (mPermissionList.isEmpty()) { //未授予的权限为空，表示都授予了
//        } else { //请求权限方法
//            val permissions = mPermissionList.toTypedArray() //将List转为数组
//            ActivityCompat.requestPermissions(this@MainActivity, permissions, PERMISSION_REQUEST)
//        }
//    }
//
//    /**
//     * 响应授权
//     * 这里不管用户是否拒绝，都进入首页，不再重复申请权限
//     */
//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        when (requestCode) {
//            PERMISSION_REQUEST -> {}
//            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        }
//    }
//
//}