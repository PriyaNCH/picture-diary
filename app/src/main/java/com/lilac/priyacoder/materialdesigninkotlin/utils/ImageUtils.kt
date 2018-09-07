package com.lilac.priyacoder.materialdesigninkotlin.utils

import android.graphics.BitmapFactory
import java.io.File

class ImageUtils{
    companion object {
        //Pass an image file to get its dimensions
        fun getFileDimensions(imageFile: File?): BitmapFactory.Options {

            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeFile(imageFile?.absolutePath, options)
            return options
        }
    }
}