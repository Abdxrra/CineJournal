package ca.qc.bdeb.c5gm.cinejournal.katanbenyoussef

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ResultatsFragment : Fragment() {
    private val sharedViewModel: FilmViewModel by activityViewModels()
    private lateinit var recyclerViewResults: RecyclerView
    private lateinit var adapter: AdapteurRechercheResultats

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_resultats, container, false)
        recyclerViewResults = view.findViewById(R.id.resultatRecyclerView)

        adapter = AdapteurRechercheResultats(emptyList())
        recyclerViewResults.adapter = adapter
        recyclerViewResults.layoutManager = LinearLayoutManager(requireContext())

        sharedViewModel.movieResults.observe(viewLifecycleOwner) { filmResults ->
            adapter.updateData(filmResults)
        }

        return view
    }
}
