package com.example.tp4_1

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.tp4_1.databinding.FragmentProfilBinding
import com.example.tp4_1.databinding.FragmentVoituresBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException


class ProfilFragment : Fragment() {
    lateinit var bindingProfil: FragmentProfilBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        bindingProfil = FragmentProfilBinding.inflate( inflater , container , false )
        return bindingProfil.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pref = requireActivity().getSharedPreferences("fileConnexion" , AppCompatActivity.MODE_PRIVATE)
        val name = pref.getString("nom","you")
        val tel = pref.getString("tel","0557920394")
        val anniv = pref.getString("anniv","27-07-2002")
        bindingProfil.name.text = name
        bindingProfil.textView12.text = tel
        if (anniv != null) {
            bindingProfil.textView13.text = anniv.subSequence(0, 10)
        }
        bindingProfil.textView18.text = pref.getString("numcarte","1234958")
        bindingProfil.btnlogout.setOnClickListener(){
            val intent = Intent(requireContext(),LoginActivity::class.java)
            val pref = requireActivity().getSharedPreferences("fileConnexion" , AppCompatActivity.MODE_PRIVATE)
            pref.edit {
                putBoolean("connected" , false)
            }
            this.startActivity(intent)
        }
        bindingProfil.changepwd.setOnClickListener(){
            view.findNavController().navigate(R.id.action_profilFragment2_to_changePwdFragment )
        }

    }

}