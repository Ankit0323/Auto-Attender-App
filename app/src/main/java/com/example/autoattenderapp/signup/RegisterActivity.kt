package com.example.autoattenderapp.signup

import android.app.Dialog
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.TextView
import com.example.autoattenderapp.R
import com.example.autoattenderapp.databinding.ActivityRegisterBinding
import com.example.autoattenderapp.firebase.myFirestore
import com.example.autoattenderapp.firebase.myUser
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase



class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var dialogBox: Dialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.arrow)

        binding.toolbar.setNavigationOnClickListener { onBackPressed() }

        binding.register.setOnClickListener {
            registerUser()
        }
    }

    private fun registerUser() {

        if(validRegisterDetails()){
            showProgressDialog("Please Wait...")
            val email:String=binding.email.text.toString().trim { it<= ' ' }
            val password:String=binding.password.text.toString().trim { it<= ' ' }
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->

                    if (task.isSuccessful) {
                        val firebaseUser: FirebaseUser =task.result!!.user!!

                        val user = myUser(firebaseUser.uid,
                            binding.firstName.text.toString(),
                            binding.email.text.toString(),
                            binding.lastName.text.toString()
                        )



                       myFirestore().registerUser(this@RegisterActivity,user)
                        registerationSuccess()
                        /* FirebaseAuth.getInstance().signOut()
                         Handler().postDelayed(Runnable {
                             finish()

                         },1200)*/

                        //val user = auth.currentUser
                        // updateUI(user)
                    } else {
                        dismissProgressDialog()
                        // If sign in fails, display a message to the user.

                        val snackbar= Snackbar.make(binding.register, "${task.exception!!.message.toString()}", Snackbar.LENGTH_LONG)
                            .setAction("Action", null)
                        val sbView: View = snackbar.view
                        sbView.setBackgroundColor(Color.RED)
                        snackbar.show()
                        //updateUI(null)
                    }
                }

        }

    }



    private fun validRegisterDetails():Boolean{
        when {
            binding.firstName.text.isNullOrEmpty()-> {
                val snackbar =
                    Snackbar.make(binding.firstName, "Please enter your name", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                val sbView: View = snackbar.view
                sbView.setBackgroundColor(Color.RED)
                snackbar.show()
                return  false

            }
            binding.lastName.text.isNullOrEmpty()-> {
                val snackbar =
                    Snackbar.make(binding.lastName, "Please enter your last name", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                val sbView: View = snackbar.view
                sbView.setBackgroundColor(Color.RED)
                snackbar.show()
                return false

            }
            binding.email.text.isNullOrEmpty()->{
                val snackbar =
                    Snackbar.make(binding.email, "Please enter your Email address", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                val sbView: View = snackbar.view
                sbView.setBackgroundColor(Color.RED)
                snackbar.show()
                return false
            }
            binding.password.text.isNullOrEmpty()-> {
                val snackbar =
                    Snackbar.make(binding.password, "Please enter your password", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                val sbView: View = snackbar.view
                sbView.setBackgroundColor(Color.RED)
                snackbar.show()
                return false

            }
            binding.confirmPassword.text.isNullOrEmpty()-> {
                val snackbar =
                    Snackbar.make(binding.confirmPassword, "Please confirm your password", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                val sbView: View = snackbar.view
                sbView.setBackgroundColor(Color.RED)
                snackbar.show()
                return false

            }
            !binding.confirmPassword.text.toString().equals( binding.password.text.toString())  -> {
                val snackbar =
                    Snackbar.make(binding.confirmPassword, "Please set your password correctly", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                val sbView: View = snackbar.view
                sbView.setBackgroundColor(Color.RED)
                snackbar.show()
                return false

            }

            !binding.check.isChecked-> {
                val snackbar =
                    Snackbar.make(binding.check, "Please tick the checkBox", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                val sbView: View = snackbar.view
                sbView.setBackgroundColor(Color.RED)
                snackbar.show()
                return false
            }
            else->{
                return  true

            }
        }
        return true
    }
    fun registerationSuccess(){
        dismissProgressDialog()
        val snackbar= Snackbar.make(binding.register, "You are registered successfully", Snackbar.LENGTH_LONG)
            .setAction("Action", null)
        val sbView: View = snackbar.view
        sbView.setBackgroundColor(Color.GREEN)
        snackbar.show()
        Handler().postDelayed(Runnable {
            finish()

        },1200)

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



}