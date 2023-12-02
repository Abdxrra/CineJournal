package ca.qc.bdeb.c5gm.cinejournal.katanbenyoussef

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.launch

class RechercheFragment : Fragment() {
    private lateinit var sharedViewModel: FilmViewModel
    private lateinit var searchButton: Button
    private lateinit var filmRechercheInput: TextInputLayout

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
            val searchTerm = filmRechercheInput.editText?.text.toString()
            if (searchTerm.isNotEmpty()) {
                lifecycleScope.launch {
                    try {
                        val response = ApiClient.apiService.searchMovies(searchTerm)

                        if (response.isSuccessful) {
                            val filmResults = response.body()?.results
                            sharedViewModel.setMovieResults(filmResults)
                        } else {
                            // Handle API error
                        }
                    } catch (e: Exception) {
                        // Handle network error
                    }
                }
            } else {
                // Handle empty search term
            }
        }

        return view
    }
}
