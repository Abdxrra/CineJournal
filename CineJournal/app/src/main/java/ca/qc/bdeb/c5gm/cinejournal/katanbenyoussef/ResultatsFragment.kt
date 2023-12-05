package ca.qc.bdeb.c5gm.cinejournal.katanbenyoussef

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ResultatsFragment : Fragment() {
    private val sharedViewModel: FilmViewModel by activityViewModels()
    private lateinit var recyclerViewResults: RecyclerView
    private lateinit var adapteurRecherche: AdapteurRechercheResultats
    private lateinit var adapteurFilms: AdapteurListeFilm
    lateinit var activityAjouter: ActivityResultLauncher<Intent>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerViewResults = view.findViewById(R.id.resultatRecyclerView)
        recyclerViewResults.layoutManager = LinearLayoutManager(requireContext())
        adapteurRecherche = AdapteurRechercheResultats(emptyList())
        recyclerViewResults.adapter = adapteurRecherche
        adapteurFilms = AdapteurListeFilm(
            requireContext(),
            MainActivity(),
            ArrayList<Film>(),
            { film ->  adapterOnclick(film) } )

        activityAjouter = registerForActivityResult(
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
                        val uid = withContext(Dispatchers.IO) {
                            val dao = AppDatabase.getDatabase(requireContext()).clientDao()
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
                        adapteurFilms.addFilm(
                            Film(
                                uid.toInt(),
                                titre,
                                description,
                                annee,
                                rating,
                                imageUri
                            )
                        )
                    }

                }
            }
        }

        adapteurRecherche.setOnItemClickListener(object : AdapteurRechercheResultats.OnItemClickListener {
            override fun onItemClick(item: FilmResults) {
                val intentMsg = Intent(requireContext(), MainActivity::class.java)
                Log.d("main", item.toString())
                intentMsg.putExtra(EXTRA_MODE, "Recherche")
                intentMsg.putExtra(EXTRA_TITRE, item.title)
                intentMsg.putExtra(EXTRA_SLOGAN, item.overview)
                intentMsg.putExtra(EXTRA_ANNEE, item.release_date.split("-")[0].toInt())
                intentMsg.putExtra(EXTRA_NOTE, item.vote_average / 2)
                intentMsg.putExtra(EXTRA_IMAGE, "https://image.tmdb.org/t/p/w500${item.poster_path}")

                activityAjouter.launch(intentMsg)
            }
        })
    }
        override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_resultats, container, false)
        recyclerViewResults = view.findViewById(R.id.resultatRecyclerView)

        adapteurRecherche = AdapteurRechercheResultats(emptyList())
        recyclerViewResults.adapter = adapteurRecherche
        recyclerViewResults.layoutManager = LinearLayoutManager(requireContext())

        sharedViewModel.movieResults.observe(viewLifecycleOwner) { filmResults ->
            adapteurRecherche.updateData(filmResults)
        }

        return view
    }

    private fun adapterOnclick(film: Film) {

        val intentMsg = Intent(requireContext(), MainActivity::class.java)
        intentMsg.putExtra(EXTRA_MODE, "Edit")
        intentMsg.putExtra(EXTRA_UID, film.uid)
        intentMsg.putExtra(EXTRA_TITRE, film.titre)
        intentMsg.putExtra(EXTRA_SLOGAN, film.description)
        intentMsg.putExtra(EXTRA_ANNEE, film.annee)
        intentMsg.putExtra(EXTRA_NOTE, film.rating)
        intentMsg.putExtra(EXTRA_IMAGE, film.imageUri)

        activityAjouter.launch(intentMsg)
    }
}
