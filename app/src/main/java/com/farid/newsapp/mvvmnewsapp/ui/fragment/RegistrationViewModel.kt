package com.farid.newsapp.mvvmnewsapp.ui.fragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.farid.newsapp.mvvmnewsapp.models.User
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase


class RegistrationViewModel:ViewModel() {
    private val mAuth = FirebaseAuth.getInstance()

    private var liveData=MutableLiveData<Boolean>()
    var isSuccess:LiveData<Boolean> = liveData
    fun login(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(OnCompleteListener<AuthResult?> { task ->
                if (task.isSuccessful) {
                    val user: FirebaseUser = mAuth.getCurrentUser()!!
                    Log.i("TAG", "signWithEmail: user success$user")
                    Log.i("TAG", "signWithEmail: user.getDisplayName " + user.displayName)
                    Log.i("TAG", "signWithEmail: user.getEmail " + user.email)
                    liveData.postValue(true)
                } else {
                    Log.i("TAG", "signWithEmail: Exception" + task.exception)
                    liveData.postValue(false)
                }
            }).addOnFailureListener(OnFailureListener { e ->
                Log.i("TAG", "signWithEmail addOnFailureListener" + e.message)
                liveData.postValue(false)
            })
    }

    fun createUserWithEmailAndPassword(email: String,password: String) {

        Log.i("TAG", "createUserWithEmailAndPassword: email--------------> $email")
        Log.i("TAG", "createUserWithEmailAndPassword: password-----------> $password")
        mAuth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener(OnCompleteListener<AuthResult?> { task ->
                if (task.isSuccessful) {
                    Log.i(
                        "TAG",
                        "signingButton for if first one task.isSuccessful()  " + task.isSuccessful
                    )
                    FirebaseDatabase.getInstance().getReference("Users").child(
                        FirebaseAuth.getInstance().currentUser!!.uid
                    )
                        .setValue(User(email,password)).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.i(
                                    "TAG",
                                    "signingButton.setOnClickListener: RegisterActivity going to MainActivity "
                                )
                                liveData.postValue(true)
                            } else {
                                Log.i(
                                    "TAG",
                                    "signingButton for else Authentication " + task.isSuccessful
                                )
                                liveData.postValue(false)
                            }
                        }
                } else {
                    liveData.postValue(false)
                    Log.i("TAG", "signingButton for addOnCompleteListener  " + task.exception)
                }
            })
    }
}