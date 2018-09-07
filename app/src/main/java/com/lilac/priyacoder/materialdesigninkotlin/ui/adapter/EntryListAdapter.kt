package com.lilac.priyacoder.materialdesigninkotlin.ui.adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.daimajia.swipe.adapters.ArraySwipeAdapter
import com.lilac.priyacoder.materialdesigninkotlin.R
import com.lilac.priyacoder.materialdesigninkotlin.data.db.PhotoEntryDatabase
import com.lilac.priyacoder.materialdesigninkotlin.data.db.model.PhotoEntriesModel
import com.lilac.priyacoder.materialdesigninkotlin.ui.activity.DetailActivity
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.edit_entry_alert_dialog.view.*

class EntryListAdapter(context: Context?, var resource: Int, textViewResourceId : Int) : ArraySwipeAdapter<PhotoEntriesModel>(context, resource, textViewResourceId) {

    private var photoEntryObject : PhotoEntriesModel
    private var database : PhotoEntryDatabase

    init{
        photoEntryObject = PhotoEntriesModel(0, "", "")
        database = (context as DetailActivity).database
    }
    override fun getSwipeLayoutResourceId(position: Int): Int {
        return R.id.swipe
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val returnView : View
        val viewHolder: ArrayAdapterViewHolder?
        photoEntryObject = getItem(position) as PhotoEntriesModel

        if(convertView == null){
            returnView = LayoutInflater.from(context).inflate(R.layout.entries_listview,parent,false)

            viewHolder = ArrayAdapterViewHolder(returnView)

            returnView.tag = viewHolder
        } else {
            returnView = convertView
            viewHolder = returnView.tag as ArrayAdapterViewHolder
        }

        // Set values in the list view
        viewHolder.textData?.text = photoEntryObject.photoEntry

        returnView?.findViewById<View>(R.id.editEntry)?.setOnClickListener {
                showEditWindow(viewHolder.textData,parent, position) // Need to remove textData and in the function directly call textResourceId instead of entryview
        }
        returnView?.findViewById<View>(R.id.deleteEntry)?.setOnClickListener {
            deleteEntry(position)
        }
        return returnView
    }

    private fun showEditWindow(entryView : TextView?, viewGroup: ViewGroup?, clickedIndex : Int) {

        // Create an EditText to show the entry in full in
        val editAlertWindow = LayoutInflater.from(context).inflate(R.layout.edit_entry_alert_dialog,viewGroup,false)
        editAlertWindow.entryEditAlertView.setText(entryView?.text.toString())
        editAlertWindow.entryEditAlertView.setSelection(editAlertWindow.entryEditAlertView.text.length)

        // Construct the Alert Dialog to edit the entry
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Edit Entry")

        builder.setView(editAlertWindow).setMessage("Click Done to save or Cancel to cancel editing")

        builder.setPositiveButton("Done") { dialog, which ->
            dialog.dismiss()

            val editedText = editAlertWindow.entryEditAlertView.text

            if(editedText != null) {
                photoEntryObject = getItem(clickedIndex) as PhotoEntriesModel
                photoEntryObject.setEntry(editedText.toString())
                Single.fromCallable{
                    database?.photoEntryDao()?.update(photoEntryObject)}
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe()
                this.notifyDataSetChanged()
            } else{
                Toast.makeText(context,"Entry must be made before saving",Toast.LENGTH_SHORT).show()
            }
        }
        builder.setNegativeButton("Cancel") { dialog, which ->
            dialog.dismiss()
        }
        builder.show()
    }

    fun deleteEntry(clickedIndex : Int){
        photoEntryObject = getItem(clickedIndex) as PhotoEntriesModel
        Single.fromCallable{
            database.photoEntryDao().deletePhotoEntry(photoEntryObject)
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
        this.notifyDataSetChanged()
    }
}

private class ArrayAdapterViewHolder(view : View) {
    var textData : TextView? = null

    init{
        textData = view.findViewById(R.id.text_data)
    }
}