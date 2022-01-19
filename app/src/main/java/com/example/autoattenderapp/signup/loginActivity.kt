
package com.example.autoattenderapp.signup

import android.app.Dialog
import android.content.ContentValues
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.TextView
import com.example.autoattenderapp.MainActivity
import com.example.autoattenderapp.R
import com.example.autoattenderapp.databinding.ActivityLoginBinding
import com.example.autoattenderapp.firebase.myFirestore
import com.example.autoattenderapp.firebase.myUser
import com.example.autoattenderapp.firebase.userCollection
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class loginActivity :AppCompatActivity(), View.OnClickListener {
    private val RC_SIGN_IN: Int=123
    private lateinit var dialogBox: Dialog
    private lateinit var  firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityLoginBinding
    private lateinit var googleSignInClient: GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firestore= FirebaseFirestore.getInstance()
        auth = Firebase.auth
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
       googleSignInClient = GoogleSignIn.getClient(this, gso)
        binding.signInViaGoogle.setOnClickListener {

            signIn()
        }
        binding.register.setOnClickListener (this)
        binding.loginbtn.setOnClickListener (this)
        binding.forgotPassword.setOnClickListener (this)


    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser != null){
            updateUi(currentUser)
        }
    }
    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent

        startActivityForResult(signInIntent, RC_SIGN_IN)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignIn(task)

        }
    }

    private fun handleSignIn(myTask: Task<GoogleSignInAccount>?) {
        try {
            // Google Sign In was successful, authenticate with Firebase
            val account = myTask!!.getResult(ApiException::class.java)!!


             firebaseAuthWithGoogle(account.idToken!!)

        } catch (e: ApiException) {
            val snackbar = Snackbar.make(binding.signInViaGoogle, "${e.message.toString()}", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
            Log.i("myErrorcomes","${e.message.toString()}")
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        showProgressDialog("Please Wait...")
        val credential = GoogleAuthProvider.getCredential(idToken, null)

        GlobalScope.launch(Dispatchers.IO) {
            auth.signInWithCredential(credential).await()

            val firebaseUser=auth.currentUser
            val user= myUser(firebaseUser!!.uid!!,
                firebaseUser!!.displayName!!,
                firebaseUser!!.email!!
            )
           registerMyUser(user)
            withContext(Dispatchers.Main){
                updateUi(firebaseUser)
            }
       }
    }

    fun tempregisterMyUser(){
        dismissProgressDialog()
        val snackbar= Snackbar.make(binding.signInViaGoogle, "You are registered successfully", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
        val sbView: View = snackbar.view
        sbView.setBackgroundColor(Color.GREEN)
        snackbar.show()
    }
    fun registerMyUser(userInfo: myUser) {
        firestore.collection(userCollection.USERS)
            .document(userInfo.id)
            .collection(userCollection.LOGIN_DATA).document(userInfo.id)
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {
                dismissProgressDialog()

                val snackbar= Snackbar.make(binding.signInViaGoogle, "You are registered successfully", Snackbar.LENGTH_LONG)
                    .setAction("Action", null)
                val sbView: View = snackbar.view
                sbView.setBackgroundColor(Color.GREEN)
                snackbar.show()
            }
            .addOnFailureListener {
                dismissProgressDialog()
                val snackbar = Snackbar.make(binding.signInViaGoogle, "${it.message.toString()}", Snackbar.LENGTH_LONG)
                    .setAction("Action", null)
                val sbView: View = snackbar.view
                sbView.setBackgroundColor(Color.RED)
                snackbar.show()
            }
    }

    private fun updateUi(firebaseUser: FirebaseUser?) {
        if(firebaseUser!=null){

//            dismissProgressDialog()
           // tempregisterMyUser()

            Handler().postDelayed(Runnable {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            },1000)


        }
        else{
            //dismissProgressDialog()
            val snackbar= Snackbar.make(binding.register, "Incorrect Email or password ", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
            val sbView: View = snackbar.view
            sbView.setBackgroundColor(Color.RED)
            snackbar.show()


        }

    }


    private fun validRegisterDetails():Boolean{
        when {
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




            else->{
                return  true

            }
        }
        return true
    }
    private fun registerUser() {

        if(validRegisterDetails()){
            showProgressDialog("Please Wait...")
            val email:String=binding.email.text.toString().trim { it<= ' ' }
            val password:String=binding.password.text.toString().trim { it<= ' ' }
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    Log.i("p3","p")
                    if (task.isSuccessful) {
                        var user : myUser? =null // not useful
                       // userLoggedInSuccess(user)
                        myFirestore().getUserDetails(this@loginActivity)
                        Log.i("p34","p5")
                        //val user = auth.currentUser
                        // updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        dismissProgressDialog()
                        Log.w(ContentValues.TAG, "createUserWithEmail:failure", task.exception)
                        //Toast.makeText(baseContext, "${task.exception!!.message.toString()}",
                        // Toast.LENGTH_SHORT).show()
                        val snackbar= Snackbar.make(binding.register, "Incorrect Email or password ", Snackbar.LENGTH_LONG)
                            .setAction("Action", null)
                        val sbView: View = snackbar.view
                        sbView.setBackgroundColor(Color.RED)
                        snackbar.show()
                    }
                }

        }

    }
    fun showProgressDialog(text :String){
        dialogBox= Dialog(this@loginActivity)
        dialogBox.setContentView(R.layout.start_progress_bar)
        dialogBox.setCancelable(false)
        dialogBox.findViewById<TextView>(R.id.progressText).setText(text)
        dialogBox.setCanceledOnTouchOutside(false)
        dialogBox.show()

    }
    fun dismissProgressDialog(){
        if(dialogBox.isShowing) {
            dialogBox.dismiss()
        }
    }

    override fun onClick(view: View?) {
        if (view != null) {
            when (view.id) {
                R.id.forgotPassword->{
                    binding.email.setText("")
                    binding.password.setText("")
                    val intent= Intent(this,ForgotActivity::class.java)
                    startActivity(intent)
                }
                R.id.loginbtn->{
                    registerUser()
                }
                R.id.register-> {
                    val intent= Intent(this,RegisterActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    fun userLoggedInSuccess(user: myUser?) {
        dismissProgressDialog()
        val snackbar= Snackbar.make(binding.register, "You are logged in succesfully ", Snackbar.LENGTH_LONG)
            .setAction("Action", null)
        val sbView: View = snackbar.view
        sbView.setBackgroundColor(Color.GREEN)
        snackbar.show()
        Handler().postDelayed(Runnable {
            val intent= Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()

        },1200)
    }


}