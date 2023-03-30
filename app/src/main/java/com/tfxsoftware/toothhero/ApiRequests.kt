package com.tfxsoftware.toothhero

import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

class ApiRequests() {

    private val registerUrl = "https://us-central1-toothhero-4102d.cloudfunctions.net/addNewData"

    suspend fun addNovoDentista(dentista: Dentista): String = withContext(Dispatchers.IO) {
        val json = Gson().toJson(dentista)
        val requestBody = json.toRequestBody("application/json".toMediaTypeOrNull())
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(registerUrl)
            .post(requestBody)
            .build()
        val response = client.newCall(request).execute()
        return@withContext response.body?.string() ?: "contato"
    }
}