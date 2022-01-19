package com.example.filemanager

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import java.io.File
import java.io.IOException

class FileOpener {
    @Throws(IOException::class)
    fun openFile(context: Context, file: File) {
       var selectedFile=file
        var uri= FileProvider.getUriForFile(context,context.applicationContext.packageName + ".provider",file)
   var intent= Intent(Intent.ACTION_VIEW)
        if(uri.toString().contains(".pdf")){
            intent.setDataAndType(uri,"application/pdf")
        }else if(uri.toString().contains(".doc") || uri.toString().contains(".docx")){
            intent.setDataAndType(uri,"application/msword")
        }else if(uri.toString().contains(".ppt") || uri.toString().contains(".pptx")|| uri.toString().contains(".PPT")){
            intent.setDataAndType(uri,"application/vnd.ms-powerpoint")
        }else{
            intent.setDataAndType(uri,"*/*")
        }
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        context.startActivity(intent)

    }
}