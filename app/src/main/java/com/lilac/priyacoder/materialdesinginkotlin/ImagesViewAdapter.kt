package com.lilac.priyacoder.materialdesinginkotlin

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.image_buckets.view.*
import java.io.File

/**
 * Created by 1021422 on 10/16/2017.
 */
class ImagesViewAdapter(private var context: Context, var listOfFiles:List<File>?) : RecyclerView.Adapter<ImagesViewAdapter.ViewHolder>() {

    lateinit var itemClickListener: OnItemClickListener
    lateinit var listOfMonth: MutableList<String>
    // 1
    override fun getItemCount() = listOfFiles?.size!!

    // 2
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row_photo, parent, false)
        return ViewHolder(itemView)
    }

    // 3
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val imageFile: File = listOfFiles!![position]
        Picasso.with(context).load(imageFile).fit().centerCrop().into(holder.itemView.placeImage)

//        val options = BitmapFactory.Options()
//        options.inPreferredConfig = Bitmap.Config.ARGB_8888
//        val bitmap = BitmapFactory.decodeFile(imageFile.absolutePath, options)

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

        override fun onClick(view: View) = itemClickListener.onItemClick(itemView, listOfFiles!![adapterPosition])
    }

    interface OnItemClickListener {
        fun onItemClick(view: View, file: File)
    }

    fun setOnItemClickListener(itemClickListener: OnItemClickListener) {
        this.itemClickListener = itemClickListener
    }
}