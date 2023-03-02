package com.example.tp4_1

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.tp4_1.databinding.FragmentReservationBinding
import com.psp.bluetoothlibrary.Connection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.io.IOException



class ReservationFragment : Fragment() {
    lateinit var bindingReservation: FragmentReservationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bindingReservation = FragmentReservationBinding.inflate( inflater , container , false )
        return bindingReservation.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!connected())
            Toast.makeText(requireActivity(), "Verifier votre connexion internet" , Toast.LENGTH_SHORT).show()
        else
            getReservations()


    }

    private fun connected (): Boolean {
        val connectivityManager = requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true
        return isConnected
    }



    private fun getReservations() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val pref = requireActivity().getSharedPreferences("fileConnexion" , AppCompatActivity.MODE_PRIVATE)
                val userid = pref.getInt("idUser",0)
                val response = RetrofitService.endpoint.getreservation(userid)
                withContext(Dispatchers.Main){
                    if(response.isSuccessful){
                        var data = response.body()
                        if (data != null) {
                            val carModel = ViewModelProvider(requireActivity()).get(CarModel::class.java)
                            bindingReservation.recyclerview3.layoutManager = GridLayoutManager(requireActivity(),1)
                            bindingReservation.recyclerview3.adapter = ReservationAdapter(requireActivity(), data , carModel.cars)
                        }
                        else{
                            bindingReservation.aucuneres.visibility = View.VISIBLE
                        }
                    }
                    else{
                        Toast.makeText(requireActivity(), "ERREUR " +response.code().toString() , Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: InterruptedException) {
            } catch (e: IOException) {
            } catch (e: NullPointerException) {
            } catch (e: IllegalStateException) { }
        }
    }


}

