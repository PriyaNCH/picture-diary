package com.lilac.priyacoder.materialdesigninkotlin

import android.app.Activity
import android.content.Context

import com.daimajia.swipe.adapters.ArraySwipeAdapter

class EntryListAdapter(context: Context?, resource: Int, textViewResourceId : Int) : ArraySwipeAdapter<String>(context, resource, textViewResourceId) {


    override fun getSwipeLayoutResourceId(position: Int): Int {
        return R.id.swipe
    }
}