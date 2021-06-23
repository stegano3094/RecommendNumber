package com.stegano.recommendnumber

import android.content.Context

class SaveData(val context: Context) {
    private val fileName = "LoadedLottoData.txt"

    fun saveToStorage(text: String) {
        val fileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE)
        fileOutputStream.write(text.toByteArray())
        fileOutputStream.close()
    }

    fun readToStorage() : String {
        val fileInputStream = context.openFileInput(fileName)
//        return fileInputStream.read().toString()
        return fileInputStream.reader().readText()
    }
}