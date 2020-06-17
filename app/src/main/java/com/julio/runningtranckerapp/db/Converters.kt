package com.julio.runningtranckerapp.db

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.TypeConverter
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.OutputStream

class Converters {

    //retorna un objeto de bitmap a partir de un array de bits
    @TypeConverter
    fun toBitmap(bytes: ByteArray) : Bitmap{
        return BitmapFactory.decodeByteArray(bytes,0,bytes.size)
    }

    //combierte un bitmap en un array de bytes para guardarlo en la bd
    @TypeConverter
    fun fromBitmap(bitmap: Bitmap) : ByteArray{
        val outputStream = ByteArrayOutputStream()

        bitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream)
        return outputStream.toByteArray()
    }
}