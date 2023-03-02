package com.example.tp4_1

import java.sql.Time
import java.util.Date

data class Reservation(
    val datedebut:String,
    val timedebut :String,
    val voitureid:Int,
    val userid :Int,
    val pin :Int
)
