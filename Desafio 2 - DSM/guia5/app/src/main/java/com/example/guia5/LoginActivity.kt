package com.example.guia5

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthCredential
import com.google.firebase.auth.OAuthProvider

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var callbackManager: CallbackManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Facebook callback
        callbackManager = CallbackManager.Factory.create()

        // Views
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLoginEmail = findViewById<Button>(R.id.btnLoginEmail)
        val btnLoginGithub = findViewById<Button>(R.id.btnLoginGithub)
        val btnLoginFacebook = findViewById<LoginButton>(R.id.btnLoginFacebook)
        val btnRegister = findViewById<Button>(R.id.btnRegister)

        // --------- LOGIN CON EMAIL ----------
        btnLoginEmail.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val pass = etPassword.text.toString().trim()

            if (email.isNotEmpty() && pass.isNotEmpty()) {
                auth.signInWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Login exitoso", Toast.LENGTH_SHORT).show()
                            goToHome()
                        } else {
                            Toast.makeText(
                                this,
                                "Error: ${task.exception?.localizedMessage}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Ingrese email y contraseña", Toast.LENGTH_SHORT).show()
            }
        }

        // --------- LOGIN CON FACEBOOK ----------
        btnLoginFacebook.setPermissions("email", "public_profile")
        btnLoginFacebook.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult) {
                handleFacebookAccessToken(result.accessToken)
            }

            override fun onCancel() {
                Toast.makeText(this@LoginActivity, "Login cancelado", Toast.LENGTH_SHORT).show()
            }

            override fun onError(error: FacebookException) {
                Toast.makeText(
                    this@LoginActivity,
                    "FB Error: ${error.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        })

        // --------- LOGIN CON GITHUB ----------
        btnLoginGithub.setOnClickListener {
            // Limpia sesión previa
            auth.signOut()

            val provider = OAuthProvider.newBuilder("github.com")
            provider.scopes = listOf("user:email")
            provider.addCustomParameter("allow_signup", "false")

            val pendingResultTask = auth.pendingAuthResult
            if (pendingResultTask != null) {
                pendingResultTask
                    .addOnSuccessListener { authResult ->
                        Toast.makeText(this, "Login GitHub exitoso", Toast.LENGTH_SHORT).show()
                        goToHome()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "GitHub Error: ${e.message}", Toast.LENGTH_LONG).show()
                    }
            } else {
                auth.startActivityForSignInWithProvider(this, provider.build())
                    .addOnSuccessListener { authResult ->
                        val credential = authResult.credential as? OAuthCredential
                        val accessToken = credential?.accessToken
                        val email = authResult.user?.email

                        if (email != null) {
                            Toast.makeText(this, "Login GitHub exitoso", Toast.LENGTH_SHORT).show()
                            goToHome()
                        } else {
                            Toast.makeText(
                                this,
                                "Error: usuario de GitHub no tiene correo público",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "GitHub Error: ${e.message}", Toast.LENGTH_LONG).show()
                    }
            }
        }

        // --------- REGISTRO CON EMAIL ----------
        btnRegister.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val pass = etPassword.text.toString().trim()

            if (email.isNotEmpty() && pass.isNotEmpty()) {
                if (pass.length >= 6) {
                    auth.createUserWithEmailAndPassword(email, pass)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(
                                    this,
                                    "Cuenta creada exitosamente",
                                    Toast.LENGTH_SHORT
                                ).show()
                                goToHome()
                            } else {
                                Toast.makeText(
                                    this,
                                    "Error al crear cuenta: ${task.exception?.localizedMessage}",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                } else {
                    Toast.makeText(
                        this,
                        "La contraseña debe tener al menos 6 caracteres",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(this, "Ingrese email y contraseña", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Manejo del resultado de Facebook
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        val credential = FacebookAuthProvider.getCredential(token.token)
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Login Facebook exitoso", Toast.LENGTH_SHORT).show()
                    goToHome()
                } else {
                    Toast.makeText(
                        this,
                        "FB Login Failed: ${task.exception?.localizedMessage}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }

    private fun goToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
