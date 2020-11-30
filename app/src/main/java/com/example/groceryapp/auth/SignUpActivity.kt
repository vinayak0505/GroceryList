package com.example.groceryapp.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.groceryapp.MainActivity2
import com.example.groceryapp.R
import com.example.groceryapp.databinding.ActivitySignUpBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase


class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        auth = Firebase.auth
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)
        binding.signUpButton.setOnClickListener { signIn() }
        binding.signUpToLoginIn.setOnClickListener {
            startActivity(Intent(this, MainActivity2::class.java))
            finish()
        }
    }

    private fun signIn(){
        auth.createUserWithEmailAndPassword(
            binding.signUpEmail.text.toString(),
            binding.signUpPassword.text.toString()
        )
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val profileUpdates = userProfileChangeRequest {
                        displayName = binding.signUpName.text.toString()
                    }
                    auth.currentUser?.updateProfile(profileUpdates)
                        ?.addOnCompleteListener(OnCompleteListener<Void?> { task ->
                            if (task.isSuccessful) {
                                startActivity(Intent(this, MainActivity2::class.java))
                                finish()
                            }
                        })

                } else {
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

}