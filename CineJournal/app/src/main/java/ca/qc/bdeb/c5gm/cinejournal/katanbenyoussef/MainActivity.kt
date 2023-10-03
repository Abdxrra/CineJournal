package ca.qc.bdeb.c5gm.cinejournal.katanbenyoussef

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 1. BD avec clients
        // 1.5 Add client dans BD avec COROUTINE
        val nouveauClient = Client(null, "Abder", "Katan", "514-444-3333")
        lifecycleScope.launch {
            insererClient(nouveauClient)

            val client = getClient()
            val texte:TextView = findViewById(R.id.text)
            texte.setText(client.nom)
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