package com.tfxsoftware.toothhero


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

}
