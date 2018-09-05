package com.lilac.priyacoder.materialdesigninkotlin.di.model

import android.content.Context
import android.widget.ImageView
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import java.io.File

class ImageLoader(private var context: Context){

    fun loadToImageView(imageFile: File,imageView: ImageView){
        Picasso.with(context).load(imageFile).fit().centerCrop().into(imageView)
    }
    fun loadToImageView(imageFile : File,target: Target,targetWidth: Int, targetHeight: Int){
        Picasso.with(context)
                .load(imageFile)
                .resize(targetWidth, targetHeight)
                .centerCrop()
                .into(target)
    }
}