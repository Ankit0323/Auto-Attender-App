package com.example.autoattenderapp.firebase

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.autoattenderapp.R
import com.example.autoattenderapp.signup.RegisterActivity
import com.example.autoattenderapp.signup.loginActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

import com.google.firebase.firestore.SetOptions

class myFirestore {
    val firestore = FirebaseFirestore.getInstance()
    private lateinit var currUser: myUser




    /////////////////////////////////////////////////////////////////////////
    fun registerUser(activity: RegisterActivity, userInfo: myUser) {
        //this currUser is basically for below meme and news data
        currUser = userInfo
        firestore.collection(userCollection.USERS)
            .document(userInfo.id)
            .collection(userCollection.LOGIN_DATA).document(userInfo.id)
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.registerationSuccess()
            }
            .addOnFailureListener {
                activity.dismissProgressDialog()
                val snackbar = Snackbar.make(
                    activity.findViewById(R.id.register),
                    "Error while registering the user",
                    Snackbar.LENGTH_LONG
                )
                    .setAction("Action", null)
                val sbView: View = snackbar.view
                sbView.setBackgroundColor(Color.RED)
                snackbar.show()
            }
    }

    /////////////////////////////////////////////////////////////////////
    fun getCurrentUser(): String {
        val currentUser = FirebaseAuth.getInstance().currentUser
        var currentId = ""
        if (currentUser != null) {
            currentId = currentUser.uid
        }

        return currentId
    }

    fun getUserDetails(activity: Activity) {
        Log.i("p2","p");
        firestore.collection(userCollection.USERS).document(getCurrentUser())
            .collection(userCollection.LOGIN_DATA).document(getCurrentUser())
            .get()
            .addOnSuccessListener { document ->
                Log.i("p2","p")
                Log.i(activity.javaClass.simpleName, document.toString())
                val user = document.toObject(myUser::class.java)
                val sharedPreferences = activity.getSharedPreferences(
                    userCollection.SHARED_CONSTANT,
                    Context.MODE_PRIVATE
                )
                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                editor.putString(userCollection.LOGGED_IN_USER, "${user!!.firstName}")
                editor.apply()

                when (activity) {
                    is loginActivity -> activity.userLoggedInSuccess(user)
                }
            }
    }





   /* fun addMeme(meme: Meme) {
        meme.time=System.currentTimeMillis()
        meme.time!!.let {
            firestore.collection(userCollection.USERS).document(getCurrentUser())
                .collection(userCollection.MEME_DATA).document(it.toString())
                .set(meme, SetOptions.merge())
        }

    }

    fun deleteMeme(context: Context, meme: Meme){

        meme.time!!.let {
            firestore.collection(userCollection.USERS).document(getCurrentUser())
                .collection(userCollection.MEME_DATA).document(it.toString())
                .delete()
                .addOnSuccessListener {
                    Toast.makeText(context, "Meme removed from Bookmark", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Error in removing Meme from Bookmark", Toast.LENGTH_SHORT).show()
                }
        }
    }*/

}
///////////////////////////////////////////////////////////////////////////
