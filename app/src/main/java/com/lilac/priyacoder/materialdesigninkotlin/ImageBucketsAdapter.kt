package com.lilac.priyacoder.materialdesigninkotlin

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v4.content.ContextCompat
import android.support.v7.graphics.Palette
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.image_buckets.view.*
import java.io.File
import java.util.function.Consumer
import kotlin.collections.HashMap


/**
 * Created by 1021422 on 10/10/2017.
 */
class ImageBucketsAdapter(private var context: Context,var imageData:HashMap<String,List<File>>)
    : RecyclerView.Adapter<ImageBucketsAdapter.ViewHolder>() {

    lateinit var itemClickListener: OnItemClickListener
    lateinit var listOfMonth: MutableList<String>
    // 1
    override fun getItemCount() = imageData.keys.size

    // 2
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.image_buckets, parent, false)
        return ViewHolder(itemView)
    }

    // 3
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val month = listOfMonth[position]
        val fileCount = imageData[month]!!.size
        holder.itemView.fileCount.text = fileCount.toString()
        holder.itemView.placeName.text = month

        val imageFile: File = imageData[month]!![0]
        Picasso.with(context).load(imageFile).fit().centerCrop().into(holder.itemView.placeImage)

//        val options = BitmapFactory.Options()
//        options.inPreferredConfig = Bitmap.Config.ARGB_8888
//        val bitmap = BitmapFactory.decodeFile(imageFile.absolutePath, options)
//
//        Palette.from(bitmap).generate { palette ->
//            val bgColor = palette.getMutedColor(ContextCompat.getColor(context, android.R.color.black))
//            holder.itemView.placeNameHolder.setBackgroundColor(bgColor)
//        }
    }

    // 2
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        init {
            itemView.placeHolder.setOnClickListener(this)
        }
        override fun onClick(view: View) = itemClickListener.onItemClick(itemView, imageData, listOfMonth[adapterPosition])
    }

    //Create an listener interface for the items in the RecyclerView
    interface OnItemClickListener {
        fun onItemClick(view: View, imageMap: HashMap<String,List<File>>, monthCode: String)
    }

    fun setOnItemClickListener(itemClickListener: OnItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    fun loadData() {
        listOfMonth =  mutableListOf()
        imageData.keys.forEach(Consumer { month ->
            run {
                listOfMonth.add(month)
            }
        })

        //sort list in the order of months starting with January ...
        listOfMonth.sortWith(MonthComparator)
    }
}