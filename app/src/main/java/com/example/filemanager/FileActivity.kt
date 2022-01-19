package com.example.filemanager

import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.autoattenderapp.R
import com.example.autoattenderapp.databinding.ActivityFileBinding
import com.example.autoattenderapp.room_database.meeting_data
import com.example.autoattenderapp.room_database.meeting_view_model
import com.example.filemanager.fileDatabase.file_data
import com.example.filemanager.fileDatabase.file_view_model
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class FileActivity : AppCompatActivity(), fileListRecyclerAdapter.myListener {
    var pdfFiles =ArrayList<File>()
    var docFiles =ArrayList<File>()
    var pptFiles =ArrayList<File>()
    var savePdfFiles=ArrayList<File>()
    var saveDocFiles=ArrayList<File>()
    var savePptFiles=ArrayList<File>()
    var date:String?=null
    var isThisSaveFolder=false
    var savedFilesList = java.util.ArrayList<file_data>()
    private lateinit var viewModel : file_view_model
    private lateinit var mAdapter:fileListRecyclerAdapter
    private lateinit var binding:ActivityFileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityFileBinding.inflate(layoutInflater)
         date=intent.getStringExtra("DATE")
//date=Date().toString()
        var layoutManager=LinearLayoutManager(this)

        binding.listRecycler.layoutManager=layoutManager
        mAdapter= fileListRecyclerAdapter(this, this)
binding.listRecycler.adapter=mAdapter

        viewModel= ViewModelProvider(this, ViewModelProvider
            .AndroidViewModelFactory.getInstance(application)).get(file_view_model::class.java)
        viewModel.allMeetings.observe(this, androidx.lifecycle.Observer {
            savedFilesList.addAll(it)
        })
        filePermission()

        binding.savefolder.setOnClickListener {
            isThisSaveFolder=true
            mAdapter.updateAll(savePdfFiles, R.drawable.pdf)
            binding.listRecycler.visibility=View.GONE
            binding.noFiles.visibility=View.GONE
            binding.shimmerViewContainer.visibility=View.VISIBLE
            binding.shimmerViewContainer.startShimmer()

            binding.pdf.visibility= View.VISIBLE
            binding.doc.visibility= View.VISIBLE
            binding.ppt.visibility= View.VISIBLE
            binding.pdf.setTextColor(getResources().getColor(R.color.white))
            binding.saveCardView.setCardBackgroundColor( getResources().getColor(R.color.light1))
            binding.pdfCardView.setCardBackgroundColor( getResources().getColor(R.color.white))
            binding.docAndPptCardView.setCardBackgroundColor( getResources().getColor(R.color.white))


            val formatter1 = SimpleDateFormat("yyyy-MM-dd")
            val date1 : Date = formatter1.parse(date)
            savePdfFiles.clear()
            saveDocFiles.clear()
            savePptFiles.clear()
            for(files in savedFilesList){
                val formatter2 = SimpleDateFormat("yyyy-MM-dd")
                val date2 : Date = formatter2.parse(files.date)
                var singleFile= File(files.filePath)
                if(date1.equals(date2)){

                    if(singleFile.name.toLowerCase().endsWith(".pdf")){

                        savePdfFiles.add(singleFile)
                    }else if(singleFile.name.toLowerCase().endsWith("doc") || singleFile.name.toLowerCase().endsWith(".docx")){

                        saveDocFiles.add(singleFile)
                    }else if(singleFile.name.toLowerCase().endsWith(".ppt") ||singleFile.name.toLowerCase().endsWith(".pptX") ){

                        savePptFiles.add(singleFile)
                    }
                }

            }

            Handler().postDelayed(Runnable {

                binding.shimmerViewContainer.stopShimmer()
                binding.shimmerViewContainer.visibility=View.GONE

                binding.listRecycler.visibility=View.VISIBLE
                pdfStore()
            }, 2000)

        }

        binding.pdffolder.setOnClickListener {
            isThisSaveFolder=false
            binding.pdf.visibility= View.VISIBLE
            binding.doc.visibility= View.GONE
            binding.ppt.visibility= View.GONE
            binding.pdf.setTextColor(getResources().getColor(R.color.white))
            binding.saveCardView.setCardBackgroundColor( getResources().getColor(R.color.white))
            binding.pdfCardView.setCardBackgroundColor( getResources().getColor(R.color.light1))
            binding.docAndPptCardView.setCardBackgroundColor( getResources().getColor(R.color.white))
            binding.noFiles.visibility=View.GONE
            pdfStore()

        }
        binding.docAndPptfolder.setOnClickListener {
            isThisSaveFolder=false
            binding.pdf.visibility= View.GONE
            binding.doc.visibility= View.VISIBLE
            binding.ppt.visibility= View.VISIBLE
            binding.doc.setTextColor(getResources().getColor(R.color.white))
            binding.saveCardView.setCardBackgroundColor( getResources().getColor(R.color.white))
            binding.pdfCardView.setCardBackgroundColor( getResources().getColor(R.color.white))
            binding.docAndPptCardView.setCardBackgroundColor( getResources().getColor(R.color.light1))
            binding.noFiles.visibility=View.GONE
            docStore()
        }
          binding.pdf.setOnClickListener {
             pdfStore()
              
          }
        binding.doc.setOnClickListener {
           docStore()

        }
        binding.ppt.setOnClickListener {
            pptStore()

        }
        setContentView(binding.root)
    }

    fun filePermission() {
        Dexter.withContext(this)
            .withPermissions(
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE

            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    if (report!!.areAllPermissionsGranted()) {
                        var internalStorage : String=System.getenv("EXTERNAL_STORAGE")
                        var storage=File(internalStorage)
                    //var st=File(storage.absolutePath)
                        listFile(storage)
                    }
                }

                override fun onPermissionRationaleShouldBeShown(permissions: List<PermissionRequest?>?, token: PermissionToken?) {
                    showRationale()
                    token?.cancelPermissionRequest()
                }
            }).check()
    }
    fun showRationale(){
        AlertDialog.Builder(this)
            .setMessage("You denied the permission for this Feature." +
                    "It can can be enabled under Settings")
            .setPositiveButton("GO TO SETTINGS"){
                    _,_->
                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)

                    val uri = Uri.fromParts("package", packageName, null)

                    intent.data = uri
                    startActivity(intent)

                }catch (e: ActivityNotFoundException){
                    e.printStackTrace()
                }
            }
            .setNegativeButton("CANCEL"){
                    Dialog,_->
                Dialog.dismiss()
            }.show()
    }
fun listFile(file:File){
    var finalList =ArrayList<File>()
    var files  =file.listFiles()
    for(singleFile in files){
         if(singleFile.isDirectory && !singleFile.isHidden){
             listFile(singleFile)
         }
    }
    for(singleFile in files){
        if(singleFile.name.toLowerCase().endsWith(".pdf") && !pdfFiles.contains(singleFile)){
            pdfFiles.add(singleFile)
        }else if((singleFile.name.toLowerCase().endsWith("doc") || singleFile.name.toLowerCase().endsWith(".docx"))&& !docFiles.contains(singleFile)){
            docFiles.add(singleFile)
        }else if((singleFile.name.toLowerCase().endsWith(".ppt") ||singleFile.name.toLowerCase().endsWith(".pptX"))&& !pptFiles.contains(singleFile) ){
            pptFiles.add(singleFile)
        }
    }
    pdfStore()
    Handler().postDelayed(Runnable {
        // finish()
        binding.shimmerViewContainer.stopShimmer()
        binding.shimmerViewContainer.visibility=View.GONE
        binding.noFiles.visibility=View.GONE
        binding.listRecycler.visibility=View.VISIBLE
    }, 5000)



}



    fun pdfStore(){
        binding.pdf.setTextColor(getResources().getColor(R.color.white))
        binding.doc.setTextColor(getResources().getColor(R.color.grey1))
        binding.ppt.setTextColor(getResources().getColor(R.color.grey1))
        if(isThisSaveFolder){
            mAdapter.updateAll(savePdfFiles, R.drawable.pdf)
            if(savePdfFiles.isEmpty()){

                    binding.noFiles.visibility=View.VISIBLE


            }else {
                binding.noFiles.visibility=View.GONE

            }
        }else {

            mAdapter.updateAll(pdfFiles, R.drawable.pdf)
        }
    }
    fun docStore(){
        binding.pdf.setTextColor(getResources().getColor(R.color.grey1))
        binding.doc.setTextColor(getResources().getColor(R.color.white))
        binding.ppt.setTextColor(getResources().getColor(R.color.grey1))
        if(isThisSaveFolder){
            mAdapter.updateAll(saveDocFiles, R.drawable.doc)
             if(saveDocFiles.isEmpty()){


                       binding.noFiles.visibility = View.VISIBLE


            }else {
                 binding.noFiles.visibility=View.GONE

             }
        }else {
            mAdapter.updateAll(docFiles, R.drawable.doc)
        }
    }
    fun pptStore(){
        binding.pdf.setTextColor(getResources().getColor(R.color.grey1))
        binding.doc.setTextColor(getResources().getColor(R.color.grey1))
        binding.ppt.setTextColor(getResources().getColor(R.color.white))
        if(isThisSaveFolder){
            mAdapter.updateAll(savePptFiles, R.drawable.ppt2)
             if(savePptFiles.isEmpty()){


                    binding.noFiles.visibility=View.VISIBLE

            }else {
                   binding.noFiles.visibility = View.GONE
               }

        }else {
            mAdapter.updateAll(pptFiles, R.drawable.ppt2)
        }
    }

    override fun fileClick(file: File) {
       FileOpener().openFile(this,file)
    }

    override fun toSaveFile(file: File) {
        Toast.makeText(this,"File saved",Toast.LENGTH_SHORT).show()
        viewModel.insertMeeting(file_data(file.absolutePath,date))
        savedFilesList.clear()
    }

}