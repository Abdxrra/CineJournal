package ca.qc.bdeb.c5gm.cinejournal.katanbenyoussef

import android.media.Image
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

class AdapteurRechercheResultats(private var filmResults: List<FilmResults>) : RecyclerView.Adapter<AdapteurRechercheResultats.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgFilm: ImageView = itemView.findViewById(R.id.resultatImage)
        val nomFilm: TextView = itemView.findViewById(R.id.resultatFilm)
        val anneeFilm: TextView = itemView.findViewById(R.id.resultatAnnee)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_resultat, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return filmResults.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val filmResult = filmResults[position]

        // Construct the full image URL
        val imageUrl = "https://image.tmdb.org/t/p/w500${filmResult.poster_path}"

        // LIBRAIRIE GLIDE POUR LES IMAGES, LE CACHING, ETC. (l'autre façon ne marchait pas)
        Glide.with(holder.itemView)
            .load(imageUrl)
            .transition(DrawableTransitionOptions.withCrossFade())
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.imgFilm)

        holder.nomFilm.text = filmResult.title
        holder.anneeFilm.text = filmResult.release_date
    }

    fun updateData(newUniversities: List<FilmResults>) {
        filmResults = newUniversities
        notifyDataSetChanged()
    }
}
