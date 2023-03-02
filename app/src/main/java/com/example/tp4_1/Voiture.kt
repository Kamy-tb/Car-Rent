package com.example.tp4_1

data class Voiture(
    val id : Int,
    var marque:String,
    var modele:String,
    var typeMoteur : String,
    var disponible : Int,
    var img : String,
    var latitude: Double,
    var longitude: Double,
    var capacity : Int,
    var tarif : Double,
    var lieu : String
)
