package com.lilac.priyacoder.materialdesigninkotlin.utils

import android.graphics.BitmapFactory
import java.io.File

class ImageUtils{
    companion object {
        fun getFileDimensions(imageFile: File?): BitmapFactory.Options {

            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeFile(imageFile?.absolutePath, options)
            return options
        }
    }
}