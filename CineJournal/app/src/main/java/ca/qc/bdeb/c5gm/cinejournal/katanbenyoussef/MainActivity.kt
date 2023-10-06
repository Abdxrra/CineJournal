package ca.qc.bdeb.c5gm.cinejournal.katanbenyoussef

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

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