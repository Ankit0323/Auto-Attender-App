package com.example.filemanager

import android.content.Context
import android.text.format.Formatter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.autoattenderapp.R
import com.google.android.material.chip.Chip
import java.io.File
import java.util.*
import kotlin.collections.ArrayList


class fileListRecyclerAdapter(val context: Context,var listener:myListener): RecyclerView.Adapter<fileListRecyclerAdapter.viewHolder>() {
var list =ArrayList<File>()
    var image:Int=R.drawable.file
    inner class viewHolder(view: View): RecyclerView.ViewHolder(view){
       var image=view.findViewById<ImageView>(R.id.folderImage)
        var fileName=view.findViewById<TextView>(R.id.fileName)
        var fileSize=view.findViewById<TextView>(R.id.fileSize)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val myView = LayoutInflater.from(parent.context)
            .inflate(R.layout.file_list_xml, parent, false)

        val myViewhld=viewHolder(myView)
myView.setOnClickListener {
    listener.fileClick(list[myViewhld.adapterPosition])
}
        myView.setOnLongClickListener {
            listener.toSaveFile(list[myViewhld.adapterPosition])
          true
        }
        return myViewhld
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
               var file=list.get(position)
        holder.fileName.setText(file.name)
        holder.fileSize.setText(Formatter.formatShortFileSize(context,file.length()))
        holder.image.setImageResource(image)



    }

    override fun getItemCount(): Int {
        return list.size
    }
    fun updateAll(files:ArrayList<File>,images:Int){
        list.clear()
        image=images
        list.addAll(files)
        notifyDataSetChanged()
    }


interface myListener{
    fun fileClick(file:File)
    fun toSaveFile(file:File)
}






}