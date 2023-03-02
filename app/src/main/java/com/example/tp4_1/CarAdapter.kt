package com.example.tp4_1

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide



class CarAdapter(val context: Context, var cars:List<Voiture>): RecyclerView.Adapter<ViewHolder>()  {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(parent.context).inflate(R.layout.car_layout, parent , false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
           marque.text = cars[position].marque
            modele.text = cars[position].modele
            typemoteur.text = cars[position].typeMoteur
            Glide.with(context).load(url + cars[position].img).into(img)
            dispo.isChecked = cars[position].disponible != 0
            tarif.text = cars[position].tarif.toString() + " /Km"
            lieu.text = cars[position].lieu
            localisation.setOnClickListener {
               val uri = "http://maps.google.com/maps?saddr=&daddr=${cars[position].latitude},${cars[position].longitude}"

               // Launch the Google Maps application
               val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
               intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity")
               ContextCompat.startActivity(context, intent, null)
            }


            img.setOnClickListener {view: View ->
                val data = bundleOf("position" to position)
                view.findNavController().navigate(R.id.action_voituresFragment2_to_detailFragment , data)
            }

        }



    }

    override fun getItemCount(): Int {
        return cars.size
    }
}
class ViewHolder(val view : View) : RecyclerView.ViewHolder(view) {
    var marque = view.findViewById(R.id.marque) as TextView
    var modele = view.findViewById(R.id.modele) as TextView
    var typemoteur = view.findViewById(R.id.typemoteur) as TextView
    var tarif = view.findViewById(R.id.tarif) as TextView
    var lieu = view.findViewById(R.id.lieu) as TextView
    var img = view.findViewById(R.id.imageCar) as ImageView
    var localisation = view.findViewById(R.id.localisation2) as ImageButton
    var dispo = view.findViewById(R.id.disponible) as Switch
}