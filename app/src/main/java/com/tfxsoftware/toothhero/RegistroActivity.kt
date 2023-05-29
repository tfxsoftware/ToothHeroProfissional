

package com.tfxsoftware.toothhero


import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.InputType
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.get
import com.google.firebase.FirebaseApp
import com.google.firebase.storage.FirebaseStorage
import android.Manifest
import android.graphics.Bitmap
import android.util.Log

import com.tfxsoftware.toothhero.databinding.ActivityRegistroBinding
import okio.IOException
import java.io.File
import java.io.FileOutputStream


class RegistroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistroBinding
    private val apiRequests = ApiRequests()
    private val CAMERA_PERMISSION_REQUEST_CODE = 100
    private val CAMERA_REQUEST_CODE = 101
    private var imageRef:String? = null
    private var photoUri:Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        FirebaseApp.initializeApp(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)
        supportActionBar!!.hide()
        binding = ActivityRegistroBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnAddFoto.setOnClickListener{
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                openCamera()
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CAMERA),
                    CAMERA_PERMISSION_REQUEST_CODE
                )
            }
        }

        binding.btnAdcEndereco.setOnClickListener {
            binding.btnApagarCampo.visibility = View.VISIBLE

            val newEditText = EditText(this)
            newEditText.id = View.generateViewId()
            newEditText.hint = "Endereço"
            newEditText.inputType = InputType.TYPE_CLASS_TEXT

            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            if (binding.editTextContainer.childCount > 0) {
                layoutParams.topMargin = 16
            }
            binding.editTextContainer.addView(newEditText, layoutParams)
            if (binding.editTextContainer.childCount == 3) {
                binding.btnAdcEndereco.setOnClickListener(null)
                Toast.makeText(this, "Permitido até 3 endereços", Toast.LENGTH_SHORT).show()
            }

        }
        binding.btnApagarCampo.setOnClickListener {
            if(binding.editTextContainer.childCount!=0) {
                binding.editTextContainer.removeViewAt(binding.editTextContainer.childCount - 1)
            }
        }
        // BOTAO CRIAR CONTA
        binding.btnCriarConta.setOnClickListener {
            binding.btnCriarConta.isActivated = false
            val listaEndereco = mutableListOf<String>()
            for (i in 0 until  binding.editTextContainer.childCount) {
                val entry = binding.editTextContainer[i] as EditText
                listaEndereco.add(entry.text.toString())
            }
            if (binding.etSenha.text.toString() == binding.etSenhaConfirm.text.toString()){
                try{
                    photoUri?.let { uploadPhotoToFirebase(it) }

                    val dentista = Dentista(
                        binding.etNome.text.toString(),
                        binding.etTelefone.text.toString(),
                        binding.etEmail.text.toString(),
                        binding.etSenha.text.toString(),
                        binding.etCRO.text.toString(),
                        binding.etCurriculo.text.toString(),
                        listaEndereco,
                        imageRef)
                    apiRequests.addNovoDentista(dentista) { success, error ->
                        if (success) {
                            Toast.makeText(this, "Dentista cadastrado com sucesso!", Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            Toast.makeText(this, error , Toast.LENGTH_SHORT).show()
                        }
                    }

                } catch(e: Exception){
                    Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
                }


            }
        }
    }
    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera()
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            val photo = data?.extras?.get("data") as Bitmap?
            photo?.let {
                binding.Perfil.setImageBitmap(it)
                savePhotoToStorage(it)
            }
        }
    }
    private fun uploadPhotoToFirebase(photoUri: Uri) {
        val storageRef = FirebaseStorage.getInstance().reference
        imageRef = "dentistas/${System.currentTimeMillis()}.jpg"
        val photoRef = storageRef.child(imageRef!!)
        val photoFile = photoUri.path?.let { File(it) }
        val uploadTask = photoRef.putFile(Uri.fromFile(photoFile))
        Log.d("foto", "funcao do firebase foi cchamada")
        uploadTask.addOnSuccessListener {
            // Photo uploaded successfully

        }.addOnFailureListener {
            Log.d("foto", "erro ao add no firestore")
        }
    }

    private fun savePhotoToStorage(photo: Bitmap): Uri? {
        val file = File(externalCacheDir, "photo.jpg")
        try {
            val fileOutputStream = FileOutputStream(file)
            photo.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
            fileOutputStream.close()
            photoUri = Uri.fromFile(file)
            if (photoUri != null) Log.d("foto", photoUri?.path!!)
            return Uri.fromFile(file)
        } catch (e: IOException) {
            Log.d("foto", "erro ao salvar no storage do celular")
        }
        return null
    }
}









