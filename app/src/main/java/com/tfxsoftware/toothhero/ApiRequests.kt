package com.tfxsoftware.toothhero

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.FirebaseFunctionsException
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.gson.JsonObject
import org.json.JSONObject
import java.util.concurrent.CountDownLatch


class ApiRequests() {


    fun addNovoDentista(dentista: Dentista) {
        val functions = FirebaseFunctions.getInstance("southamerica-east1")
        val json = Gson().toJson(dentista)
        val jsonObject = JSONObject(json)

        val addData = functions.getHttpsCallable("addNewDentista")
        addData.call(jsonObject)
            .addOnSuccessListener { result ->

            }
            .addOnFailureListener { exception ->
                if (exception is FirebaseFunctionsException) {
                    val code = exception.code
                    val message = exception.message
                    val details = exception.details
                    Log.e(TAG, "Falha:  $code: $message\nDetails: $details", exception)
                } else {
                    Log.e(TAG, "Failed to add data: ", exception)
                }
            }


    }
}