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

    fun findEmergencias(callback: (emergencias: MutableList<Emergencia>?) -> Unit){
        val lista: MutableList<Emergencia> = mutableListOf<Emergencia>()
        functions
            .getHttpsCallable("getEmergenciasAbertas")
            .call()
            .continueWith { task ->
                if (task.isSuccessful) {
                    val result = task.result?.data as List<*>

                    for (i in result.indices){
                        val value = result[i] as HashMap<*,*>
                        val gson = Gson()
                        val json = gson.toJson(value)
                        val emergencia = gson.fromJson(json, Emergencia::class.java)
                        Log.d("id", emergencia.toString())
                        lista.add(emergencia)
                    }


                    Log.d("emergencias", lista.toString())
                    callback(lista)
                } else {
                    callback(null)
                    Log.d("emergencias", task.exception.toString())
                }
            }
    }
    fun findAvaliacoes(id: String?,callback: (avaliacoes: MutableList<Avaliacao>?) -> Unit){
        val data = hashMapOf("id" to id)
        val lista: MutableList<Avaliacao> = mutableListOf<Avaliacao>()
        functions
            .getHttpsCallable("getAvaliacoesByDentista")
            .call(data)
            .continueWith { task ->
                if (task.isSuccessful) {
                    val result = task.result?.data as List<*>

                    for (i in result.indices){
                        val value = result[i] as HashMap<*,*>
                        val gson = Gson()
                        val json = gson.toJson(value)
                        val avaliacao = gson.fromJson(json, Avaliacao::class.java)
                        lista.add(avaliacao)
                    }


                    Log.d("avaliacoes", lista.toString())
                    callback(lista)
                } else {
                    callback(null)
                    Log.d("avaliacoes", task.exception.toString())
                }
            }
    }

    fun getAtendimentoEmAndamento(id: String?, callback: (atendimento: Atendimento?) -> Unit ){
        val data = hashMapOf("id" to id)
        functions
            .getHttpsCallable("getAtendimentoByDentista")
            .call(data)
            .continueWith { task ->
                if (task.isSuccessful) {
                    val result = task.result?.data as HashMap<*, *>?
                    val gson = Gson()
                    val json = gson.toJson(result)
                    val atendimento = gson.fromJson(json, Atendimento::class.java)
                    callback(atendimento)
                } else {
                    callback(null)
                }
            }
    }

    fun fecharAtendimento(id: String?, callback: (success:Boolean) -> Unit ){
        val data = hashMapOf("id" to id)
        functions
            .getHttpsCallable("closeAtendimento")
            .call(data)
            .continueWith { task ->
                if (task.isSuccessful) {
                    callback(true)
                } else {
                    callback(false)
                }
            }
    }

    fun addNovaDisputa(disputa: Disputa, callback: (success: Boolean) -> Unit){
        val json = Gson().toJson(disputa)
        val jsonObject = JSONObject(json)

        val addData = functions.getHttpsCallable("addNewDisputa")
        addData.call(jsonObject)
            .addOnSuccessListener {
                callback(true)
            }
            .addOnFailureListener { _ ->
                callback(false)
            }
    }
    fun getEmergencia(id: String?, callback: (emergencia: Emergencia?) -> Unit) {
        val data = hashMapOf("id" to id)
        functions
            .getHttpsCallable("getEmergenciaById")
            .call(data)
            .continueWith { task ->
                if (task.isSuccessful) {
                    val result = task.result?.data as HashMap<*, *>?
                    val gson = Gson()
                    val json = gson.toJson(result)
                    val emergencia = gson.fromJson(json, Emergencia::class.java)
                    callback(emergencia)
                } else {
                    callback(null)

                }
            }
    }

}
