package com.example.autoattenderapp.signup

import android.app.Dialog
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.autoattenderapp.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth



class ForgotActivity : AppCompatActivity() {
    private lateinit var dialogBox: Dialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot)


        findViewById<Button>(R.id.submitbtn).setOnClickListener {
            val email: String =
                findViewById<EditText>(R.id.email).text.toString().trim { it <= ' ' }
            if (email.isNullOrEmpty()) {
                val snackbar =
                    Snackbar.make(
                        findViewById<EditText>(R.id.email),
                        "Please enter your Email address",
                        Snackbar.LENGTH_LONG
                    )
                        .setAction("Action", null)
                val sbView: View = snackbar.view
                sbView.setBackgroundColor(Color.RED)
                snackbar.show()
            } else {
                showProgressDialog("Please Wait...")
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        dismissProgressDialog()
                        if (task.isSuccessful) {
                            val snackbar =
                                Snackbar.make(
                                    findViewById<Button>(R.id.submitbtn),
                                    "Email sent successfully to reset your password",
                                    Snackbar.LENGTH_LONG
                                )
                                    .setAction("Action", null)
                            val sbView: View = snackbar.view
                            sbView.setBackgroundColor(Color.GREEN)
                            snackbar.show()
                            Handler().postDelayed(Runnable {
                                finish()

                            }, 1200)
                        } else {


                            Toast.makeText(
                                baseContext, "${task.exception!!.message.toString()}",
                                Toast.LENGTH_SHORT
                            ).show()

                        }
                    }
            }
        }
    }

    fun showProgressDialog(text :String){
        dialogBox= Dialog(this)
        dialogBox.setContentView(R.layout.start_progress_bar)
        dialogBox.setCancelable(false)
        dialogBox.findViewById<TextView>(R.id.progressText).setText(text)
        dialogBox.setCanceledOnTouchOutside(false)
        dialogBox.show()

    }
    fun dismissProgressDialog(){
        dialogBox.dismiss()
    }
    ;
//This can then be added to its parent layout


}