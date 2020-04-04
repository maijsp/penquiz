package com.example.penquiz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.fieldEmail
import kotlinx.android.synthetic.main.activity_login.fieldPassword
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() ,View.OnClickListener {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        auth = FirebaseAuth.getInstance()

        confirmRegister.setOnClickListener(this)
        cancelRegister.setOnClickListener(this)
//        val button = findViewById<Buttonsdfsd>(R.id.confirmRegister)
//        button.setOnClickListener {
//            createAccount()
//        }
    }

     override fun onClick(v: View) {
        val i = v.id
        when (i) {
            // R.id.emailCreateAccountButton -> createAccount(fieldEmail.text.toString(), fieldPassword.text.toString())
            // R.id.emailSignInButton -> signIn(fieldEmail.text.toString(), fieldPassword.text.toString())
            R.id.cancelRegister -> cancelRegistration(cancelRegister)
            R.id.confirmRegister -> createAccount(fieldEmail.text.toString(), fieldPassword.text.toString())
            //R.id.signOutButton -> signOut()
            //R.id.verifyEmailButton -> sendEmailVerification()
        }
    }

    private fun cancelRegistration(view: View) {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun validateForm(): Boolean {
        var valid = true

        val email = fieldEmail.text.toString()
        if (TextUtils.isEmpty(email)) {
            fieldEmail.error = "Required."
            valid = false
        } else {
            fieldEmail.error = null
        }

        val password = fieldPassword.text.toString()
        if (TextUtils.isEmpty(password)) {
            fieldPassword.error = "Required."
            valid = false
        } else {
            fieldPassword.error = null
        }
        return valid
    }

    private fun createAccount(email: String, password: String) {
        Log.d(TAG,"createAccount:$email")
        if (!validateForm()) {
            return
        }
        // showProgressBar()

        // [START create_user_with_email]
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success -> $email")
                    Toast.makeText(baseContext, "Success.", Toast.LENGTH_SHORT).show()
                    val user = auth.currentUser
                     //updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "User already exists in the system", Toast.LENGTH_SHORT).show()
                    //updateUI(null)
                }

                // [START_EXCLUDE]
                // [END_EXCLUDE]
            }
        // [END create_user_with_email]
    }
    companion object {
        private const val TAG = "EmailPasswordLogin"
    }
}
