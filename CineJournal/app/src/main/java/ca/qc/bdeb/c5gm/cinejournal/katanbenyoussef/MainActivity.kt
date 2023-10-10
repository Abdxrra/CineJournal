package ca.qc.bdeb.c5gm.cinejournal.katanbenyoussef

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Adapter
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

class MainActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    lateinit var adapteur: AdapteurListeFilm
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        recyclerView = findViewById(R.id.recyclerView)
        val listeFilms = listOf(
            ItemView(R.drawable.logo_background, "Hello world", "blabla bal blabla", 3.0),
            ItemView(R.drawable.logo_background, "Hello world", "blabla bal blabla", 3.0),
            ItemView(R.drawable.logo_background, "Hello world", "blabla bal blabla", 3.0),
            ItemView(R.drawable.logo_background, "Hello world", "blabla bal blabla", 3.0)
        )
        adapteur = AdapteurListeFilm(applicationContext, this, listeFilms)
        recyclerView.adapter = adapteur

        /*val nouveauClient = Client(null, "Abder", "Katan", "514-444-3333")
        lifecycleScope.launch {
            insererClient(nouveauClient)

            val client = getClient()
            val texte:TextView = findViewById(R.id.text)
            texte.setText(client.nom)
        }*/
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
}
