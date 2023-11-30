package ca.qc.bdeb.c5gm.cinejournal.katanbenyoussef

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RechercheActivity : AppCompatActivity() {

    lateinit var searchButton: Button
    lateinit var filmRechercheInput: TextInputLayout
    lateinit var textViewRecherche: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recherche_film)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Rechercher un film"

        setUpSearchFunctionality()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun setUpSearchFunctionality() {
        searchButton = findViewById(R.id.button2)
        filmRechercheInput = findViewById(R.id.filmRechercheInput)
        textViewRecherche = findViewById(R.id.textView9)

        searchButton.setOnClickListener {
            val searchTerm = filmRechercheInput.editText?.text.toString()
            if (searchTerm.isNotEmpty()) {
                lifecycleScope.launch {
                    try {
                        val response = ApiClient.apiService.searchMovies(
                            query = searchTerm,
                            language = "en-US",
                            page = 1,
                            authorization = "Bearer ${BuildConfig.API_KEY_TMDB}"
                        )

                        if (response.isSuccessful) {
                            val filmResults = response.body()?.results

                            if (filmResults.isNullOrEmpty()) {
                                textViewRecherche.text = "No results found."
                            } else {
                                // Assuming you want to display the first result
                                val stringBuilder = StringBuilder()
                                for (film in filmResults) {
                                    stringBuilder.append("${film.title}\n")
                                }
                                textViewRecherche.text = stringBuilder.toString().trim()
                            }
                        } else {
                            Log.e("APIERROR", "API error: ${response.code()}")
                        }
                    } catch (e: Exception) {
                        Log.e("APIERROR", "Api error in fetchAndDisplay", e)
                    }
                }
            } else {
                Log.d("EMPTY SEARCH", "BYE BYE BEN !!!?")
            }
        }
    }
}