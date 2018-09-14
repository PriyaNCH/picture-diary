package com.lilac.priyacoder.materialdesigninkotlin.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lilac.priyacoder.materialdesigninkotlin.R
import com.lilac.priyacoder.materialdesigninkotlin.di.model.ImageLoader
import com.lilac.priyacoder.materialdesigninkotlin.utils.MonthComparator
import kotlinx.android.synthetic.main.image_buckets.view.*
import java.io.File
import java.util.function.Consumer


/**
 * Created by Vishnu Priya Nallan on 10/10/2017.
 */
class ImageBucketsAdapter (private var context: Context, var imageData:HashMap<String,List<File>>, imageviewLoader : ImageLoader)
    : RecyclerView.Adapter<ImageBucketsAdapter.ViewHolder>() {

    lateinit var itemClickListener: OnItemClickListener
    lateinit var listOfMonth: MutableList<String>
    private var imageLoader : ImageLoader = imageviewLoader

    override fun getItemCount() = imageData.keys.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.image_buckets, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val month = listOfMonth[position]
        val fileCount = imageData[month]!!.size
        holder.itemView.fileCount.text = fileCount.toString()
        holder.itemView.placeName.text = month

        val imageFile: File = imageData[month]!![0]
        imageLoader.loadToImageView(imageFile,holder.itemView.placeImage)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        init {
            itemView.placeHolder.setOnClickListener(this)
        }
        override fun onClick(view: View) = itemClickListener.onItemClick(itemView, imageData, listOfMonth[adapterPosition])
    }

    //Create a listener interface for the items in the RecyclerView
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

        //sort list in the order of months starting with January
        listOfMonth.sortWith(MonthComparator)
    }
}