package com.example.tp4_1

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.Toast
import androidx.core.content.edit
import com.example.tp4_1.databinding.ActivityLoginBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.io.IOException



class LoginActivity : AppCompatActivity() {

    private lateinit var bindinglogin : ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bindinglogin = ActivityLoginBinding.inflate(layoutInflater)
        val view = bindinglogin.root
        setContentView(view)


        var isPasswordVisible = false
        bindinglogin.eye.setOnClickListener(){
            if (isPasswordVisible) {
                bindinglogin.pwd.transformationMethod = PasswordTransformationMethod.getInstance()
                bindinglogin.eye.setImageResource(R.drawable.eye_slash)
            } else {
                bindinglogin.pwd.transformationMethod = null
                bindinglogin.eye.setImageResource(R.drawable.eye)
            }
            isPasswordVisible = !isPasswordVisible
            bindinglogin.pwd.setSelection(bindinglogin.pwd.text.length)
        }

        bindinglogin.gotosignup.setOnClickListener(){
            val intent = Intent(this,RegisterActivity::class.java)
            this.startActivity(intent)
        }

        bindinglogin.forgetpwd.setOnClickListener(){

        }

        bindinglogin.signin.setOnClickListener {
            val tel = bindinglogin.tel.text.toString()
            val password = bindinglogin.pwd.text.toString()
            if (tel.length == 0) {
                bindinglogin.tel.setError("Entrer votre numéro de télephone")
            }
            else if (password.length == 0) {
                bindinglogin.pwd.setError("Entrer le mot de passe")
            }
            else {
                try {
                    if (!connected())
                        throw Exception("Vérifier votre connexion internet")
                    else{
                        bindinglogin.progressBar.visibility = View.VISIBLE
                        login(tel, password)
                    }
                }
                catch (_: InterruptedException) {
                } catch (_: IOException) {
                } catch (_: NullPointerException) {
                } catch (_: IllegalStateException) {
                } catch (_: Exception) { }
            }
        }
    }

    private fun connected (): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true
        return isConnected
    }
    private fun login(tel: String, password: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = RetrofitService.endpoint.verifyUser(tel, password)
            withContext(Dispatchers.Main) {
                bindinglogin.progressBar.visibility = View.INVISIBLE
                if (response.isSuccessful) {
                    val user = response.body() // mettre adapter a ce niveau car c adapter qui contient les donnees
                    if (user != null) {
                        val pref = getSharedPreferences("fileConnexion", MODE_PRIVATE)
                        pref.edit {
                            user.id?.let { putInt("idUser", it) }
                            putBoolean("connected", true)
                            putString("nom" , user.nom)
                            putString("tel" ,user.tel)
                            if (user.birthdate.isNullOrBlank())
                            {

                            }else{
                                putString("anniv" , user.birthdate.toString())
                            }
                            putString("numcarte" , user.creditcart)
                        }


                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(
                            this@LoginActivity,
                            "numero de tel ou mot de passe non valide",
                            Toast.LENGTH_SHORT
                        ).show();
                    }

                }
                else {
                    throw Exception("Unexpected code $response")
                }
            }

        }
    }


}

