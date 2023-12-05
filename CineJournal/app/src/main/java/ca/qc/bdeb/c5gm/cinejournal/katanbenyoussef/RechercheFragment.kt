package ca.qc.bdeb.c5gm.cinejournal.katanbenyoussef

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RechercheFragment : Fragment() {
    private lateinit var sharedViewModel: FilmViewModel
    private lateinit var searchButton: Button
    private lateinit var filmRechercheInput: EditText

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModel = ViewModelProvider(requireActivity()).get(FilmViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_recherche, container, false)

        searchButton = view.findViewById(R.id.boutonRechercher)
        filmRechercheInput = view.findViewById(R.id.filmRechercheInput)

        searchButton.setOnClickListener {
            lifecycleScope.launch {
                try {
                    val searchTerm = filmRechercheInput.text.toString()
                    if (searchTerm.isNotEmpty()) {
                        val response = ApiClient.apiService.searchMovies(searchTerm)

                        if (response.isSuccessful) {
                            val filmResults = response.body()?.results
                            Log.d("main", filmResults.toString())
                            sharedViewModel.setMovieResults(filmResults)
                        } else {
                            afficherErreurSnackbar()
                        }
                    }
                } catch (e: Exception) {
                    afficherErreurSnackbar()
                }
            }
        }

        return view
    }
    private fun afficherErreurSnackbar() {
        view?.let { view ->
            val snackbar = Snackbar.make(
                view,
                "Une erreur s'est produite. Veuillez réessayer.",
                Snackbar.LENGTH_INDEFINITE
            )

            snackbar.setAction("Réessayer") {
                lifecycleScope.launch {
                    relancerRecherche()
                }
            }

            snackbar.show()
        }
    }

    private suspend fun relancerRecherche() {
        try {
            val searchTerm = filmRechercheInput.text.toString()
            if (searchTerm.isNotEmpty()) {
                val response = withContext(Dispatchers.IO) {
                    ApiClient.apiService.searchMovies(searchTerm)
                }

                if (response.isSuccessful) {
                    val filmResults = response.body()?.results
                    sharedViewModel.setMovieResults(filmResults)
                }
            }
        } catch (e: Exception) {
            afficherErreurSnackbar()
        }
    }
}
