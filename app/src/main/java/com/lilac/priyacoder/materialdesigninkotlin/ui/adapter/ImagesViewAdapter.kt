package com.lilac.priyacoder.materialdesigninkotlin.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lilac.priyacoder.materialdesigninkotlin.R
import com.lilac.priyacoder.materialdesigninkotlin.di.model.ImageLoader
import kotlinx.android.synthetic.main.image_buckets.view.*
import java.io.File

/**
 * Created by Vishnu Priya Nallan on 10/16/2017.
 */
class ImagesViewAdapter(private var context: Context, var listOfFiles:List<File>?, imageviewLoader  : ImageLoader) : RecyclerView.Adapter<ImagesViewAdapter.ViewHolder>() {

    lateinit var itemClickListener: OnItemClickListener

    override fun getItemCount() = listOfFiles?.size!!

    private var imageLoader : ImageLoader = imageviewLoader

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row_photo, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val imageFile: File = listOfFiles!![position]
        imageLoader.loadToImageView(imageFile,holder.itemView.placeImage)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        init {
            itemView.placeHolder.setOnClickListener(this)
        }

        override fun onClick(view: View) = itemClickListener.onItemClick(itemView, listOfFiles!![adapterPosition])
    }

    interface OnItemClickListener {
        fun onItemClick(view: View, file: File)
    }

    fun setOnItemClickListener(itemClickListener: OnItemClickListener) {
        this.itemClickListener = itemClickListener
    }
}