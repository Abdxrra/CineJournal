package ca.qc.bdeb.c5gm.cinejournal.katanbenyoussef

import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Date
import java.util.Locale


class AjouterEditerFilm : AppCompatActivity() {

    lateinit var modeActivity: TextView
    lateinit var editTitre: EditText
    lateinit var editSlogan: EditText
    lateinit var editAnnee: EditText
    lateinit var editFilmRating: RatingBar
    lateinit var editFilmImage: ImageView
    lateinit var imgBtn: Button
    val viewModel: CineViewModel by viewModels()

    var uriFilm: String = ""
    var uid: Int? = null

    // code pris du labo en classe
    val selectionPhoto =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
            if (uri != null) {

                val imageLocale = creerUriPhoto()
                viewModel.imageSelectionneUri = imageLocale
                val inputStream = contentResolver.openInputStream(uri)
                val outputStream = contentResolver.openOutputStream(imageLocale)

                inputStream?.use { input ->
                    outputStream.use { output ->
                        output?.let { input.copyTo(it) }
                    }
                }

                // On fait quelque chose avec l'image reçue sous forme d'URI
                editFilmImage.setImageURI(viewModel.imageSelectionneUri)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //viewModel = ViewModelProvider(this)[CineViewModel::class.java]
        setContentView(R.layout.activity_ajout_edit_film)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        modeActivity = findViewById(R.id.modeActivity)
        imgBtn = findViewById(R.id.imgBtn)
        editTitre = findViewById(R.id.editTitre)
        editSlogan = findViewById(R.id.editSlogan)
        editAnnee = findViewById(R.id.editAnnee)
        editFilmRating = findViewById(R.id.editFilmRating)
        editFilmImage = findViewById(R.id.editFilmImage)

        val titre = intent.getStringExtra(EXTRA_TITRE)
        val slogan = intent.getStringExtra(EXTRA_SLOGAN)
        val annee = intent.getStringExtra(EXTRA_ANNEE)
        val note = intent.getStringExtra(EXTRA_NOTE)
        val imageUri = intent.getStringExtra(EXTRA_IMAGE)

        val dateSortie = annee?.takeIf { it.isNotBlank() } ?: "1970-01-01"

        try {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val date = LocalDate.parse(dateSortie, formatter)
            val yearString = date.year.toString()
            editTitre.text = Editable.Factory.getInstance().newEditable(titre)
            editSlogan.text = Editable.Factory.getInstance().newEditable(slogan)
            editFilmRating.rating = (note?.toDouble() ?: 0 / 2).toFloat()

            val imageUrl = "https://image.tmdb.org/t/p/w500$imageUri"
            Glide.with(this)
                .load(imageUrl)
                .into(editFilmImage)

            editAnnee.text = Editable.Factory.getInstance().newEditable(yearString)
        } catch (e: DateTimeParseException) {
            e.printStackTrace()
        }

        // rechage l'image si elle à déjà été sélectionné
        editFilmImage.setImageURI(viewModel.imageSelectionneUri)


        val extras = intent.extras
        if (extras != null) {
            val mode = extras.getString(EXTRA_MODE)
            Log.d("Main", mode.toString())
            modeActivity.text = when (mode) {

                // getInt(EXTRA_UID) retourne 0 si c'est null, donc je le met dans un switch
                "Edit" -> {
                    uid = extras.getInt(EXTRA_UID)
                    setExtrasInDisplay(extras)
                    "Modifier un Film"
                }
                "Widget" -> {
                    uid = null
                    setExtrasInDisplay(extras)
                    "Nouveau Film"
                }

                else -> "Nouveau Film"
            }
        }
        imgBtn.setOnClickListener {
            selectionPhoto.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    fun setExtrasInDisplay(extras: Bundle){
        editTitre.setText(extras.getString(EXTRA_TITRE))
        editSlogan.setText(extras.getString(EXTRA_SLOGAN))
        editAnnee.setText(extras.getInt(EXTRA_ANNEE).toString())
        editFilmRating.rating = extras.getDouble(EXTRA_NOTE).toFloat()
        viewModel.imageSelectionneUri = extras.getString(EXTRA_IMAGE)!!.toUri()
        editFilmImage.setImageURI(viewModel.imageSelectionneUri)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun creerUriPhoto(): Uri {
        val timeStamp: String =
            SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val photoFile = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "$timeStamp.jpg")
        return photoFile.toUri()
    }

    fun sauvegarder(v: View) {
        if (TextUtils.isEmpty(editTitre.text)) {
            Toast.makeText(applicationContext, "Le titre est obligatoire !", Toast.LENGTH_SHORT)
                .show()
        }
        else if (TextUtils.isEmpty(editAnnee.text)) {
            Toast.makeText(applicationContext, "L'année est obligatoire !", Toast.LENGTH_SHORT)
                .show()
        } else {

            val dao = AppDatabase.getDatabase(applicationContext).clientDao()

            var filmToAdd = Film(
                uid,
                editTitre.text.toString(),
                editSlogan.text.toString(),
                editAnnee.text.toString().toInt(),
                editFilmRating.rating.toDouble(),
                viewModel.imageSelectionneUri.toString()
            )

            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    if (uid != null){
                        dao.updateOne(filmToAdd)
                        //viewModel.adapteur?.test()
                        viewModel.adapteur?.updateFilm(filmToAdd)
                    } else {
                        dao.insertAll(filmToAdd)
                        Log.d("ttt", viewModel.adapteur.toString())
                        viewModel.adapteur?.addFilm(filmToAdd)
                    }
                }
            }

            finish()
            val toastMessage = "${editTitre.text} ajouté à la liste"
            Toast.makeText(applicationContext, toastMessage, Toast.LENGTH_SHORT).show()
        }
    }

    fun annuler(v: View) {
        finish()
    }
}