package com.example.tp4_1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.tp4_1.databinding.FragmentTrajetsBinding
import com.example.tp4_1.databinding.FragmentTrajetsBinding.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.io.IOException
import java.sql.Date
import java.sql.Time
import java.text.SimpleDateFormat


class TrajetsFragment : Fragment() {

    lateinit var bindingTrajet: FragmentTrajetsBinding
    lateinit var carModel: CarModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
          bindingTrajet = FragmentTrajetsBinding.inflate( inflater , container , false )
          return bindingTrajet.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val instance = AppDatabase.buildDatabase(requireContext())
        // Remplir la base de données trajet "first execution only"
        //GetTrajets(1)

        val t = Trajet()
        instance?.getTrajetDao()?.addTrajets(t)
        val data = instance?.getTrajetDao()?.getTrajets()
        bindingTrajet.recyclerview2.layoutManager = GridLayoutManager(requireActivity(),1)
        bindingTrajet.recyclerview2.adapter = data?.let { TrajetAdapter(requireActivity() , it)
        }
    }

    // Pour importer les données dans la base de données interne
    private fun GetTrajets(id:Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val response: Response<List<Trajet>> = RetrofitService.endpoint.gettrajets(id)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    val trajets = response.body()
                    if (trajets != null) {
                        val instance = AppDatabase.buildDatabase(requireContext())
                        for (t in trajets){
                            instance?.getTrajetDao()?.addTrajets(t)
                        }
                    }
                }
            }

        }
    }
}