package ca.qc.bdeb.c5gm.cinejournal.katanbenyoussef

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class AjouterEditerFilm : AppCompatActivity() {

    lateinit var modeActivity: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ajout_edit_film)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        modeActivity = findViewById(R.id.modeActivity)

        val extras = intent.extras
        if (extras != null) {
            val mode = extras.getString("Mode")
            modeActivity.text = when (mode) {
                "Edit" -> "Modifier un Film"
                else -> "Nouveau un Film"
            }

            if(extras.containsKey("Titre")){
                var titre: String? = extras.getString("titre")
                var slogan: String? = extras.getString("slogan")
                var annee: String? = extras.getString("annee")
                var note: String? = extras.getString("note")
                var image: String? = extras.getString("image")

            }
        }

    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}