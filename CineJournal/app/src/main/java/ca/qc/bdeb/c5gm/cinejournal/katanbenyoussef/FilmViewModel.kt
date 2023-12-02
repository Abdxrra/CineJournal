package ca.qc.bdeb.c5gm.cinejournal.katanbenyoussef

import androidx.lifecycle.*
import kotlinx.coroutines.*

class FilmViewModel : ViewModel() {
    private val _movieResults = MutableLiveData<List<FilmResults>>()
    val movieResults: LiveData<List<FilmResults>> get() = _movieResults

    fun setMovieResults(results: List<FilmResults>?) {
        _movieResults.value = results ?: emptyList()
    }
}