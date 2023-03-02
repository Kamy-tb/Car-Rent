package com.example.tp4_1

import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
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
import com.example.tp4_1.databinding.FragmentChangePwdBinding
import com.example.tp4_1.databinding.FragmentProfilBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException


class ChangePwdFragment : Fragment() {
    lateinit var bindingChange : FragmentChangePwdBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        bindingChange = FragmentChangePwdBinding.inflate(inflater, container, false)
        return bindingChange.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var isPasswordVisible = false
        bindingChange.eye.setOnClickListener(){
            if (isPasswordVisible) {
                bindingChange.pwd.transformationMethod = PasswordTransformationMethod.getInstance()
                bindingChange.eye.setImageResource(R.drawable.eye_slash)
            } else {
                bindingChange.pwd.transformationMethod = null
                bindingChange.eye.setImageResource(R.drawable.eye)
            }
            isPasswordVisible = !isPasswordVisible
            bindingChange.pwd.setSelection(bindingChange.pwd.text.length)
        }

        var isPasswordVisible2 = false
        bindingChange.eye2.setOnClickListener(){
            if (isPasswordVisible2) {
                bindingChange.pwd.transformationMethod = PasswordTransformationMethod.getInstance()
                bindingChange.eye.setImageResource(R.drawable.eye_slash)
            } else {
                bindingChange.pwd.transformationMethod = null
                bindingChange.eye.setImageResource(R.drawable.eye)
            }
            isPasswordVisible2 = !isPasswordVisible2
            bindingChange.pwd.setSelection(bindingChange.pwd.text.length)
        }
        bindingChange.maj.setOnClickListener(){
            val pref = requireActivity().getSharedPreferences("fileConnexion" , AppCompatActivity.MODE_PRIVATE)
            val userid = pref.getInt("idUser",0)
            if ((bindingChange.pwd.text.isEmpty()) ){
                bindingChange.pwd.setError("Entrer le nouveau mot de passe")
            }
            if ((bindingChange.old.text.isEmpty()) ){
                bindingChange.old.setError("Entrer votre ancien mot de passe")
            }
            else{
                changePwd(bindingChange.pwd.text.toString() , userid , bindingChange.old.text.toString() )
            }
        }
        bindingChange.back.setOnClickListener(){
            view.findNavController().navigate(R.id.action_changePwdFragment_to_profilFragment2 )
        }
    }


    private fun changePwd(new:String , userid:Int , old:String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val champ = Newpwd (new,userid ,old )
                val response = RetrofitService.endpoint.modifiermdp(champ)
                withContext(Dispatchers.Main){
                    if(response.isSuccessful){
                        val data = response.body()
                        if (data == "vide") {
                            Toast.makeText(requireActivity(), "Le mot de passe introduit est faux" , Toast.LENGTH_SHORT).show()
                        }
                        else {
                            Toast.makeText(requireActivity(), "Votre mot de passe à été modifer " , Toast.LENGTH_SHORT).show()
                            view?.findNavController()?.navigate(R.id.action_changePwdFragment_to_profilFragment2 )
                        }
                    }
                    else{
                        Toast.makeText(requireActivity(), "erreur lors de la connexion au serveur " +response.code().toString() , Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: InterruptedException) {
                Toast.makeText(requireActivity(), "erreur lors de la connexion au serveur "  , Toast.LENGTH_SHORT).show()
            } catch (e: IOException) {
                Toast.makeText(requireActivity(), "erreur lors de la connexion au serveur "  , Toast.LENGTH_SHORT).show()
            } catch (e: NullPointerException) {
                Toast.makeText(requireActivity(), "erreur lors de la connexion au serveur "  , Toast.LENGTH_SHORT).show()
            } catch (e: IllegalStateException) {
                Toast.makeText(requireActivity(), "erreur lors de la connexion au serveur "  , Toast.LENGTH_SHORT).show()
            }
        }
    }
}