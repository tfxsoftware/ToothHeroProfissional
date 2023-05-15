package com.tfxsoftware.toothhero


import android.util.Log
import com.google.firebase.functions.FirebaseFunctions

import com.google.gson.Gson

import org.json.JSONObject



class ApiRequests {
    private val functions = FirebaseFunctions.getInstance("southamerica-east1")

    fun addNovoDentista(dentista: Dentista, callback: (Boolean, String?) -> Unit) {

        val json = Gson().toJson(dentista)
        val jsonObject = JSONObject(json)

        val addData = functions.getHttpsCallable("addNewDentista")
        addData.call(jsonObject)
            .addOnSuccessListener {
                callback(true, null)
            }
            .addOnFailureListener { exception ->
                    callback(false, exception.message)
            }
    }

    fun addNovoAtendimento(atendimento: Atendimento, callback: (Boolean, String?) -> Unit) {

        val json = Gson().toJson(atendimento)
        val jsonObject = JSONObject(json)

        val addData = functions.getHttpsCallable("addNewAtendimento")
        addData.call(jsonObject)
            .addOnSuccessListener {
                callback(true, null)
            }
            .addOnFailureListener { exception ->
                callback(false, exception.message)
            }
    }

    fun getDentista(id: String?, callback: (dentista: Dentista?) -> Unit) {
        val data = hashMapOf("id" to id)
        functions
            .getHttpsCallable("getDentistaById")
            .call(data)
            .continueWith { task ->
                if (task.isSuccessful) {
                    val result = task.result?.data as HashMap<*, *>?
                    val gson = Gson()
                    val json = gson.toJson(result)
                    val dentista = gson.fromJson(json, Dentista::class.java)
                    callback(dentista)
                } else {
                    callback(null)

                }
            }
    }

    fun findEmergencias(callback: (emergencias: List<Emergencia>?) -> Unit){
        functions
            .getHttpsCallable("getEmergenciasAbertas")
            .call()
            .continueWith { task ->
                if (task.isSuccessful) {
                    val result = task.result?.data as String
                    Log.d("emergencias", result)
                    callback(null)

                } else {
                    callback(null)
                    Log.d("emergencias", "deu errado")
                }
            }
    }
}
