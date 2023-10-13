package ca.qc.bdeb.c5gm.cinejournal.katanbenyoussef

import android.app.Activity
import android.content.ClipData.Item
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.w3c.dom.Text

const val EXTRA_MODE = "ca.qc.bdeb.c5gm.cinejournal.EXTRA_MODE"
const val EXTRA_TITRE = "ca.qc.bdeb.c5gm.cinejournal.EXTRA_TITRE"
const val EXTRA_SLOGAN = "ca.qc.bdeb.c5gm.cinejournal.EXTRA_SLOGAN"
const val EXTRA_ANNEE = "ca.qc.bdeb.c5gm.cinejournal.EXTRA_ANNEE"
const val EXTRA_NOTE = "ca.qc.bdeb.c5gm.cinejournal.EXTRA_NOTE"
const val EXTRA_IMAGE = "ca.qc.bdeb.c5gm.cinejournal.EXTRA_IMAGE"


class MainActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    lateinit var adapteur: AdapteurListeFilm
    lateinit var noFilmText: TextView
    lateinit var trierView: TextView
    val transformToItemView: (Film) -> ItemView =
        { ItemView(it.titre, it.description, it.annee, it.rating, it.imageUri) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        noFilmText = findViewById(R.id.noFilmText)
        trierView = findViewById(R.id.trierMode)
        recyclerView = findViewById(R.id.recyclerView)
        adapteur = AdapteurListeFilm(applicationContext, MainActivity(), ArrayList<ItemView>())
        recyclerView.adapter = adapteur

        addFilmsToView()

        val activityAjouter = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data ?: Intent()
                val extras = intent.extras

                if (extras != null) {
                    val titre: String = extras.getString(EXTRA_TITRE)!!
                    val description: String = extras.getString(EXTRA_SLOGAN)!!
                    val annee: Int = extras.getInt(EXTRA_ANNEE)
                    val rating: Double = extras.getDouble(EXTRA_NOTE)
                    val imageUri: String = extras.getString(EXTRA_IMAGE)!!

                    Log.d("main", titre)

                    lifecycleScope.launch {
                        withContext(Dispatchers.IO) {
                            val dao = AppDatabase.getDatabase(applicationContext).clientDao()
                            dao.insertAll(
                                Film(
                                    null,
                                    titre,
                                    description,
                                    annee,
                                    rating,
                                    imageUri
                                )
                            )
                        }
                    }
                    adapteur.addFilm(ItemView(titre, description, annee, rating, imageUri))
                    updateRecyclerView()

                }
            }
        }

        var ajouterBtn: Button = findViewById(R.id.ajouter)
        ajouterBtn.setOnClickListener {
            val intent = Intent(applicationContext, AjouterEditerFilm::class.java)
            intent.putExtra(EXTRA_MODE, "Ajouter")
            activityAjouter.launch(intent)
        }

    }

    fun addFilmsToView() {

        lifecycleScope.launch {
            val films = withContext(Dispatchers.IO) {
                AppDatabase.getDatabase(applicationContext).clientDao().getAll()
            }

            val transformToItemView: (Film) -> ItemView =
                { ItemView(it.titre, it.description, it.annee, it.rating, it.imageUri) }
            adapteur.addAllFilms(films.map(transformToItemView) as ArrayList<ItemView>)

            updateRecyclerView()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
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
                deleteAllFilms()
                updateRecyclerView()
            }

            R.id.trierAnnee -> {
                trierView.setText(R.string.tri_par_annee)
                trierFilms {
                    AppDatabase.getDatabase(applicationContext).clientDao().trierParAnnee()
                }
            }

            R.id.trierNote -> {
                trierView.setText(R.string.tri_par_note)
                trierFilms {
                    AppDatabase.getDatabase(applicationContext).clientDao().trierParNote()
                }
            }

            R.id.trierTitre -> {
                trierView.setText(R.string.tri_par_titre)
                trierFilms {
                    AppDatabase.getDatabase(applicationContext).clientDao().trierParTitre()
                }
            }

            else -> false
        }
        return true
    }

    fun trierFilms(trierFunction: suspend () -> List<Film>) {
        lifecycleScope.launch {
            val listeTrie = withContext(Dispatchers.IO) { trierFunction() }
            adapteur.addAllFilms(listeTrie.map(transformToItemView) as ArrayList<ItemView>)
            updateRecyclerView()
        }
    }

    fun deleteAllFilms() {
        adapteur.deleteAllFilms()
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                AppDatabase.getDatabase(applicationContext).clientDao().deleteAll()
            }
        }
    }

    fun updateRecyclerView() {
        if (adapteur.itemCount != 0) {
            noFilmText.visibility = INVISIBLE
            recyclerView.visibility = VISIBLE
        }
        if (adapteur.itemCount == 0) {
            noFilmText.visibility = VISIBLE
            recyclerView.visibility = INVISIBLE
        }

    }
}
