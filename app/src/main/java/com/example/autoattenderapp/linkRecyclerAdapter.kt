package com.example.autoattenderapp

import android.content.Context
import android.text.format.Formatter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.autoattenderapp.R
import com.example.autoattenderapp.roomForLinks.btm_data
import com.google.android.material.chip.Chip
import java.io.File
import java.util.*
import kotlin.collections.ArrayList


class linkRecyclerAdapter(val context: Context): RecyclerView.Adapter<linkRecyclerAdapter.viewHolder>() {
var list =ArrayList<btm_data>()

    inner class viewHolder(view: View): RecyclerView.ViewHolder(view){
       var time=view.findViewById<TextView>(R.id.time)
        var className=view.findViewById<TextView>(R.id.className)
        var link=view.findViewById<TextView>(R.id.link)
var duration=view.findViewById<TextView>(R.id.duration)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val myView = LayoutInflater.from(parent.context)
            .inflate(R.layout.btm_sheet_xml, parent, false)

        val myViewhld=viewHolder(myView)

        return myViewhld
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
               var file=list.get(position)
      holder.time.setText(file.startTime)
        holder.className.setText(file.className)
        holder.link.setText(file.link)
holder.duration.setText(file.timeDifference)


    }

    override fun getItemCount(): Int {
        return list.size
    }
    fun updateAll(files:ArrayList<btm_data>){
        list.clear()
        list.addAll(files)
        notifyDataSetChanged()
    }


interface myListener{

}






}