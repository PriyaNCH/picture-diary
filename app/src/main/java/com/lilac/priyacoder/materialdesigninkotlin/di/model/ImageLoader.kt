package com.lilac.priyacoder.materialdesigninkotlin.di.model

import android.content.Context
import android.widget.ImageView
import com.squareup.picasso.Picasso
import java.io.File

class ImageLoader(private var context: Context){

    fun loadToImageView(imageFile: File,imageView: ImageView){
        Picasso.with(context).load(imageFile).fit().centerCrop().into(imageView)
    }
    fun loadToImageView(imageFile : File,imageView: ImageView,targetWidth: Int, targetHeight: Int){
        Picasso.with(context).load(imageFile).resize(targetWidth,targetHeight).centerCrop().into(imageView)
    }
}