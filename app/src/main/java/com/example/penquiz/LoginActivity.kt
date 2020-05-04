package com.example.penquiz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.FirebaseAuth

import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() , View.OnClickListener{
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        // Buttons
        emailSignInButton.setOnClickListener(this)
        emailCreateAccountButton.setOnClickListener(this)

        // [START initialize_auth]
        auth = FirebaseAuth.getInstance()
        // [END initialize_auth]
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser

    }
    /*
    If users want to create the account, it will direct to another
     */
    private fun createAccount(view: View) {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
        finish()
    }

    /*
    If users want to sign in immediately
     */
    private fun signIn(email: String, password: String) {
        Log.d(TAG, "signIn:$email")
        if (!validateForm()) {
            return
        }
        // [START sign_in_with_email]
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, Go to MainActivity
                    Log.d(TAG, "signInWithEmail:success")
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                }

                // [START_EXCLUDE]
                if (!task.isSuccessful) {
                    status.setText(R.string.auth_failed)
                }
                // [END_EXCLUDE]
            }
//         [END sign_in_with_email]
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

    override fun onClick(v: View) {
        val i = v.id
        when (i) {
            // R.id.emailCreateAccountButton -> createAccount(fieldEmail.text.toString(), fieldPassword.text.toString())
            R.id.emailSignInButton -> signIn(fieldEmail.text.toString(), fieldPassword.text.toString())
            R.id.emailCreateAccountButton -> createAccount(emailCreateAccountButton)
        }
    }

    companion object {
        private const val TAG = "LoginActivity"
    }
}
