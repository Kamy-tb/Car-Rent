package com.example.tp4_1

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class TrajetAdapter(val context: Context, var trajet:List<Trajet> ): RecyclerView.Adapter<ViewHolderTrajet>()  {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderTrajet {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(parent.context).inflate(R.layout.trajet_layout, parent , false)
        return ViewHolderTrajet(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolderTrajet, position: Int) {
        holder.apply {
            debut.text = trajet[position].datedebut + " "+ trajet[position].timedebut
            fin.text = trajet[position].datefin + " "+ trajet[position].timefin
            cout2.text = trajet[position].cout.toString() + "DA"
        }

    }

    override fun getItemCount(): Int {
        return trajet.size
    }
}
class ViewHolderTrajet(val view : View) : RecyclerView.ViewHolder(view) {
    var debut = view.findViewById(R.id.datedebut) as TextView
    var fin = view.findViewById(R.id.datefin) as TextView
    var cout2 = view.findViewById(R.id.cout2) as TextView
}