package ca.qc.bdeb.c5gm.cinejournal.katanbenyoussef

import android.content.ClipData.Item
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.Adapter
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.ArrayList

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

        recyclerView = findViewById(R.id.recyclerView)
        val listeFilmss = ArrayList<ItemView>()
        listeFilmss.add(ItemView(R.drawable.logo_background, "Hi", "dsadsadsad", 2.0, 2013))

        val listeFilms = mutableListOf(
            ItemView(R.drawable.logo_background, "Hello world", "blabla bal blabla", 2.0, 2002),
            ItemView(R.drawable.logo_background, "Hello world1", "blabla bal blabla", 3.5, 2003),
            ItemView(R.drawable.logo_background, "Hello world3", "blabla bal blabla", 1.0, 2015),
            ItemView(R.drawable.logo_background, "Hello world4", "blabla bal blabla", 4.5, 2023)
        )

        listeFilmss.addAll(listeFilms)
        adapteur = AdapteurListeFilm(applicationContext, this, listeFilmss)
        recyclerView.adapter = adapteur

        var ajouterBtn: Button = findViewById(R.id.ajouter)
        ajouterBtn.setOnClickListener{
            val intent = Intent(applicationContext, AjouterEditerFilm::class.java)
            intent.putExtra("Mode", "Ajouter");
            startActivity(intent)
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

    suspend fun insererClient(nouveauClient: Client) = withContext(Dispatchers.IO) {
        val dao = AppDatabase.getDatabase(applicationContext).clientDao()
        dao.insertAll(nouveauClient)
    }

    suspend fun getClient() = withContext(Dispatchers.IO) {
        val dao = AppDatabase.getDatabase(applicationContext).clientDao()
        dao.findByName("abder", "katan")
    }

    fun ajouterFilm() {
        // ajouter code pour ajouter un film dans la bd et la liste
        updateRecyclerView()
    }

    fun updateRecyclerView(){
        if(adapteur.itemCount != 0){
            noFilmText.visibility = INVISIBLE
            recyclerView.visibility = VISIBLE
        }
    }
}
