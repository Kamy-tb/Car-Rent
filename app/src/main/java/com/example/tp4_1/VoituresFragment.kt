package com.example.tp4_1

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.tp4_1.databinding.FragmentVoituresBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.io.IOException


class VoituresFragment : Fragment() {


    lateinit var binding: FragmentVoituresBinding
    lateinit var carModel: CarModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       binding = FragmentVoituresBinding.inflate( inflater , container , false )
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        carModel = ViewModelProvider(requireActivity()).get(CarModel::class.java)
        try{
            GetCars()
            if(carModel.cars.isEmpty()) {
                if (!connected())
                {
                    binding.msg.text = "Erreur lors de la connexion au serveur"
                    binding.msg.visibility = View.VISIBLE
                }
                else {
                    binding.progressBar.visibility = View.VISIBLE
                    GetCars()
                }
            }
            else {
                val adapter = CarAdapter(requireActivity(),carModel.cars)
                binding.recyclerview.layoutManager = GridLayoutManager(requireActivity(),1)
                binding.recyclerview.adapter = adapter
            }
        }
        catch(e : Exception ){
            binding.progressBar.visibility = View.INVISIBLE
            binding.msg.text = "Erreur lors de la connexion au serveur"
            binding.msg.visibility = View.VISIBLE
        }
        //binding.searchBar.clearFocus()
        binding.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
        androidx.appcompat.widget.SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                //binding.searchBar.clearFocus()
                val filtercars = carModel.cars.filter { it.lieu.contains(query!!) }
                if (filtercars.isEmpty()){
                    binding.msg.text = "Aucun résultat à cette recherche"
                    binding.msg.visibility = View.VISIBLE
                }
                else {
                    val adapter = CarAdapter(requireActivity(),filtercars)
                    binding.recyclerview.layoutManager = GridLayoutManager(requireActivity(),1)
                    binding.recyclerview.adapter = adapter
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val filtercars = carModel.cars.filter { it.lieu.contains(newText!!) }
                val adapter = CarAdapter(requireActivity(),filtercars)
                binding.recyclerview.layoutManager = GridLayoutManager(requireActivity(),1)
                binding.recyclerview.adapter = adapter
                return false
            } 
        })
        
    }


    private fun connected (): Boolean {
        val connectivityManager = requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true
        return isConnected
    }

    private fun GetCars() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response: Response<List<Voiture>> = RetrofitService.endpoint.getcars()
                withContext(Dispatchers.Main) {
                    binding.progressBar.visibility = View.INVISIBLE
                    binding.msg.visibility = View.INVISIBLE
                    if (response.isSuccessful) {
                        val cars = response.body()
                        if (cars != null) {
                            carModel.cars = cars
                            binding.recyclerview.layoutManager = GridLayoutManager(requireActivity(), 1)
                            binding.recyclerview.adapter = CarAdapter(requireActivity(), cars)
                        }
                    } else {
                        binding.progressBar.visibility = View.INVISIBLE
                        binding.msg.text = "Erreur lors de la connexion au serveur"
                        binding.msg.visibility = View.VISIBLE                    }
                }
            } catch (_: InterruptedException) {

            } catch (_: IOException) {

            } catch (_: NullPointerException) {

            } catch (_: IllegalStateException) {

            }
        }
    }


}