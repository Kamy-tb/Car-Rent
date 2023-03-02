package com.example.tp4_1
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface EndPoint {
    @GET("getuser/{tel}/{pwd}")
    suspend fun verifyUser(@Path ("tel")tel: String,@Path("pwd")pwd:String):Response<User?>

    @GET("getcars")
    suspend fun getcars():Response<List<Voiture>>

    @GET("getcar/{id}")
    suspend fun getcar(@Path ("id")id: Int):Response<Voiture?>

    @GET("getreservation/{userid}")
    suspend fun getreservation(@Path ("userid")userid: Int):Response<List<Reservation>>

    @Multipart
    @POST("adduser")
    suspend fun adduser(@Part image:MultipartBody.Part ,
                        @Part user:MultipartBody.Part ) : Response<String>

    @POST("addreservation")
    suspend fun addreservation(@Body reservation: Reservation) : Response<String>

    @PATCH("modifierpwd")
    suspend fun modifiermdp(@Body x:Newpwd  ) : Response<String>

    @GET("gettrajets/{id}")
    suspend fun gettrajets(@Path ("id")id: Int):Response<List<Trajet>>
}
