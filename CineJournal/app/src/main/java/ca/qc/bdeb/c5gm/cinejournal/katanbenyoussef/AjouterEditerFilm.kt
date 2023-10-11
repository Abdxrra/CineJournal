package ca.qc.bdeb.c5gm.cinejournal.katanbenyoussef

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.net.toUri
import ca.qc.bdeb.c5gm.cinejournal.katanbenyoussef.EXTRA_TITRE
import ca.qc.bdeb.c5gm.cinejournal.katanbenyoussef.EXTRA_SLOGAN
import ca.qc.bdeb.c5gm.cinejournal.katanbenyoussef.EXTRA_ANNEE
import ca.qc.bdeb.c5gm.cinejournal.katanbenyoussef.EXTRA_NOTE
import ca.qc.bdeb.c5gm.cinejournal.katanbenyoussef.EXTRA_IMAGE


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
            val mode = extras.getString("Mode")
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
    fun sauvegarder(){

        val intentMsg = Intent()
        intentMsg.putExtra(EXTRA_TITRE, editTitre.text)
        intentMsg.putExtra(EXTRA_SLOGAN, editSlogan.text)
        intentMsg.putExtra(EXTRA_ANNEE, editAnnee.text)
        intentMsg.putExtra(EXTRA_NOTE, editFilmRating.rating)
        intentMsg.putExtra(EXTRA_IMAGE, uriFilm)
        setResult(RESULT_OK, intentMsg)
        finish()
    }
}