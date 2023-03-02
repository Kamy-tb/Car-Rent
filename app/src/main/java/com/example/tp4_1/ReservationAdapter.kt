package com.example.tp4_1

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.psp.bluetoothlibrary.Connection
import java.io.IOException
import java.io.OutputStream
import java.util.*



// Bluetooth device address (MAC address)

class ReservationAdapter(val context: Context, var data:List<Reservation>, var detail: List<Voiture>): RecyclerView.Adapter<ViewHolderReservation>()  {

    val REQUEST_ENABLE_BT = 1
    val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderReservation {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.reservation_layout, parent , false)
        return ViewHolderReservation(view)
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onBindViewHolder(holder: ViewHolderReservation, position: Int) {
        holder.apply {
            //img.setImageResource(detail.img)
            rv_date.text = data[position].datedebut
            if (detail.isNullOrEmpty()){

            }
            else {
                modele.text = detail[data[position].voitureid].modele
                marque.text = detail[data[position].voitureid].marque
                rv_type.text = detail[data[position].voitureid].typeMoteur
                Glide.with(context).load(url + detail[data[position].voitureid].img).into(img)
            }
            pin.text = data[position].pin.toString()

            changeStateBtn.setOnClickListener {
                val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
                var deviceAddress = "0C:02:BD:0D:88:1B" // Replace with your car's MAC address
                deviceAddress = "4C:6F:9C:48:95:4C" //oppo
                val remoteDevice: BluetoothDevice? = bluetoothAdapter?.getRemoteDevice(deviceAddress)
                val serviceUuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

                if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.BLUETOOTH
                    ) == PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.BLUETOOTH_ADMIN
                    ) == PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.BLUETOOTH_CONNECT
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    val bluetoothSocket: BluetoothSocket = remoteDevice?.createInsecureRfcommSocketToServiceRecord(serviceUuid)!!
                    Toast.makeText(context, "debut" , Toast.LENGTH_SHORT).show()

                    bluetoothAdapter.cancelDiscovery()
                    bluetoothSocket.connect()

                    val outputStream: OutputStream = bluetoothSocket.outputStream
                    val message = "Hello".toByteArray()
                    outputStream.write(message)
                    Toast.makeText(context, "bda srx" , Toast.LENGTH_SHORT).show()
                    outputStream.close()
                    bluetoothSocket.close()
                } else {
                    ActivityCompat.requestPermissions(
                        context as Activity,
                        arrayOf(
                            Manifest.permission.BLUETOOTH,
                            Manifest.permission.BLUETOOTH_ADMIN,
                            Manifest.permission.BLUETOOTH_CONNECT
                        ),
                        0
                    )
                }




            }
        }
    }





    override fun getItemCount(): Int {
        return data.size
    }



}


class ViewHolderReservation(val view : View) : RecyclerView.ViewHolder(view) {
    var rv_date = view.findViewById(R.id.reservation_id) as TextView
    var marque = view.findViewById(R.id.datedebut) as TextView
    var modele = view.findViewById(R.id.datefin) as TextView
    var rv_type = view.findViewById(R.id.type) as TextView
    var img = view.findViewById(R.id.img) as ImageView
    var changeStateBtn = view.findViewById(R.id.smart)  as ImageButton
    var pin = view.findViewById(R.id.cout2) as TextView
}