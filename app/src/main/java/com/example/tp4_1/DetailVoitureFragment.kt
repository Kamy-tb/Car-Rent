package com.example.tp4_1

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.tp4_1.databinding.FragmentDetailVoitureBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class DetailFragment : Fragment() {


    lateinit var binding: FragmentDetailVoitureBinding
    lateinit var carModel: CarModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDetailVoitureBinding.inflate(inflater, container, false)
        val view = binding.root
        return view    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        carModel = ViewModelProvider(requireActivity()).get(CarModel::class.java)
        val position = arguments?.getInt("position")

        val capacity = resources.getStringArray(R.array.capacities)
        if (position != null) {
            val car = carModel.cars.get(position)
            binding.marque2.text = car.marque
            binding.modele2.text = car.modele
            binding.capacite.text = car.capacity.toString() + " sièges"
            binding.tarif2.text = car.tarif.toString() + "/Km"
            Glide.with(requireContext()).load(url + car.img).into(binding.carimage)
            binding.editTextDate.inputType = InputType.TYPE_NULL
            binding.editTextTime.inputType = InputType.TYPE_NULL
            // Date
            binding.editTextDate.setOnClickListener {
                val cal = Calendar.getInstance()
                val picker =  DatePickerDialog.OnDateSetListener { datePicker, year, month, day ->
                    cal.set(Calendar.YEAR, year)
                    cal.set(Calendar.MONTH,month)
                    cal.set(Calendar.DAY_OF_MONTH,day)
                    binding.editTextDate.setText(SimpleDateFormat("yyyy-MM-dd").format(cal.time))
                }

                DatePickerDialog(requireActivity(), picker, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),cal.get(
                    Calendar.DAY_OF_MONTH)).show()
            }
            // TIme
            binding.editTextTime.setOnClickListener {
                // Time
                val cal = Calendar.getInstance()
                val picker =  TimePickerDialog.OnTimeSetListener { timePicker, hour, time ->
                    cal.set(Calendar.HOUR_OF_DAY, hour)
                    cal.set(Calendar.MINUTE,time)
                    binding.editTextTime.setText(SimpleDateFormat("HH:mm").format(cal.time))
                }
                TimePickerDialog(requireActivity(), picker, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
            }

            binding.localisation2.setOnClickListener{
                // Construct the URI for the Google Maps intent
                val uri = "http://maps.google.com/maps?saddr=&daddr=${car.latitude},${car.longitude}"

                // Launch the Google Maps application
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity")
                ContextCompat.startActivity(requireActivity(), intent, null)
            }

            binding.reserve.setOnClickListener(){
                val pref = requireActivity().getSharedPreferences("fileConnexion" , AppCompatActivity.MODE_PRIVATE)
                val userid = pref.getInt("idUser",0)
                if (!connected())
                    Toast.makeText(requireActivity(), "Vérifier votre connexion internet" , Toast.LENGTH_SHORT).show()
                else {
                    if ((binding.editTextDate.text.length == 0 ) ||  (binding.editTextTime.text.length == 0)){
                        Toast.makeText(requireActivity(), "Veuiller verifier les informations de la reservation" , Toast.LENGTH_SHORT).show()
                    }
                    else {
                        var new = Reservation( binding.editTextDate.text.toString(),binding.editTextTime.text.toString(),carModel.cars[position].id,userid,-1)
                        AddReservation(new)
                    }
                }
            }
        }
    }

    private fun connected (): Boolean {
        val connectivityManager = requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true
        return isConnected
    }
    private fun AddReservation(new : Reservation) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = RetrofitService.endpoint.addreservation(new)
            withContext(Dispatchers.Main){
                if(response.isSuccessful){
                    Toast.makeText(requireActivity(), "Reservation ajouté "  , Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(requireActivity(), "ERREUR " +response.code().toString() , Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}