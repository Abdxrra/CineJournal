package ca.qc.bdeb.c5gm.cinejournal.katanbenyoussef

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import ca.qc.bdeb.c5gm.cinejournal.katanbenyoussef.EXTRA_MODE
import ca.qc.bdeb.c5gm.cinejournal.katanbenyoussef.EXTRA_TITRE
import ca.qc.bdeb.c5gm.cinejournal.katanbenyoussef.EXTRA_SLOGAN
import ca.qc.bdeb.c5gm.cinejournal.katanbenyoussef.EXTRA_ANNEE
import ca.qc.bdeb.c5gm.cinejournal.katanbenyoussef.EXTRA_NOTE
import ca.qc.bdeb.c5gm.cinejournal.katanbenyoussef.EXTRA_IMAGE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class AjouterEditerFilm : AppCompatActivity() {

    lateinit var modeActivity: TextView
    lateinit var editTitre: EditText
    lateinit var editSlogan: EditText
    lateinit var editAnnee: EditText
    lateinit var editFilmRating: RatingBar
    lateinit var editFilmImage: ImageView
    var uriFilm: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ajout_edit_film)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        modeActivity = findViewById(R.id.modeActivity)
        editTitre = findViewById(R.id.editTitre)
        editSlogan = findViewById(R.id.editSlogan)
        editAnnee = findViewById(R.id.editAnnee)
        editFilmRating = findViewById(R.id.editFilmRating)
        editFilmImage = findViewById(R.id.editFilmImage)

        val extras = intent.extras
        if (extras != null) {
            val mode = extras.getString(EXTRA_MODE)
            modeActivity.text = when (mode) {
                "Edit" -> "Modifier un Film"
                else -> "Nouveau un Film"
            }

            if(extras.containsKey("Titre")){
                editTitre.setText(extras.getString(EXTRA_TITRE))
                editSlogan.setText(extras.getString(EXTRA_SLOGAN))
                editAnnee.setText(extras.getString(EXTRA_ANNEE))
                editFilmRating.rating = (extras.getString(EXTRA_NOTE)!!.toFloat())
                uriFilm = extras.getString(EXTRA_IMAGE)!!.toString()
                editFilmImage.setImageURI(uriFilm.toUri())
            }
        }

    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    fun sauvegarder(v: View){

        val intentMsg = Intent()
        intentMsg.putExtra(EXTRA_TITRE, editTitre.text.toString())
        intentMsg.putExtra(EXTRA_SLOGAN, editSlogan.text.toString())
        intentMsg.putExtra(EXTRA_ANNEE, editAnnee.text.toString().toInt())
        intentMsg.putExtra(EXTRA_NOTE, editFilmRating.rating.toDouble())
        intentMsg.putExtra(EXTRA_IMAGE, uriFilm)
        setResult(RESULT_OK, intentMsg)
        finish()
    }
}