package com.wireless.spyapp.util

import android.util.Log
import java.io.File
import java.io.RandomAccessFile

class FileManager {
//    private fun writeData() {
//        val filePath = "/Android/data/com.wireless.spyapp/"
//        val fileName = "data.txt"
//        writeTxtToFile("Wx:lcti1314", filePath, fileName)
//    }

    // 将字符串写入到文本文件中
    fun writeTxtToFile(strcontent: String, filePath: String, fileName: String) {
        //生成文件夹之后，再生成文件，不然会出错
        makeFilePath(filePath, fileName)
        val strFilePath = filePath + fileName
        // 每次写入时，都换行写
        val strContent = """
         $strcontent

         """.trimIndent()
        try {
            val file = File(strFilePath)
            if (!file.exists()) {
                Log.d("TestFile", "Create the file:$strFilePath")
                file.getParentFile()?.mkdirs()
                file.createNewFile()
            }
            val raf = RandomAccessFile(file, "rwd")
            raf.seek(file.length())
            raf.write(strContent.toByteArray())
            raf.close()
        } catch (e: Exception) {
            Log.e("TestFile", "Error on write File:$e")
        }
    }

    //生成文件
    fun makeFilePath(filePath: String, fileName: String): File? {
        var file: File? = null
        makeRootDirectory(filePath)
        try {
            file = File(filePath + fileName)
            if (!file.exists()) {
                file.createNewFile()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return file
    }

    //生成文件夹
    fun makeRootDirectory(filePath: String) {
        var file: File? = null
        try {
            file = File(filePath)
            if (!file.exists()) {
                file.mkdir()
            }
        } catch (e: Exception) {
            Log.i("error:", e.toString() + "")
        }
    }

}