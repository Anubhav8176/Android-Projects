package com.example.thesocialmediaapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import com.example.thesocialmediaapp.daos.UserDao
import com.example.thesocialmediaapp.models.User
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

@Suppress("DEPRECATION")
class SignInActivity : AppCompatActivity() {

    private val RC_SIGN_IN:Int = 9001
    private val TAG = "SignInActivity Tag"
    private lateinit var signInButton: SignInButton
    private lateinit var progressBar: ProgressBar
    private lateinit var googleSignInClient:GoogleSignInClient
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        signInButton = findViewById(R.id.signInButton)
        progressBar = findViewById(R.id.progressBar)
        auth = Firebase.auth

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        signInButton.setOnClickListener{
            signIn()
        }

    }

    override fun onStart() {
        super.onStart()
        updateUI(auth.currentUser)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account =
                completedTask.getResult(ApiException::class.java)!!
            Log.d(TAG, "firebaseAuthenticationWithGoogle "+account.id)
            firebaseAuthWithGoogle(account.idToken!!)

        }catch (e: ApiException){
            Log.w(TAG, "SignInResults:failed code "+e.statusCode)
        }

    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)

        signInButton.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
        GlobalScope.launch(Dispatchers.IO) {
            val auth = auth.signInWithCredential(credential).await()
            val firebaseUser = auth.user
            withContext(Dispatchers.Main) {
                updateUI(firebaseUser)
            }
        }

    }


    //For Updating the UI after the authentication.
    private fun updateUI(firebaseUser: FirebaseUser?) {
        if (firebaseUser!=null){

            val user = User(firebaseUser.uid, firebaseUser.displayName!!, firebaseUser.photoUrl.toString())
            val usersDao = UserDao()
            usersDao.addUser(user)


            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        else{
            signInButton.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
        }
    }
}