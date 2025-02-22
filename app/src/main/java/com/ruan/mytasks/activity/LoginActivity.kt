package com.ruan.mytasks.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ruan.mytasks.databinding.ActivityLoginBinding
import com.ruan.mytasks.extension.value
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        initComponents()
    }

    private fun initComponents() {
        binding.btLogin.setOnClickListener {
            login()
        }

        binding.btCreateAccount.setOnClickListener {
            createAccount()
        }

        //TODO: login com Google
        //Problemas ao gerar a chave SHA para o login com conta Google
    }

    private fun login() {
        Firebase.auth.signInWithEmailAndPassword(binding.etEmail.value(), binding.etPassword.value())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        finish()
                        startActivity(Intent(this, MainActivity::class.java))
                    } else {
                        Log.e("auth", "createUserWithEmail:failure", task.exception)

                        task.exception?.message?.let { errorMessage ->
                            binding.tilEmail.error = errorMessage
                        }

                        Toast.makeText(
                            baseContext,
                            "Authentication failed.",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }

    }

    private fun createAccount() {
        Firebase.auth.createUserWithEmailAndPassword(binding.etEmail.value(), binding.etPassword.value())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        login()
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.e("auth", "createUserWithEmail:failure", task.exception)

                        task.exception?.message?.let { errorMessage ->
                            binding.tilEmail.error = errorMessage
                        }

                        Toast.makeText(
                            baseContext,
                            "Authentication failed.",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }
    }
}