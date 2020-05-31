package com.burakusluer.foursquarekotlin.view

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.burakusluer.foursquarekotlin.R
import com.burakusluer.foursquarekotlin.functions.DefaultFunctions
import com.parse.ParseException
import com.parse.ParseUser

class LoginActivity : AppCompatActivity() {
    //region Init Area
    private fun init() {
        editTextName = findViewById(R.id.editTextLoginName)
        editTextPass = findViewById(R.id.editTextLoginPassword)
    }

    private lateinit var editTextName: EditText
    private lateinit var editTextPass: EditText

    //endregion
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        init()
        if(ParseUser.getCurrentUser()!=null){
            DefaultFunctions.ActivityTraveler(
                this@LoginActivity,
                LocationsActivity::class.java
            )
        }
    }

    fun buttonClick(view: View) {
        if (editTextName.text.isNotEmpty() && editTextPass.text.isNotEmpty()) {
            val parseUser = ParseUser()
            parseUser.username = editTextName.text.toString()
            parseUser.setPassword(editTextPass.text.toString())
            when (view.id) {
                R.id.buttonRegister -> {

                    parseUser.signUpInBackground {
                        if (it != null) {
                            it.printStackTrace()
                            Toast.makeText(
                                this@LoginActivity,
                                it.localizedMessage,
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            Toast.makeText(
                                this@LoginActivity,
                                "Your User Succesfully Created",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
                R.id.buttonLogin -> {
                    ParseUser.logInInBackground(
                        editTextName.text.toString(), editTextPass.text.toString()
                    ) { user: ParseUser?, parseException: ParseException? ->
                        if (parseException != null) {
                            parseException.printStackTrace()
                            Toast.makeText(
                                this@LoginActivity,
                                parseException.localizedMessage,
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            DefaultFunctions.ActivityTraveler(
                                this@LoginActivity,
                                LocationsActivity::class.java
                            )
                        }
                    }
                }
            }
        } else {
            Toast.makeText(this@LoginActivity, "Username Or Password Empty", Toast.LENGTH_LONG)
                .show()
        }
    }//buttonClick
}