package com.example.tp4_1

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName ="trajets" )
data class Trajet (
    @PrimaryKey
    var id : Int ,
    var datedebut:String ,
    var timedebut:String  ,
    var datefin : String,
    var timefin : String,
    var cout : Int,
    var id_reservation : Int ,
    )
