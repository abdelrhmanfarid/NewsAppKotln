package com.farid.newsapp.mvvmnewsapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.farid.newsapp.R
import com.farid.newsapp.mvvmnewsapp.ui.fragment.RegistrationViewModel
import com.farid.newsapp.mvvmnewsapp.ui.fragment.SignInFragment
import com.google.firebase.auth.FirebaseAuth

class RegistrationActivity : AppCompatActivity() {

    lateinit var viewModel: RegistrationViewModel
    lateinit var auth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        supportFragmentManager.beginTransaction().replace(R.id.host,SignInFragment()).commit()

        viewModel = ViewModelProvider(this).get(RegistrationViewModel::class.java)

        auth = FirebaseAuth.getInstance()

    }

    override fun onStart() {
        super.onStart()

        if(auth.currentUser!=null){
            Toast.makeText(this,"welcome",Toast.LENGTH_SHORT).show()
            startActivity(Intent(this,NewsActivity::class.java))
        }else{
            Toast.makeText(this,"please login ",Toast.LENGTH_SHORT).show()

        }
    }
}