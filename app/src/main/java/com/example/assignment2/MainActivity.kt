package com.example.assignment2

import android.content.Intent // Library for using Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.assignment2.databinding.ActivityMainBinding // Library for using ViewBinding

// Google Library
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.android.gms.common.api.ApiException

//Facebook Imports
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.GraphRequest
import com.facebook.login.LoginResult
import org.json.JSONException

/*
Group 5
* Katherine Hincapie
* Anthony Pastran
* Francisco Esteves
* Juan David Montana O
* */

// We create a variable that holds the object of googleSignInClient
private lateinit var googleSignInClient: GoogleSignInClient

// We create a variable that holds the object of FirebaseAuth,
// it is complementary for the Sign In Client google
private lateinit var auth: FirebaseAuth

// Constants for Facebook permissions
private const val EMAIL = "email"
private const val PUBLIC_PROFILE = "public_profile"
private val google_sign_in_code = 123

//Facebook Callback Manager
private lateinit var callbackManager: CallbackManager

class MainActivity : AppCompatActivity() {

    // We initialize the object for the ViewBinding class
    private lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initial values  for the created binding object:
        binding = ActivityMainBinding.inflate(layoutInflater)

        // We change from first connection to the ViewBinding connection
        setContentView(binding.root)

        // We initialize our variable auth that will be a copy of the class
        auth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()

        googleSignInClient =GoogleSignIn.getClient(this, gso)

        findViewById<Button>(R.id.btnGoogleSignIn).setOnClickListener {
            // Explicitly, we want to sign out the the user who signed in first, why? Because if we don't
            // do this Firebase would have kept that user signed in forever, so with this feature what we do
            // is cutting the process of the last user signed in each time we click the button.
            googleSignInClient.signOut().addOnCompleteListener{
                // if our signOut feature works, the following code would be executed
                if(it.isSuccessful){
                    val signInIntent = googleSignInClient.signInIntent
                    startActivityForResult(signInIntent, google_sign_in_code)
                }else{
                    println("Signed-out failed ${it.exception?.message}")
                }
            }
        }

        //Navigate to the sign up page, will allow to display the create account features.
        binding.btnSignUp.setOnClickListener{
            //Send an order to the sign up page to open
            // order = intent
            //to send intent: We create the intent then send

            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
        }

        //Initialize  Facebook callback manager
        callbackManager =  CallbackManager.Factory.create()

        binding.loginButton.setPermissions(EMAIL,PUBLIC_PROFILE)
        binding.loginButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult) {
                handleFacebookAccessToken(result.accessToken)
            }

            override fun onCancel(){
                Toast.makeText(this@MainActivity, "Facebook login canceled", Toast.LENGTH_SHORT).show()
            }

            override fun onError(error: FacebookException){
                Toast.makeText(this@MainActivity, "Facebook login failed ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun handleFacebookAccessToken(token: AccessToken){
        val request = GraphRequest.newMeRequest(token){obj, _ ->
            try{
                val name = obj?.getString("name")
                val email = obj?.getString("email")
                Toast.makeText(this, "Welcome $name, ($email)", Toast.LENGTH_SHORT).show()

                //Redirect to another activity after succesful login
                val intent = Intent(this, GameMainPage::class.java)
                startActivity(intent)
                finish()
            } catch (e: JSONException){
                e.printStackTrace()
            }
        }

        val parameters = Bundle()
        parameters.putString("fields", "id, name, email")
        request.parameters = parameters
        request.executeAsync()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        super.onActivityResult(requestCode,resultCode, data)

        if(requestCode == google_sign_in_code){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try{
                val account = task.getResult(ApiException::class.java)
                val idToken = account.idToken
                val credential = GoogleAuthProvider.getCredential(idToken, null)

                auth.signInWithCredential(credential).addOnCompleteListener {task ->
                    if(task.isSuccessful){
                        val user = auth.currentUser
                        println("Welcome ${user?.displayName}")

                        val intent = Intent(this, GameMainPage::class.java)
                        startActivity(intent)
                        finish()

                    } else {
                        println("Sign In Failed: ${task.exception?.message}")
                    }
                }
            }
            catch(e: ApiException){
                println("Google Sign-In Failed: ${task.exception?.message}")
            }
        }
    }
}