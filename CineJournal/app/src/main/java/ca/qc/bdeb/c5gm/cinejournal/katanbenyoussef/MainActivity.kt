package ca.qc.bdeb.c5gm.cinejournal.katanbenyoussef

import android.app.Activity
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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
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


class MainActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    lateinit var adapteur: AdapteurListeFilm
    lateinit var noFilmText: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        noFilmText = findViewById(R.id.noFilmText)

        getFilms()

        val activityAjouter = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data?:Intent()
                val extras = intent.extras

                if (extras != null) {
                    val titre: String = extras.getString(EXTRA_TITRE)!!
                    val description: String = extras.getString(EXTRA_SLOGAN)!!
                    val annee: Int = extras.getInt(EXTRA_ANNEE)
                    val rating: Double = extras.getDouble(EXTRA_NOTE)
                    val imageUri: String = extras.getString(EXTRA_IMAGE)!!

                    Log.d("main", titre)

                    lifecycleScope.launch {
                        val liste = withContext(Dispatchers.IO) {
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
                    getFilms()

                }
            }
        }

        var ajouterBtn: Button = findViewById(R.id.ajouter)
        ajouterBtn.setOnClickListener{
            val intent = Intent(applicationContext, AjouterEditerFilm::class.java)
            intent.putExtra(EXTRA_MODE, "Ajouter");
            activityAjouter.launch(intent)
        }

    }

    fun getFilms(){

        var listeFilms = ArrayList<ItemView>()

        lifecycleScope.launch {
            val films = withContext(Dispatchers.IO) {
                AppDatabase.getDatabase(applicationContext).clientDao().getAll()
            }

            val transformToItemView: (Film) -> ItemView = {ItemView( it.titre, it.description, it.annee, it.rating, it.imageUri)}
            listeFilms.addAll(films.map( transformToItemView ))

            recyclerView = findViewById(R.id.recyclerView)
            adapteur = AdapteurListeFilm(applicationContext, MainActivity(), listeFilms)
            recyclerView.adapter = adapteur

        }
    }

    fun addFilms(){
        lifecycleScope.launch {
            // Cette portion roule dans le thread IO
            val liste = withContext(Dispatchers.IO) {
                val dao = AppDatabase.getDatabase(applicationContext).clientDao()

                dao.insertAll(Film(null, "test", "test", 2000, 2.0, ""))
                dao.insertAll(Film(null, "test1", "test", 2000, 2.0, ""))
                dao.insertAll(Film(null, "test2", "test", 2000, 2.0, ""))
                dao.insertAll(Film(null, "test3", "test", 2000, 2.0, ""))
                dao.insertAll(Film(null, "test4", "test", 2000, 2.0, ""))

            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.aPropos -> {
                val intent = Intent(applicationContext, AProposActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.trouverFilm ->{
                val intent = Intent(applicationContext, RechercheActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.toutSupprimer ->{
                // ajouter le code pour update la liste et la BD
                updateRecyclerView()
                true
            }

            else -> false
        }
    }

    fun updateRecyclerView(){
        if(adapteur.itemCount != 0){
            noFilmText.visibility = INVISIBLE
            recyclerView.visibility = VISIBLE
        }
    }
}
