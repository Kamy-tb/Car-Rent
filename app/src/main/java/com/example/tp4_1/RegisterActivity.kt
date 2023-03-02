package com.example.tp4_1

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tp4_1.databinding.ActivityRegisterBinding
import java.util.*
import android.content.Intent
import android.graphics.Bitmap
import android.provider.MediaStore
import android.telephony.SmsManager
import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Base64.encodeToString
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import android.util.Base64
import android.view.View
import androidx.core.app.NotificationCompat
import androidx.core.content.edit
import com.google.gson.Gson
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


private val requestImageCapture = 1
private val requestGallery = 2
private val REQUEST_SMS = 1


class RegisterActivity : AppCompatActivity() {

    private lateinit var bindingRegister : ActivityRegisterBinding
    private val pwd = generatePwd(8)
    private lateinit var imageBitmap :Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingRegister = ActivityRegisterBinding.inflate(layoutInflater)
        val view = bindingRegister.root
        setContentView(view)

        bindingRegister.btnsignup.setOnClickListener{
            val nom = bindingRegister.nom.text.toString()
            val phoneNumber = bindingRegister.num.text.toString()
            val numcarte = bindingRegister.cardnum.text.toString()
            if (nom.length == 0) {
                bindingRegister.nom.error = "Entrer votre nom SVP"
            }
            if (phoneNumber.length == 0) {
                bindingRegister.num.error = "Entrer votre numéro de télephone SVP"
            }
            if (numcarte.length == 0) {
                bindingRegister.cardnum.error = "Entrer votre numéro de carte"
            }
            else {
                val new = User ( null ,nom ,phoneNumber, "" , numcarte  , pwd )

                val filesDir = getApplicationContext().getFilesDir()
                val file = File(filesDir, "image" + ".png")
                val bos = ByteArrayOutputStream()
                imageBitmap.compress(Bitmap.CompressFormat.PNG, 0, bos)
                val bitmapdata = bos.toByteArray()
                val fos = FileOutputStream(file)
                fos.write(bitmapdata)
                fos.flush()
                fos.close()
                val reqFile = RequestBody.create(MediaType.parse("image/*"), file)
                val image = MultipartBody.Part.createFormData("image", file.getName(), reqFile)
                val userBody =  MultipartBody.Part.createFormData("user", Gson().toJson(new))
                AddUser(image,userBody)

            }

        }

        // Prendre une photo avec l'appareil photo
        bindingRegister.imagefromcamera.setOnClickListener {
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                takePictureIntent.resolveActivity(packageManager)?.also {
                    startActivityForResult(takePictureIntent, requestImageCapture)
                }
            }
        }

        // Sélectionner une image de la galerie
        bindingRegister.imagefromgallery.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, requestGallery)
        }

    }

    fun notif( pwd : String){
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationId = 1
        val channelId = "channel-01"
        val channelName = "My Channel"

        val notificationIntent = Intent(this, RegisterActivity::class.java)

        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Registration")
            .setContentText("Votre mot de passe est :$pwd")
            .setSmallIcon(R.drawable.eye)
            .setContentIntent(pendingIntent)
            .build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(notificationId, notification)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == requestImageCapture && resultCode == RESULT_OK) {
            imageBitmap = data?.extras?.get("data") as Bitmap
            bindingRegister.appercupermis.setImageBitmap(imageBitmap)

        } else if (requestCode == requestGallery) {
            //var imageData: Bitmap = data?.extras?.get("data") as Bitmap
            val uri = data?.data
            imageBitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
            bindingRegister.appercupermis.setImageBitmap(imageBitmap)

        }
    }

    private fun generatePwd(passwordLength :Int):String {
        val characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        val random = Random()
        var password = ""
        for (i in 0 until passwordLength) {
            password += characters[random.nextInt(characters.length)]
        }
        return password
    }

    private fun AddUser(user : MultipartBody.Part , img : MultipartBody.Part) {
        CoroutineScope(Dispatchers.IO).launch {
            try{
                val response = RetrofitService.endpoint.adduser(user , img)
                withContext(Dispatchers.Main){
                    if(response.isSuccessful){
                        Toast.makeText(this@RegisterActivity , "Added" + response.code(), Toast.LENGTH_SHORT).show();
                        notif(pwd)
                        val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    else{
                        Toast.makeText(this@RegisterActivity , "Message d'erreur" + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }}
                catch (_: InterruptedException) {
                } catch (_: IOException) {
                } catch (_: NullPointerException) {
                } catch (_: IllegalStateException) {
                }
            }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_SMS -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay!
                    Toast.makeText( this , "Done" , Toast.LENGTH_SHORT).show()
                    notif(pwd)
                    sendSMS(bindingRegister.num.toString() , pwd)
                } else {
                    Toast.makeText( this , "Permission denited" , Toast.LENGTH_SHORT).show()
                }}}}

    private fun sendSMS(phoneNumber : String , pwd : String) {
        val message = "Votre mot de passe est : $pwd"

        val sentIntent = Intent("SENT_SMS_ACTION")
        val sentPendingIntent: PendingIntent = PendingIntent.getBroadcast(this, 0, sentIntent, 0)

        val smsManager = SmsManager.getDefault()
        smsManager.sendTextMessage(phoneNumber, null, message, sentPendingIntent, null)

    }

    }




