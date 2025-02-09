package com.example.assignment2

import android.content.Intent
//Give access to my storage
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.assignment2.databinding.ActivitySignUpBinding

class SignUp : AppCompatActivity() {

    // We initialize the object for the ViewBinding class
    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize the variable to set the ViewBinding.
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCreateAccount.setOnClickListener{
            val username = binding.etUsername.text.toString()
            val password = binding.etPassword.text.toString()
            val confirmPassword = binding.etConfirmPassword.text.toString()

            //Text we get from the user are: "Editable" datatype
            // We convert them to strings for comparison later(compare this
            // value to a value in a database (string)

            //Check if passwords match

            if(password != confirmPassword){
                // If this password does not match the password saved it will print the error
                Toast.makeText(this, "Password do not match", Toast.LENGTH_SHORT).show()
            }else{
                val sharedPreferences: SharedPreferences = getSharedPreferences("UserPref", MODE_PRIVATE)
                val editor: SharedPreferences.Editor = sharedPreferences.edit()

                editor.putString("USERNAME:", username)
                editor.putString("PASSWORD:", password)
                editor.apply()

                Toast.makeText(this, "Account Created!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, GameMainPage::class.java)
                startActivity(intent)
            }
        }

        //setContentView(R.layout.activity_sign_up)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}