package ca.qc.bdeb.c5gm.cinejournal.katanbenyoussef

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

const val EXTRA_MODE = "ca.qc.bdeb.c5gm.cinejournal.EXTRA_MODE"
const val EXTRA_TITRE = "ca.qc.bdeb.c5gm.cinejournal.EXTRA_TITRE"
const val EXTRA_SLOGAN = "ca.qc.bdeb.c5gm.cinejournal.EXTRA_SLOGAN"
const val EXTRA_ANNEE = "ca.qc.bdeb.c5gm.cinejournal.EXTRA_ANNEE"
const val EXTRA_NOTE = "ca.qc.bdeb.c5gm.cinejournal.EXTRA_NOTE"
const val EXTRA_IMAGE = "ca.qc.bdeb.c5gm.cinejournal.EXTRA_IMAGE"
const val EXTRA_UID = "ca.qc.bdeb.c5gm.cinejournal.EXTRA_UID"
const val EXTRA_FILM = "ca.qc.bdeb.c5gm.cinejournal.EXTRA_FILM"



class MainActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    //public lateinit var adapteur: AdapteurListeFilm
    lateinit var noFilmText: TextView
    lateinit var activityEditAddFilm: ActivityResultLauncher<Intent>
    lateinit var trierView: TextView
    val viewModel: CineViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        noFilmText = findViewById(R.id.noFilmText)
        trierView = findViewById(R.id.trierMode)
        recyclerView = findViewById(R.id.recyclerView)

        viewModel.adapteur = AdapteurListeFilm(
            applicationContext,
            MainActivity(),
            ArrayList<Film>(),
            { Film -> adapterOnclick(Film) })
        recyclerView.adapter = viewModel.adapteur
        val cle = BuildConfig.API_KEY_TMDB
        triPreference()
        addFilmsToView()

        activityEditAddFilm = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult -> addFilmsToView() }

        var ajouterBtn: Button = findViewById(R.id.ajouter)
        ajouterBtn.setOnClickListener {
            val intent = Intent(applicationContext, AjouterEditerFilm::class.java)
            intent.putExtra(EXTRA_MODE, "Ajouter")
            activityEditAddFilm.launch(intent)
        }

        // code pour lancer l'activité modifier/ajouter à partir du widget/recherche
        if (intent.extras != null) {
            val newIntent = Intent(applicationContext, AjouterEditerFilm::class.java)
            newIntent.putExtras(intent.extras!!)
            activityEditAddFilm.launch(newIntent)
        }
    }

    private fun adapterOnclick(film: Film) {

        val intentMsg = Intent(applicationContext, AjouterEditerFilm::class.java)
        intentMsg.putExtra(EXTRA_MODE, "Edit")
        intentMsg.putExtra(EXTRA_UID, film.uid)
        intentMsg.putExtra(EXTRA_TITRE, film.titre)
        intentMsg.putExtra(EXTRA_SLOGAN, film.description)
        intentMsg.putExtra(EXTRA_ANNEE, film.annee)
        intentMsg.putExtra(EXTRA_NOTE, film.rating)
        intentMsg.putExtra(EXTRA_IMAGE, film.imageUri)

        activityEditAddFilm.launch(intentMsg)
    }

    fun addFilmsToView() {
        lifecycleScope.launch {
            val films = withContext(Dispatchers.IO) {
                AppDatabase.getDatabase(applicationContext).clientDao().getAll()
            }

            Log.d("main", viewModel.adapteur.toString())
            viewModel.adapteur?.addAllFilms(films)//.map(transformToItemView) as ArrayList<ItemView>)

            updateRecyclerView()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    fun dialogDeConfirmation() {
        val alert = AlertDialog.Builder(this)

        alert.setTitle("Confirmation")
        alert.setMessage("Êtes-vous sûr de vouloir tout supprimer?")

        alert.setPositiveButton("OK") { dialog, which ->
            deleteAllFilms()
            updateRecyclerView()
        }

        alert.setNegativeButton("Cancel") { dialog, which ->
            dialog.dismiss()
        }

        val dialog: AlertDialog = alert.create()

        dialog.show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.aPropos -> {
                val intent = Intent(applicationContext, AProposActivity::class.java)
                startActivity(intent)
            }

            R.id.trouverFilm -> {
                val intent = Intent(applicationContext, RechercheActivity::class.java)
                startActivity(intent)
            }

            R.id.toutSupprimer -> {
                dialogDeConfirmation()
            }

            R.id.trierAnnee -> {
                trierView.setText(R.string.tri_par_annee)
                AppPreferences(applicationContext).setSortOption("Annee")
                trierFilms {
                    AppDatabase.getDatabase(applicationContext).clientDao().trierParAnnee()
                }
            }

            R.id.trierNote -> {
                trierView.setText(R.string.tri_par_note)
                AppPreferences(applicationContext).setSortOption("Note")
                trierFilms {
                    AppDatabase.getDatabase(applicationContext).clientDao().trierParNote()
                }
            }

            R.id.trierTitre -> {
                trierView.setText(R.string.tri_par_titre)
                AppPreferences(applicationContext).setSortOption("Titre")
                trierFilms {
                    AppDatabase.getDatabase(applicationContext).clientDao().trierParTitre()
                }
            }

            else -> false
        }
        return true
    }
    // LE TRI NE MARCHE PAS QUAND ON REDEMARRE ESSAYE DANS UPDATERECYCLERVIEW CA MARCHE PAS NN PLUS
    fun triPreference() {
        val tri = AppPreferences(applicationContext).getSortOption()
        when (tri) {
            "Note" -> {
                trierView.setText(R.string.tri_par_note)
                trierFilms { AppDatabase.getDatabase(applicationContext).clientDao().trierParNote() }
            }
            "Annee" -> {
                trierView.setText(R.string.tri_par_annee)
                trierFilms { AppDatabase.getDatabase(applicationContext).clientDao().trierParAnnee() }
            }
            "Titre" -> {
                trierView.setText(R.string.tri_par_titre)
                trierFilms { AppDatabase.getDatabase(applicationContext).clientDao().trierParTitre() }
            }
        }
    }

    fun trierFilms(trierFunction: suspend () -> List<Film>) {
        lifecycleScope.launch {
            val listeTrie = withContext(Dispatchers.IO) { trierFunction() }
            viewModel.adapteur?.addAllFilms(listeTrie)//.map(transformToItemView) as ArrayList<ItemView>)
            updateRecyclerView()
        }
    }

    fun deleteAllFilms() {
        viewModel.adapteur?.deleteAllFilms()
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                AppDatabase.getDatabase(applicationContext).clientDao().deleteAll()
            }
        }
    }

    fun updateRecyclerView() {
        if (viewModel.adapteur?.itemCount != 0) {
            noFilmText.visibility = INVISIBLE
            recyclerView.visibility = VISIBLE
        }
        if (viewModel.adapteur?.itemCount == 0) {
            noFilmText.visibility = VISIBLE
            recyclerView.visibility = INVISIBLE
        }

    }
}
